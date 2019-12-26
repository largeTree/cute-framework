package com.qiuxs.cuteframework.tech.log;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Closer;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;

public class CuteDatabaseManager extends AbstractDatabaseManager {
	private static final JdbcDatabaseManagerFactory INSTANCE = new JdbcDatabaseManagerFactory();
	private final List<Column> columns;
	private final ConnectionSource connectionSource;
	private final String sqlStatement;

	private Connection connection;
	private PreparedStatement statement;
	private boolean isBatchSupported;

	private CuteDatabaseManager(final String name, final int bufferSize, final ConnectionSource connectionSource,
			final String sqlStatement, final List<Column> columns) {
		super(name, bufferSize);
		this.connectionSource = connectionSource;
		this.sqlStatement = sqlStatement;
		this.columns = columns;
	}

	@Override
	protected void startupInternal() throws Exception {
		this.connection = this.connectionSource.getConnection();
		final DatabaseMetaData metaData = this.connection.getMetaData();
		this.isBatchSupported = metaData.supportsBatchUpdates();
		Closer.closeSilently(this.connection);
	}

	@Override
	protected void shutdownInternal() throws Exception {
		if (this.connection != null || this.statement != null) {
			this.commitAndClose();
		}
	}

	@Override
	protected void connectAndStart() {
		try {
			this.connection = this.connectionSource.getConnection();
			this.connection.setAutoCommit(false);
			this.statement = this.connection.prepareStatement(this.sqlStatement);
			this.statement.setQueryTimeout(10);// 执行超时时间10s
		} catch (final SQLException e) {
			NoDbLogger.log
					.error("Cannot write logging event or flush buffer; JDBC manager cannot connect to the database: "
							+ e.getMessage(), e);
			throw new AppenderLoggingException(
					"Cannot write logging event or flush buffer; JDBC manager cannot connect to the database.", e);
		} catch (final Exception e) {
			NoDbLogger.log
					.error("Cannot write logging event or flush buffer; JDBC manager cannot connect to the database: "
							+ e.getMessage(), e);
		}
	}

	@Override
	protected void writeInternal(LogEvent event) {
		StringReader reader = null;
		try {
			if (!this.isRunning() || this.connection == null || this.connection.isClosed() || this.statement == null
					|| this.statement.isClosed()) {
				throw new AppenderLoggingException(
						"Cannot write logging event; JDBC manager not connected to the database.");
			}

			int i = 1;
			for (final Column column : this.columns) {
				if (column.isEventTimestamp) {
					this.statement.setTimestamp(i++, new Timestamp(event.getTimeMillis()));
				} else {
					if ("clob".equalsIgnoreCase(column.type)) {
						reader = new StringReader(column.layout.toSerializable(event));
						if (column.isUnicode) {
							this.statement.setNClob(i++, reader);
						} else {
							this.statement.setClob(i++, reader);
						}
					} else if ("int".equalsIgnoreCase(column.type)) {
						String value = column.layout.toSerializable(event);
						if (StringUtils.isNotEmpty(value)) {
							int intValue = Integer.parseInt(value);
							this.statement.setInt(i++, intValue);
						} else {
							this.statement.setInt(i++, 0);
						}
						// 增加long类型的处理。update by liushanhong 2017/5/18
					} else if ("long".equalsIgnoreCase(column.type)) {
						String value = column.layout.toSerializable(event);
						if (StringUtils.isNotEmpty(value)) {
							long longValue = Long.parseLong(value);
							this.statement.setLong(i++, longValue);
						} else {
							this.statement.setLong(i++, 0);
						}
						// 增加时间戳类型的处理。时间戳类型传入的是时间戳长long型数值串
					} else if ("timestamp".equalsIgnoreCase(column.type)) {
						String value = column.layout.toSerializable(event);
						if (StringUtils.isNotEmpty(value)) {
							this.statement.setTimestamp(i++, new Timestamp(Long.valueOf(value)));
						} else {
							this.statement.setTimestamp(i++, null);
						}
					} else {
						// 手动插入serverid，保证未通过LogFilter的日志也能记录serverid
						if (LogConstant.COLUMN_SERVERID.equals(column.name)
								&& EnvironmentContext.getServerId() != null) {
							this.statement.setString(i++, EnvironmentContext.getServerId());
						} else if (column.isUnicode) {
							this.statement.setNString(i++, column.layout.toSerializable(event));
						} else {
							this.statement.setString(i++, column.layout.toSerializable(event));
						}
					}
				}
			}

			if (this.isBatchSupported) {
				this.statement.addBatch();
			} else if (this.statement.executeUpdate() == 0) {
				throw new AppenderLoggingException(
						"No records inserted in database table for log event in JDBC manager.");
			}
		} catch (final SQLException e) {
			NoDbLogger.log.error("Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
			throw new AppenderLoggingException(
					"Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
		} catch (final Exception e) {
			NoDbLogger.log.error("Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
		} finally {
			Closer.closeSilently(reader);
		}
	}

	@Override
	protected void commitAndClose() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				if (this.isBatchSupported) {
					this.statement.executeBatch();
				}
				this.connection.commit();
			}
		} catch (SQLException e) {
			NoDbLogger.log.error("Failed to commit transaction logging event or flushing buffer: " + e.getMessage(), e);
		} finally {
			try {
				Closer.close(this.statement);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.statement = null;
			}
			try {
				Closer.close(this.connection);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.connection = null;
			}
		}
	}

	public static CuteDatabaseManager getJDBCDatabaseManager(final String name, final int bufferSize,
			final ConnectionSource connectionSource, final String tableName, final CuteColumnConfig[] columnConfigs) {
		return AbstractDatabaseManager.getManager(name,
				new FactoryData(bufferSize, connectionSource, tableName, columnConfigs), getFactory());
	}

	private static JdbcDatabaseManagerFactory getFactory() {
		return INSTANCE;
	}

	private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
		private final CuteColumnConfig[] columnConfigs;
		private final ConnectionSource connectionSource;
		private final String tableName;

		protected FactoryData(final int bufferSize, final ConnectionSource connectionSource, final String tableName,
				final CuteColumnConfig[] columnConfigs) {
			super(bufferSize);
			this.connectionSource = connectionSource;
			this.tableName = tableName;
			this.columnConfigs = columnConfigs;
		}
	}

	/**
	 * Creates managers.
	 */
	private static final class JdbcDatabaseManagerFactory implements ManagerFactory<CuteDatabaseManager, FactoryData> {
		@Override
		public CuteDatabaseManager createManager(final String name, final FactoryData data) {
			final StringBuilder columnPart = new StringBuilder();
			final StringBuilder valuePart = new StringBuilder();
			final List<Column> columns = new ArrayList<>();
			int i = 0;
			for (final CuteColumnConfig config : data.columnConfigs) {
				if (i++ > 0) {
					columnPart.append(',');
					valuePart.append(',');
				}

				columnPart.append(config.getColumnName());

				if (config.getLiteralValue() != null) {
					valuePart.append(config.getLiteralValue());
				} else {
					columns.add(new Column(config.getLayout(), config.isEventTimestamp(), config.isUnicode(),
							config.getType(), config.getColumnName()));
					valuePart.append('?');
				}
			}

			final String sqlStatement = "INSERT INTO " + data.tableName + " (" + columnPart + ") VALUES (" + valuePart
					+ ')';

			return new CuteDatabaseManager(name, data.getBufferSize(), data.connectionSource, sqlStatement, columns);
		}

	}

	private static final class Column {
		private final PatternLayout layout;
		private final boolean isEventTimestamp;
		private final boolean isUnicode;
		private final String type;
		private final String name;

		private Column(final PatternLayout layout, final boolean isEventDate, final boolean isUnicode,
				final String type, final String name) {
			this.layout = layout;
			this.isEventTimestamp = isEventDate;
			this.isUnicode = isUnicode;
			this.type = type;
			this.name = name;
		}
	}
}
