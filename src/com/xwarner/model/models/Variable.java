package com.xwarner.model.models;

public class Variable {

	public static final int TYPE_UNKNOWN = 0, TYPE_EXO = 1, TYPE_ENDO = 2, TYPE_PARAM = 3, TYPE_OTHER = 4;

	public String name;
	public int type;
	public double value;
	public String defaultValue = "0";
	public boolean custom;

	public Variable(String name, int type, double value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public Variable clone() {
		Variable b = new Variable(name, type, value);
		b.defaultValue = defaultValue;
		b.custom = custom;
		return b;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		switch (type) {
		case TYPE_UNKNOWN:
			return "unknown";
		case TYPE_EXO:
			return "exogenous";
		case TYPE_ENDO:
			return "endogenous";
		case TYPE_PARAM:
			return "parameter";
		case TYPE_OTHER:
			return "other";
		default:
			return "unknown";
		}
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getValue() {
		return Double.toString(value);
	}

	public void setType(String str) {
		if (str.equals("exogenous"))
			type = TYPE_EXO;
		else if (str.equals("endogenous"))
			type = TYPE_ENDO;
		else if (str.equals("parameter"))
			type = TYPE_PARAM;
		else if (str.equals("other"))
			type = TYPE_OTHER;
		else
			type = TYPE_UNKNOWN;
	}

}
