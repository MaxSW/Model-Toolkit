package com.xwarner.model.scripts.functions;

import com.xwarner.model.scripts.functions.maths.ExpFunction;
import com.xwarner.model.scripts.functions.maths.LnFunction;
import com.xwarner.model.scripts.interpreter.Context;

public class InbuiltFunctions {

	static Function[] functions = new Function[] { new ExpFunction(), new LnFunction() };

	public static void addFunctions(Context context) {
		for (Function function : functions) {
			context.functions.put(function.name, function);
		}
	}

}
