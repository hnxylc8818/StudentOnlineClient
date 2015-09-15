package com.stuonline.entity;

/**
 * News entity. @author MyEclipse Persistence Tools
 */

public class News implements java.io.Serializable {

	// Fields

	private Integer nid;
	private String title;
	private String newsUrl;
	private String content;
	private String resume;
	private String type;
	private Integer tabid;
	private String from;
	private String ndate;

	// Constructors

	/** default constructor */
	public News() {
	}

	/** full constructor */
	public News(String title, String newsUrl, String content, String resume,
			String type, Integer tabid, String from, String ndate) {
		this.title = title;
		this.newsUrl = newsUrl;
		this.content = content;
		this.resume = resume;
		this.type = type;
		this.tabid = tabid;
		this.from = from;
		this.ndate = ndate;
	}

	// Property accessors

	public Integer getNid() {
		return this.nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsUrl() {
		return this.newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResume() {
		return this.resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTabid() {
		return this.tabid;
	}

	public void setTabid(Integer tabid) {
		this.tabid = tabid;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getNdate() {
		return this.ndate;
	}

	public void setNdate(String ndate) {
		this.ndate = ndate;
	}

}