package com.magma.engine.tests;

public class TestFailedException extends RuntimeException {
	private static final long serialVersionUID = 7183884701801184361L;

	public TestFailedException(String message) {
		super(message);
	}
}
