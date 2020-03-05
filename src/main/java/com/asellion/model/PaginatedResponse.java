package com.asellion.model;

import java.util.List;

public class PaginatedResponse<T> {

	private int totalPages;
	private long totalElements;
	private int size;
	private boolean first;
	private boolean last;
	private List<T> items;

	public PaginatedResponse(int totalPages, long totalElements, int size, boolean first, boolean last,
			List<T> content) {
		this.totalPages = totalPages;
		this.totalElements = totalElements;
		this.size = size;
		this.first = first;
		this.last = last;
		this.items = content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getSize() {
		return size;
	}

	public List<T> getItems() {
		return items;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public boolean isFirst() {
		return first;
	}

	public boolean isLast() {
		return last;
	}

}
