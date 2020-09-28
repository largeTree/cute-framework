package com.qiuxs.cuteframework.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;

public class ActionResult implements Serializable {
	
	private static final long serialVersionUID = -6706471004713521476L;
	
	public static final String RES_KEY_CODE = "code";
	public static final String RES_KEY_MSG = "msg";
	public static final String RES_KEY_ROWS = "rows";
	public static final String RES_KEY_TOTAL = "total";
	public static final String RES_KEY_SUMROW = "sumrow";
	public static final String RES_KEY_VAL = "val";

	public static final int CODE_SUCCESS = 0;
	public static final String MSG_SUCCESS = "请求成功";
	public static final int CODE_FAILED = -10;
	public static final String MSG_FAILED = "请求失败";

	public static final ActionResult SUCCESS_INSTANCE = new ActionResult(CODE_SUCCESS, MSG_SUCCESS);
	public static final ActionResult FAILED_INSTANCE = new ActionResult(CODE_FAILED, MSG_FAILED);

	private int code;
	private long globalId;
	private String msg;
	private Object data;

	public ActionResult() {
	}

	public ActionResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ActionResult(Integer val) {
		this(MapUtils.genMap(RES_KEY_VAL, val));
	}

	public ActionResult(Long val) {
		this(MapUtils.genMap(RES_KEY_VAL, val));
	}

	public ActionResult(String val) {
		this(MapUtils.genMap(RES_KEY_VAL, val));
	}

	public ActionResult(Boolean val) {
		this(MapUtils.genMap(RES_KEY_VAL, val));
	}
	
	public ActionResult(Object obj) {
		this(CODE_SUCCESS, MSG_SUCCESS, obj);
	}

	public ActionResult(List<?> rows) {
		this(rows, rows == null ? 0 : rows.size());
	}

	public ActionResult(List<?> rows, int total) {
		this(rows, total, null);
	}

	public ActionResult(List<?> rows, Integer total, Map<String, ? extends Number> sumrow) {
		Map<String, Object> data = new HashMap<>();
		if (rows == null) {
			rows = new ArrayList<>();
		}
		data.put(RES_KEY_ROWS, rows);
		data.put(RES_KEY_TOTAL, total);
		data.put(RES_KEY_SUMROW, sumrow);
		this.data = data;
		this.code = CODE_SUCCESS;
		this.msg = MSG_SUCCESS;
	}

	public ActionResult(Map<String, Object> mapData) {
		this(CODE_SUCCESS, MSG_SUCCESS, mapData);
	}

	public ActionResult(int code, String msg, Object data) {
		this.data = data;
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getGlobalId() {
		return globalId;
	}

	public void setGlobalId(long globalId) {
		this.globalId = globalId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public static ActionResult of(Object data) {
		return new ActionResult(data);
	}

	public static ActionResult of(Integer total, List<?> menuList) {
		return new ActionResult(menuList, total);
	}

	public static ActionResult success() {
		return new ActionResult(CODE_SUCCESS, MSG_SUCCESS);
	}

}
