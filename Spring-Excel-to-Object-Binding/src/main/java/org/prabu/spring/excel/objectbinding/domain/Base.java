package org.prabu.spring.excel.objectbinding.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonIgnore;
@XmlRootElement
@XmlSeeAlso(User.class)
public class Base implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1664479729754546180L;
	
	public Base() {
		// TODO Auto-generated constructor stub
	}
	
	@JsonIgnore
	@XmlTransient
	Errors errors;
	
	long row;

	public long getRow() {
		return row;
	}

	public void setRow(long row) {
		this.row = row;
	}

	@XmlTransient
	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}

}
