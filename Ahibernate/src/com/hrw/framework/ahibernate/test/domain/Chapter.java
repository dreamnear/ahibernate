package com.hrw.framework.ahibernate.test.domain;

public class Chapter extends BaseDomain {

	private Long id;
	private String chapterName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

}
