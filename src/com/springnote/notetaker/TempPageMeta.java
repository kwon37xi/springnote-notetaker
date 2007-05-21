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
		return getName() + " [" + getId() + "]" ;
	}
}
