package com.qiuxs.cuteframework.ruleengine;

import java.util.ArrayList;
import java.util.List;

import com.qiuxs.cuteframework.tech.ruleengine.Condition;
import com.qiuxs.cuteframework.tech.ruleengine.ConditionGroup;
import com.qiuxs.cuteframework.tech.ruleengine.RuleParser;
import com.qiuxs.cuteframework.tech.ruleengine.ScriptRunner;

public class RuleEngineTester {
	public static void main(String[] args) {

		List<ConditionGroup> condGroups = new ArrayList<>();
		ConditionGroup group = new ConditionGroup();
		group.setCondOp("&&");
		Condition cond = new Condition();
		cond.setProp("age");
		cond.setOp(">");
		cond.setVal("10");
		group.getConds().add(cond);
		cond = new Condition();
		cond.setProp("money");
		cond.setOp(">");
		cond.setVal("100");
		group.getConds().add(cond);
		condGroups.add(group);
		
		group = new ConditionGroup();
		group.setLinkOp("||");
		group.setCondOp("&&");
		cond = new Condition();
		cond.setProp("age");
		cond.setOp(">");
		cond.setVal("20");
		group.getConds().add(cond);
		cond = new Condition();
		cond.setProp("money");
		cond.setOp(">");
		cond.setVal("200");
		group.getConds().add(cond);
		condGroups.add(group);
		
		Person person = new Person();
		person.setAge(11);
		person.setMoney(120);
		
		System.out.println(ScriptRunner.evelBooleanValue(RuleParser.parseScript(condGroups, "obj"), "obj", person));
		
	}
}
