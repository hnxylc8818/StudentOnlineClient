package com.stuonline.entity;

/**
 * Tab entity. @author MyEclipse Persistence Tools
 */

public class Tab implements java.io.Serializable {

	// Fields

	private Integer tid;
	private String tab;

	// Constructors

	/** default constructor */
	public Tab() {
	}

	/** full constructor */
	public Tab(String tab) {
		this.tab = tab;
	}

	// Property accessors

	public Integer getTid() {
		return this.tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public String getTab() {
		return this.tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

}