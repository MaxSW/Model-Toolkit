package com.xwarner.model.scripts.parser;

/**
 * Converts an input stream to a token stream
 * 
 * Token types: punctuation, number, keyword, variable, operator
 * 
 * @author max
 *
 */

public class TokenStream {

	private InputStream input;
	private Token current; // cache

	public TokenStream(InputStream input) {
		this.input = input;
	}

	public Token peek() {
		if (current == null)
			current = readNext();
		return current;
	}

	public Token next() {
		Token token = current;
		if (current == null)
			token = readNext();
		else
			current = null;
		return token;
	}

	public boolean done() {
		return peek() == null;
	}

	private Token readNext() {
		// skip any whitespace
		readWhile(isWhitespace);
		// finish if empty
		if (input.done())
			return null;
		char c = input.peek();
		if (c == '#') {
			skipComment();
			return readNext();
		} else if (isNewline.match(c)) {
			input.next();
			return new Token(Token.NEWLINE, "");
		} else if (isAssignment.match(c)) {
			input.next();
			return new Token(Token.ASSIGNMENT, "");
		} else if (isDigit.match(c)) {
			return new Token(Token.NUMBER, readNumber());
		} else if (isIdStart.match(c)) {
			return readId();
		} else if (isPunctuation.match(c)) {
			return new Token(Token.PUNCTUATION, Character.toString(input.next()));
		} else if (isOperator.match(c)) {
			return new Token(Token.OPERATOR, readWhile(isOperator));
		}
		input.error("can't handle character " + c);
		return null;
	}

	private String readWhile(TokenMatcher matcher) {
		String str = "";
		while (!input.done() && matcher.match(input.peek())) {
			str += input.next();
		}
		return str;
	}

	private String readNumber() {
		boolean decimal = false;
		String str = "";
		while (!input.done()) {
			char c = input.peek();
			if (c == '.') {
				if (decimal)
					return str;
				decimal = true;
				str += input.next();
			} else {
				if (isDigit.match(c))
					str += input.next();
				else
					return str;
			}
		}
		return str;
	}

	private Token readId() {
		String id = readWhile(isId);
		int type = isKeyword(id) ? Token.KEYWORD : Token.VARIABLE;
		return new Token(type, id);
	}

	private void skipComment() {
		readWhile(isntNewline);
		input.next();
	}

	/*
	 * Subclasses
	 */

	public class Token {
		public static final int PUNCTUATION = 1, NUMBER = 2, KEYWORD = 3, VARIABLE = 4, OPERATOR = 5, NEWLINE = 6,
				ASSIGNMENT = 7;

		public int type;
		public String value;

		public Token(int type, String value) {
			this.type = type;
			this.value = value;
		}
	}

	private interface TokenMatcher {
		public boolean match(char c);
	}

	/*
	 * Matchers
	 */

	private boolean isKeyword(String str) {
		// spaces ensure not part of the word
		return " function return ".contains(" " + str + " ");
	}

	private TokenMatcher isPunctuation = new TokenMatcher() {
		public boolean match(char c) {
			return "{}(),".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isNewline = new TokenMatcher() {
		public boolean match(char c) {
			return "\n".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isDigit = new TokenMatcher() {
		public boolean match(char c) {
			return Character.isDigit(c);
		}
	};

	private TokenMatcher isId = new TokenMatcher() {
		public boolean match(char c) {
			int type = Character.getType(c);
			return (type == Character.LOWERCASE_LETTER) || (type == Character.UPPERCASE_LETTER)
					|| (type == Character.DECIMAL_DIGIT_NUMBER) || c == '_';
		}
	};

	// variables cannot start with a number
	private TokenMatcher isIdStart = new TokenMatcher() {
		public boolean match(char c) {
			return Character.getType(c) == Character.LOWERCASE_LETTER;
		}
	};

	private TokenMatcher isOperator = new TokenMatcher() {
		public boolean match(char c) {
			return "+-*/^".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isAssignment = new TokenMatcher() {
		public boolean match(char c) {
			return "=".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isWhitespace = new TokenMatcher() {
		public boolean match(char c) {
			return " \t".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isntNewline = new TokenMatcher() {
		public boolean match(char c) {
			return c != '\n';
		}
	};

}
