package com.xwarner.model.scripts.interpreter;

import java.util.HashMap;

import com.xwarner.model.scripts.functions.Function;

public class Context {

	public Context parent;

	public HashMap<String, Var> variables;
	public HashMap<String, Function> functions;

	public Context() {
		variables = new HashMap<String, Var>();
		functions = new HashMap<String, Function>();
	}

	public double getVariable(String name) {
//		System.out.println("getting: \"" + name + "\"");
		Var var = variables.get(name);
		if (var != null)
			return var.value;
		else if (parent != null)
			return parent.getVariable(name);
		else {
//			System.out.println("adding new variable " + name);
			setVariable(name, 0.0);
			return 0.0;
		}
	}

	public Function getFunction(String name) {
		Function func = functions.get(name);
		if (func != null)
			return func;
		else if (parent != null)
			return parent.getFunction(name);
		else
			return null;
	}

	public void setVariable(String name, double value) {
		Var var = variables.get(name);
		if (var != null)
			var.value = value;
		else if (parent != null)
			parent.setVariable(name, value);
		else
			variables.put(name, new Var(name, value));
	}
	
	public void resetVariables() {
		variables.clear();
	}
}
