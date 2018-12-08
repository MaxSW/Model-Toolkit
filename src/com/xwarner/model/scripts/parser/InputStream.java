package com.xwarner.model.scripts.parser;

public class InputStream {

	private String source;
	private int pos = 0;
	// line and col for tracking error locations
	private int line = 1;
	private int col = 0;

	public InputStream(String source) {
		this.source = source;
	}

	public char next() {
		if (pos >= source.length())
			return ' ';
		char c = source.charAt(pos);
		pos++;
		if (c == '\n') {
			line++;
			col = 0;
		} else {
			col++;
		}
		return c;
	}

	public char peek() {
		return source.charAt(pos);
	}

	public boolean done() {
		return pos >= source.length();
	}

	public void error(String message) {
		throw new Error(message + " (" + line + ":" + col + ")");
	}

}
