package com.foureyez.lox.parse.util;

import com.foureyez.lox.parse.expr.Binary;
import com.foureyez.lox.parse.expr.Expr;
import com.foureyez.lox.parse.expr.Grouping;
import com.foureyez.lox.parse.expr.Literal;
import com.foureyez.lox.parse.expr.Unary;

/**
 * @author arawat
 */
public class AstPrinter implements Expr.Visitor<String> {

	/**
	 * 
	 * @param expr
	 * @return This method takes a expression and returns a parenthesized string
	 *         which represents the syntax tree accurately.
	 */
	public String print(Expr expr) {
		return expr.accept(this);
	}

	@Override
	public String visitBinaryExpr(Binary expr) {
		return parenthesize(expr.operator.lexeme, expr.left, expr.right);
	}

	@Override
	public String visitUnaryExpr(Unary expr) {
		return parenthesize(expr.operator.lexeme, expr.right);
	}

	@Override
	public String visitGroupingExpr(Grouping expr) {
		return parenthesize("group", expr.expression);
	}

	@Override
	public String visitLiteralExpr(Literal expr) {
		if (expr.value == null)
			return "nil";
		return expr.value.toString();
	}

	/**
	 * 
	 * @param name
	 * @param exprs
	 * @return This method takes a name and array of expressions and returns a
	 *         parenthesized string to represent the expression.
	 * 
	 */
	private String parenthesize(String name, Expr... exprs) {
		StringBuilder builder = new StringBuilder();

		builder.append("(").append(name);
		for (Expr expr : exprs) {
			builder.append(" ");
			builder.append(expr.accept(this));
		}
		builder.append(")");

		return builder.toString();
	}
}
