package com.stuonline.entity;

/**
 * Comment entity. @author MyEclipse Persistence Tools
 */

public class Comment implements java.io.Serializable {

	// Fields

	private Integer cid;
	private Integer nid;
	private Integer uid;
	private String content;
	private Integer toUid;
	private String cdate;

	// Constructors

	/** default constructor */
	public Comment() {
	}

	/** full constructor */
	public Comment(Integer nid, Integer uid, String content, Integer toUid,
			String cdate) {
		this.nid = nid;
		this.uid = uid;
		this.content = content;
		this.toUid = toUid;
		this.cdate = cdate;
	}

	// Property accessors

	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getNid() {
		return this.nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getToUid() {
		return this.toUid;
	}

	public void setToUid(Integer toUid) {
		this.toUid = toUid;
	}

	public String getCdate() {
		return this.cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

}