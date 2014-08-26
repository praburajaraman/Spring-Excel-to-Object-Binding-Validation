package org.prabu.spring.excel.objectbinding.handler;

import java.io.Serializable;

public class ColumnTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5796776674559197647L;
	
	private int id;
	
	private int pos;
	
	private String name;
	
	private String templateName;
	
	private String dbColumnName;
	
	private String beanColumnName;
	
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDbColumnName() {
		return dbColumnName;
	}

	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}

	public String getBeanColumnName() {
		return beanColumnName;
	}

	public void setBeanColumnName(String beanColumnName) {
		this.beanColumnName = beanColumnName;
	}

	@Override
	public String toString() {
		return "[ pos=" + pos 	+ ", ColumnName=" + templateName + ", type=" + type + "]";
	}
	
	

}
