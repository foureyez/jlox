package com.foureyez.lox.lex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.foureyez.lox.Lox;
import com.foureyez.lox.common.Token;
import com.foureyez.lox.common.TokenType;

public class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;
	private static final Map<String, TokenType> keywords;

	static {
		keywords = new HashMap<>();
		keywords.put("and", TokenType.AND);
		keywords.put("class", TokenType.CLASS);
		keywords.put("else", TokenType.ELSE);
		keywords.put("false", TokenType.FALSE);
		keywords.put("for", TokenType.FOR);
		keywords.put("fun", TokenType.FUN);
		keywords.put("if", TokenType.IF);
		keywords.put("nil", TokenType.NIL);
		keywords.put("or", TokenType.OR);
		keywords.put("print", TokenType.PRINT);
		keywords.put("return", TokenType.RETURN);
		keywords.put("super", TokenType.SUPER);
		keywords.put("this", TokenType.THIS);
		keywords.put("true", TokenType.TRUE);
		keywords.put("var", TokenType.VAR);
		keywords.put("while", TokenType.WHILE);
	}

	public Scanner(String source) {
		this.source = source;
	}

	/**
	 * Scans the source code and returns a list of tokens.
	 * 
	 * @return A list of token.
	 */
	public List<Token> scanTokens() {
		while (!isAtEnd()) {
			// We are at the beginning of the next lexeme.
			start = current;
			scanToken();
		}

		// Add EOF token when encountered the end of source code.
		tokens.add(new Token(TokenType.EOF, "", null, line));
		return tokens;
	}

	private void scanToken() {
		char c = advance();
		switch (c) {
			// @formatter:off
			case '(' : addToken(TokenType.LEFT_PAREN); break;
			case ')' : addToken(TokenType.RIGHT_PAREN); break;
			case '{' : addToken(TokenType.LEFT_BRACE); break;
			case '}' : addToken(TokenType.RIGHT_BRACE); break;
			case ',' : addToken(TokenType.COMMA); break;
			case '.' : addToken(TokenType.DOT); break;
			case '-' : addToken(TokenType.MINUS); break;
			case '+' : addToken(TokenType.PLUS); break;
			case ';' : addToken(TokenType.SEMICOLON); break;
			case '*' : addToken(TokenType.STAR); break;
			case '!' : addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG); break;
			case '=' : addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL); break;
			case '<' : addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS); break;
			case '>' : addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER); break;
			// @formatter:on
			case '/' :
				if (match('/')) {
					// A comment goes until the end of the line.
					while (peek() != '\n' && !isAtEnd())
						advance();
				} else {
					addToken(TokenType.SLASH);
				}
				break;
			// Ignore whitespaces
			case ' ' :
			case '\r' :
			case '\t' :
				break;

			case '\n' :
				line++;
				break;

			// Reading a string literal
			case '"' :
				string();
				break;
			default :
				if (isDigit(c)) {
					number();
				} else if (isAlpha(c)) {
					identifier();
				} else {
					Lox.error(line, "Unexpected character.");
				}
				break;
		}

	}

	/**
	 * Checks if the current index is a the end of the source code.
	 */
	private boolean isAtEnd() {
		return current >= source.length();
	}

	/**
	 * Returns the character at current position and increments current by 1.
	 */
	private char advance() {
		current++;
		return source.charAt(current - 1);
	}

	/**
	 * Returns the character at the current position.
	 */
	private char peek() {
		if (isAtEnd()) {
			return '\n';
		}

		return source.charAt(current);
	}

	/**
	 * Returns the character at the next position.
	 */
	private char peekNext() {
		if (current + 1 >= source.length()) {
			return '\0';
		}
		return source.charAt(current + 1);
	}

	/**
	 * 
	 * @param expected
	 * @return A boolean true if the @expected matches the current value in
	 *         source code, false if not.
	 */
	private boolean match(char expected) {
		if (isAtEnd() || source.charAt(current) != expected) {
			return false;
		}

		current++;
		return true;
	}

	/**
	 * Returns of the current charatcter is a digit or not.
	 */
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}
	/**
	 * Reads a string literal and stores its value as a token.
	 */
	private void string() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') {
				line++;
			}
			advance();
		}

		if (isAtEnd()) {
			Lox.error(line, "Unterminated string.");
			return;
		}

		advance();

		String value = source.substring(start + 1, current - 1);
		addToken(TokenType.STRING, value);
	}

	/**
	 * Reads a number literal and stores its value in a token.
	 */
	private void number() {
		while (isDigit(peek()))
			advance();

		// Handle the fractional Part
		if (peek() == '.' && isDigit(peekNext())) {
			advance();

			while (isDigit(peek()))
				advance();
		}

		addToken(TokenType.NUMBER,
				Double.parseDouble(source.substring(start, current)));

	}

	/**
	 * Reads an identifier and stores it as a token. Also checks if the
	 * identifier read is a reserved keyword. If it is then store it as that
	 * token type.
	 */
	private void identifier() {
		while (isAlphaNumeric(peek()))
			advance();

		// Check if the identifier scanned is a reserved keyword.
		String text = source.substring(start, current);
		TokenType type = keywords.get(text);
		if (type == null)
			type = TokenType.IDENTIFIER;

		addToken(type);
	}

	/**
	 * Adds a token.
	 */
	private void addToken(TokenType type) {
		addToken(type, null);
	}

	/**
	 * Adds a token with literal value.
	 */
	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}
}
