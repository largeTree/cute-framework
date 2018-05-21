package com.qiuxs.cuteframework.web.controller;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.persistent.entity.IEntity;
import com.qiuxs.cuteframework.core.persistent.service.AbstractPropertyService;

public abstract class AbstractPropertyController<PK extends Serializable, T extends IEntity<PK>, S extends AbstractPropertyService<PK, T>> extends BaseController {

	protected abstract S getService();

}