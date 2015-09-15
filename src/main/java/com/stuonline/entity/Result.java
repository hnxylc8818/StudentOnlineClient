package com.stuonline.entity;

import java.io.Serializable;

public class Result<T> implements Serializable {

	public static final int STATE_SUC = 1;
	public static final int STATE_FAIL = 0;

	public String desc;		// 描述
	public int state;		// 状态码
	public T data;			// 数据
}
