package com.mrbt.orbit.common.api;

public record PaginationParams(int page, int size) {

	private static final int MAX_SIZE = 100;

	public static PaginationParams of(int page, int size) {
		return new PaginationParams(page, Math.min(size, MAX_SIZE));
	}

}
