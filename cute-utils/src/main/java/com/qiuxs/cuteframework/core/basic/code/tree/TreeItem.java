package com.qiuxs.cuteframework.core.basic.code.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 树形结构节点
 * @author qiuxs
 *
 */
public class TreeItem implements Serializable {

	private static final long serialVersionUID = -3261374887205659262L;

	/** id */
	private Object id;
	/** 名称 */
	private String name;
	/** 扩展属性 */
	private JSONObject attr;
	/** 是否还有子节点 */
	private boolean hasChildren = true;
	/** 子节点 */
	private List<TreeItem> children = new ArrayList<>();

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getAttr() {
		return attr;
	}

	public void setAttr(JSONObject attr) {
		this.attr = attr;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public List<TreeItem> getChildren() {
		return children;
	}

	public void setChildren(List<TreeItem> children) {
		this.children = children;
	}

}
