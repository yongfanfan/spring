package com.dark.common.localthread;

import com.dark.common.domain.Pager;

public class PaginationThreadLocal {
	private static ThreadLocal<Pager> paginationThreadLocal = new ThreadLocal<Pager>();

	public static void setPager(Pager pager) {
		paginationThreadLocal.set(pager);
	}

	public static Pager getPager() {
		Pager pager = paginationThreadLocal.get();
		return pager;
	}

	public static void removePager() {
		paginationThreadLocal.remove();
	}
}
