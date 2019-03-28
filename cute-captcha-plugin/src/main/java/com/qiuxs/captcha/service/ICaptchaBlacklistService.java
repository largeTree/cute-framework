package com.qiuxs.captcha.service;

import com.qiuxs.captcha.dao.CaptchaBlacklistDao;
import com.qiuxs.captcha.entity.CaptchaBlacklist;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 服务接口
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 */
public interface ICaptchaBlacklistService extends IDataPropertyService<Long, CaptchaBlacklist, CaptchaBlacklistDao> {

}
