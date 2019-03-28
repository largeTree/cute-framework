package com.qiuxs.captcha.dao;

import org.springframework.stereotype.Repository;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.captcha.entity.CaptchaHistory;

/**
 * 验证码历史记录Dao接口
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 *
 */
@Repository
public interface CaptchaHistoryDao extends IBaseDao<Long, CaptchaHistory> {

}
