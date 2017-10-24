package jrange.myordy.entity.list;

import java.io.Serializable;
import java.util.ArrayList;

public class PaginatedResponse <T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long totalItems;
	private final ArrayList<T> items = new ArrayList<T>();

	public ArrayList<T> getItems() {
		return items;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(final Long totalItems) {
		this.totalItems = totalItems;
	}

}
