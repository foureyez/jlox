package com.foureyez.lox.parse.expr;

public class Literal implements Expr {

	public final Object value;

	public Literal(Object value) {
		this.value = value;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitLiteralExpr(this);
	}

}
