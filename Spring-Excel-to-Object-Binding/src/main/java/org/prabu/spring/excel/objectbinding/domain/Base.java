package org.prabu.spring.excel.objectbinding.domain;

import java.io.Serializable;

import org.springframework.validation.Errors;

public class Base implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1664479729754546180L;
	
	Errors errors;
	
	long row;

	public long getRow() {
		return row;
	}

	public void setRow(long row) {
		this.row = row;
	}

	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}

}
