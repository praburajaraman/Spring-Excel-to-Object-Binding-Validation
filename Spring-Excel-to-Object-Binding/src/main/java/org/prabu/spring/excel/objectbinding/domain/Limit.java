package org.prabu.spring.excel.objectbinding.domain;

import java.io.Serializable;

public class Limit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -782664235277519007L;
	
	int startRow;
	
	int endRow;

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	@Override
	public String toString() {
		return "Limit [startRow=" + startRow + ", endRow=" + endRow + "]";
	}
	
	
	
}
