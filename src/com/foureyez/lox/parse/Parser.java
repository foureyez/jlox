package com.foureyez.lox.parse;

import java.util.List;

import com.foureyez.lox.common.Token;
import com.foureyez.lox.common.TokenType;
import com.foureyez.lox.parse.expr.Binary;
import com.foureyez.lox.parse.expr.Expr;
import com.foureyez.lox.parse.expr.Grouping;
import com.foureyez.lox.parse.expr.Literal;
import com.foureyez.lox.parse.expr.Unary;

public class Parser {
	private final List<Token> tokens;
	private int current = 0;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	private Expr expression() {
		return equality();
	}

	private Expr equality() {
		Expr expr = comparison();

		while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
			Token operator = previous();
			Expr right = comparison();
			expr = new Binary(expr, operator, right);
		}

		return expr;
	}

	private Expr comparison() {
		Expr expr = addition();

		while (match(TokenType.GREATER_EQUAL, TokenType.GREATER, TokenType.LESS,
				TokenType.LESS_EQUAL)) {
			Token operator = previous();
			Expr right = addition();
			expr = new Binary(expr, operator, right);
		}

		return expr;
	}

	private Expr addition() {
		Expr expr = multiplication();

		while (match(TokenType.MINUS, TokenType.PLUS)) {
			Token operator = previous();
			Expr right = multiplication();
			expr = new Binary(expr, operator, right);
		}

		return expr;
	}

	private Expr multiplication() {
		Expr expr = unary();

		while (match(TokenType.SLASH, TokenType.STAR)) {
			Token operator = previous();
			Expr right = unary();
			expr = new Binary(expr, operator, right);
		}

		return expr;
	}

	private Expr unary() {
		if (match(TokenType.BANG, TokenType.MINUS)) {
			Token operator = previous();
			Expr right = unary();
			return new Unary(operator, right);
		}

		return primary();
	}

	private Expr primary() {
		if (match(TokenType.FALSE))
			return new Literal(false);
		if (match(TokenType.TRUE))
			return new Literal(true);
		if (match(TokenType.NIL))
			return new Literal(null);

		if (match(TokenType.NUMBER, TokenType.STRING)) {
			return new Literal(previous().literal);
		}

		if (match(TokenType.LEFT_PAREN)) {
			Expr expr = expression();
			consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
			return new Grouping(expr);
		}

		return null;
	}

	private Token consume(TokenType type, String message) {
		if (check(type))
			return advance();
		return null;

	}

	// Utility Methods

	private boolean match(TokenType... types) {
		for (TokenType type : types) {
			if (check(type)) {
				advance();
				return true;
			}
		}

		return false;
	}

	private Token previous() {
		return tokens.get(current - 1);
	}

	private Token advance() {
		if (!isAtEnd())
			current++;
		return previous();
	}

	private boolean check(TokenType type) {
		if (isAtEnd())
			return false;
		return tokens.get(current).type == type;
	}

	private boolean isAtEnd() {
		return peek().type == TokenType.EOF;
	}

	private Token peek() {
		return tokens.get(current);
	}

	private Exception error(Token peek, String message) {
		return null;
	}
}
