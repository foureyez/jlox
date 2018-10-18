package com.foureyez.lox.parse.expr;

public class Grouping implements Expr {
	public final Expr expression;

	public Grouping(Expr expression) {
		this.expression = expression;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}

}
