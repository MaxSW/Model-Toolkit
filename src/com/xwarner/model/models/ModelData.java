package com.xwarner.model.models;

import java.util.HashMap;

public interface ModelData {

	public void parse(String src);

	public void evaluate(HashMap<String, Variable> variables);

}
