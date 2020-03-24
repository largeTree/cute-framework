package com.qiuxs.cuteframework.tech.microsvc.log;

public class ApiLogConstants {
	
	/** apiLogProp */
	public static final String TL_APILOG = "apilog_apiLogProp";
	/** apilog_globalId */
	public static final String TL_APILOG_GLOBAL_ID = "apilog_globalId";
	/** apilog_requestId */
	public static final String TL_APILOG_REQUEST_ID = "apilog_requestId";

    /** 日志属性作为附加数据传送到提供者端时，使用的Key名称 */
	public static final String ATTACH_KEY_REQ_PROP = "apilog.reqProp";

    /** 日志属性作为附加数据返回给消费者端时，使用的Key名称 */
    public static final String ATTACH_KEY_ANSWER_PROP = "apilog.answerProp";

    /** 客户端请求 */
    public static final String TYPE_REQUEST_CLI = "cli_r";
    /** 客户端响应 */
    public static final String TYPE_ANSWER_CLI = "cli_a";
    /** mq请求 */
	public static final String TYPE_REQUEST_MQ = "mq_r";
	/** mq响应 */
	public static final String TYPE_ANSWER_MQ = "mq_a";
    
    

}
