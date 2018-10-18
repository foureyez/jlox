package com.foureyez.lox.parse.expr;

import com.foureyez.lox.common.Token;

public class Binary implements Expr {
	public final Expr left;
	public final Token operator;
	public final Expr right;

	public Binary(Expr left, Token operator, Expr right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitBinaryExpr(this);
	}
}
