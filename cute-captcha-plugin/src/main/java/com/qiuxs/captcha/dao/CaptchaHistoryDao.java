package com.qiuxs.captcha.dao;

import com.qiuxs.captcha.entity.CaptchaHistory;
import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.tech.mybatis.dao.MyBatisRepository;

/**
 * 验证码历史记录Dao接口
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 *
 */
@MyBatisRepository
public interface CaptchaHistoryDao extends IBaseDao<Long, CaptchaHistory> {

}
