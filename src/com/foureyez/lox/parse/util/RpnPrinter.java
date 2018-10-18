package com.foureyez.lox.parse.util;

import com.foureyez.lox.common.Token;
import com.foureyez.lox.common.TokenType;
import com.foureyez.lox.parse.expr.Binary;
import com.foureyez.lox.parse.expr.Expr;
import com.foureyez.lox.parse.expr.Grouping;
import com.foureyez.lox.parse.expr.Literal;
import com.foureyez.lox.parse.expr.Unary;

/**
 * 
 * @author arawat RPN(Reverse Polish Notation), also known as PostOrder Notation
 *         is where the arithmetic operator is placed after its operand in the
 *         string representing the expression.
 * 
 *         This class is used for generating a RPN string which represents the
 *         given expression tree.
 */
public class RpnPrinter implements Expr.Visitor<String> {

	public String print(Expr expr) {
		return expr.accept(this);
	}

	@Override
	public String visitBinaryExpr(Binary expr) {
		return expr.left.accept(this) + " " + expr.right.accept(this) + " "
				+ expr.operator.lexeme;
	}

	@Override
	public String visitUnaryExpr(Unary expr) {
		return expr.operator.lexeme + expr.right.accept(this);
	}

	@Override
	public String visitGroupingExpr(Grouping expr) {
		return "(" + expr.expression.accept(this) + ")";
	}

	@Override
	public String visitLiteralExpr(Literal expr) {
		return String.valueOf(expr.value);
	}

	// For Testing
	public static void main(String[] args) {
		Expr expression = new Binary(
				new Unary(new Token(TokenType.MINUS, "-", null, 1),
						new Literal(123)),
				new Token(TokenType.STAR, "*", null, 1),
				new Grouping(new Literal(45.67)));

		System.out.println(new RpnPrinter().print(expression));
	}
}
