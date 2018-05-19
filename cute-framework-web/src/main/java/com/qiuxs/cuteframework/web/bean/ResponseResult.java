package com.qiuxs.cuteframework.web.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseResult {

	public static final String RES_KEY_CODE = "code";
	public static final String RES_KEY_MSG = "msg";
	public static final String RES_KEY_ROWS = "rows";
	public static final String RES_KEY_TOTAL = "total";
	public static final String RES_KEY_SUMROW = "sumrow";

	public static final int CODE_SUCCESS = 0;
	public static final String MSG_SUCCESS = "请求成功";
	public static final int CODE_FAILED = -10;
	public static final String MSG_FAILED = "请求失败";

	private int code;
	private String msg;
	private Object data;

	public ResponseResult() {
	}

	public ResponseResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ResponseResult(List<?> rows) {
		this(rows, rows == null ? 0 : rows.size());
	}

	public ResponseResult(List<?> rows, int total) {
		this(rows, total, null);
	}

	public ResponseResult(List<?> rows, int total, Map<String, ? extends Number> sumrow) {
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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

	public static ResponseResult makeSuccess() {
		return new ResponseResult(CODE_SUCCESS, MSG_SUCCESS);
	}

	public static ResponseResult makeFailed() {
		return new ResponseResult(CODE_FAILED, MSG_FAILED);
	}

}
