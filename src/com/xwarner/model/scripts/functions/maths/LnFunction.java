package com.xwarner.model.scripts.functions.maths;

import com.xwarner.model.scripts.functions.Function;
import com.xwarner.model.scripts.interpreter.Context;

public class LnFunction extends Function {

	public LnFunction() {
		super();
		this.name = "ln";
		this.arguments.add("var");
	}
	
	public double evaluate(Context ctx) {
		return Math.log(ctx.getVariable("var"));
	}

}
