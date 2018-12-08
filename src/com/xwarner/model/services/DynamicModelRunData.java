package com.xwarner.model.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.model.models.Variable;

public class DynamicModelRunData {

	public HashMap<String, Variable> variables;
	public ArrayList<HashMap<String, Double>> history;
	public int tickCount;
	public boolean steadyState;

}
