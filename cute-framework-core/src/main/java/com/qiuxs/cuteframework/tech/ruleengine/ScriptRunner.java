package com.qiuxs.cuteframework.tech.ruleengine;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptRunner {
	
	public static boolean evelBooleanValue(String script, String funcArgName, Object param) {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		try {
			script = "function func(" + funcArgName + ") { return " + script + "; }";
			System.out.println("evelScript:" + script);
			engine.eval(script);
			Invocable invocable = (Invocable) engine;
			return (boolean) invocable.invokeFunction("func", param);
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return false;
	}

}
