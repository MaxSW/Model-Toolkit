package com.xwarner.model.models;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class ModelIO {

	public static Model open(File file) throws Exception {
		Model model = new Model("");
		JsonObject obj = Json.parse(new FileReader(file)).asObject();
		model.setName(obj.get("name").asString());
		model.setModel(obj.get("model").asString());
		model.setType(obj.get("type").asString());
		JsonArray vars = obj.get("variables").asArray();
		for (JsonValue value : vars) {
			JsonObject o = value.asObject();
			Variable var = new Variable(o.get("name").asString(), o.get("type").asInt(), 0.0);
			var.defaultValue = o.get("default").asString();
			var.value = Double.parseDouble(var.defaultValue);
			model.addVariable(var);
		}
		return model;
	}

	public static void save(File file, Model model) throws Exception {
		JsonObject obj = Json.object();
		obj.add("name", model.getName());
		obj.add("type", model.getTypeStr());
		JsonArray vars = Json.array().asArray();
		for (String name : model.getVariables().keySet()) {
			Variable var = model.getVariable(name);
			JsonObject o = Json.object();
			o.add("name", var.name);
			o.add("type", var.type);
			o.add("default", var.defaultValue);
			vars.add(o);
		}
		obj.add("variables", vars);
		obj.add("model", model.getModel());
		FileWriter w = new FileWriter(file);
		obj.writeTo(w);
		w.close();
	}

}
