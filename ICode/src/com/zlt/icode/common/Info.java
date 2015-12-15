package com.zlt.icode.common;

import java.io.Serializable;
import java.util.Date;

import org.litepal.crud.DataSupport;

public class Info extends DataSupport implements Serializable {

	private String title;
	private String account;
	private String password;
	private String extras;

	private Date createTime;
	private Date lastTime;
	private int queryTimes;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAccount() {
		return account == null ? "" : account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExtras() {
		return extras == null ? "" : extras;
	}

	public void setExtras(String extras) {
		this.extras = extras;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public int getQueryTimes() {
		return queryTimes;
	}

	public void setQueryTimes(int queryTimes) {
		this.queryTimes = queryTimes;
	}

}
