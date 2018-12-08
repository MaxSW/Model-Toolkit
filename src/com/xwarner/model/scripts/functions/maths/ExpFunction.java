package com.xwarner.model.scripts.functions.maths;

import com.xwarner.model.scripts.functions.Function;
import com.xwarner.model.scripts.interpreter.Context;

public class ExpFunction extends Function {

	public ExpFunction() {
		super();
		this.name = "exp";
		this.arguments.add("exponent");
	}
	
	public double evaluate(Context ctx) {
		return Math.exp(ctx.getVariable("exponent"));
	}

}
