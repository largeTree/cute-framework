package com.qiuxs.cuteframework.tech.ruleengine;

import java.util.List;

public class RuleParser {

	/**
	 * 解析为脚本
	 * @author qiuxs
	 *
	 * @param condGroups
	 * @return
	 *
	 * 创建时间：2018年11月27日 下午9:34:57
	 */
	public static String parseScript(List<ConditionGroup> condGroups, String beanName) {
		StringBuilder script = new StringBuilder();
		for (int i = 0; i < condGroups.size(); i++) {
			ConditionGroup group = condGroups.get(i);
			if (i > 0) {
				script.append(" ").append(group.getLinkOp()).append(" ");
			}
			script.append("(");
			List<Condition> conds = group.getConds();
			for (int j = 0; j < conds.size(); j++) {
				if (j > 0) {
					script.append(" ").append(group.getCondOp()).append(" ");
				}
				Condition cond = conds.get(j);
				script.append(beanName).append(".").append(cond.getProp()).append(" ").append(cond.getOp()).append(" ").append(cond.getVal());
			}
			script.append(")");
		}
		return script.toString();
	}

}
