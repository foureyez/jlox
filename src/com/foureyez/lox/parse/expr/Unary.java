package com.foureyez.lox.parse.expr;

import com.foureyez.lox.common.Token;

public class Unary implements Expr {

	public final Token operator;
	public final Expr right;

	public Unary(Token operator, Expr right) {
		this.operator = operator;
		this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitUnaryExpr(this);
	}

}
