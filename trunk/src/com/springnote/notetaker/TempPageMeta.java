package com.springnote.notetaker;

import rath.toys.springnote.PageMeta;

public class TempPageMeta {
	private PageMeta pageMeta = null;

	public TempPageMeta(PageMeta pageMeta) {
		this.pageMeta  = pageMeta;
	}
	
	public int getId() {
		return pageMeta.getId();
	}
	
	public String getName() {
		return pageMeta.getName();
	}
	
	public String toString() {
		final int MAX_LENGTH = 30;
		String name = getName();
		if (name.length() > MAX_LENGTH) {
			name = name.substring(0, MAX_LENGTH) + "...";
		}
		return name + " [" + getId() + "]" ;
	}
}
