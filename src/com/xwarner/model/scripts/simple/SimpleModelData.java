package com.xwarner.model.scripts.simple;

import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.model.Events;
import com.xwarner.model.models.ModelData;
import com.xwarner.model.models.Variable;

public class SimpleModelData implements ModelData {

	public ArrayList<Equation> equations;

	public void parse(String src) {
		try {
			equations = new ArrayList<Equation>();
			String[] split = src.split("\n");
			for (int i = 0; i < split.length; i++) {
				String line = split[i].trim();
				if (line.startsWith("#") || line.startsWith("//"))
					continue;
				if (line.contains("="))
					equations.add(new Equation(line));
			}
		} catch (Exception e) {
			Events.parsingError();
		}
	}

	public void evaluate(HashMap<String, Variable> variables) {
		try {
			HashMap<String, Double> vars = variableConversion(variables);
			for (Equation eqn : equations) {
				eqn.evaluate(vars);
			}
			variableReconversion(vars, variables);
			for (Equation eqn : equations) {
				for (String name : variables.keySet()) {
					Variable var = variables.get(name);
					if (var.name.equals(eqn.result))
						if (var.type == Variable.TYPE_UNKNOWN)
							var.type = Variable.TYPE_ENDO;
				}
			}
		} catch (Exception e) {
			Events.runError();
		}
	}

	// due to differing needs, the model object stores variables as an array
	// list of Variable objects. the model data class uses a hashmap of variable
	// names and variable values
	public HashMap<String, Double> variableConversion(HashMap<String, Variable> src) {
		HashMap<String, Double> out = new HashMap<String, Double>();
		for (String name : src.keySet())
			out.put(name, src.get(name).value);
		return out;
	}

	public void variableReconversion(HashMap<String, Double> src, HashMap<String, Variable> out) {
		for (String name : src.keySet()) {
			if (out.containsKey(name))
				out.get(name).value = src.get(name);
			else
				out.put(name, new Variable(name, Variable.TYPE_UNKNOWN, src.get(name)));
		}
	}

}
