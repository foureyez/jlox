package com.foureyez.lox.parse.expr;

public interface Expr {

	<R> R accept(Visitor<R> visitor);

	interface Visitor<R> {
		R visitBinaryExpr(Binary expr);
		R visitUnaryExpr(Unary expr);
		R visitGroupingExpr(Grouping expr);
		R visitLiteralExpr(Literal expr);
	}
}
