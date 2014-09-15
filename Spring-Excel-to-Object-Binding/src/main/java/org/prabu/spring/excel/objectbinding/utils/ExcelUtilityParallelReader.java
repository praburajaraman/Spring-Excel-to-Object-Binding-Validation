package org.prabu.spring.excel.objectbinding.utils;

import java.util.List;
import java.util.concurrent.Callable;

import org.prabu.spring.excel.objectbinding.domain.Base;
import org.prabu.spring.excel.objectbinding.domain.ExcelFile;
import org.prabu.spring.excel.objectbinding.domain.Limit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtilityParallelReader implements  Callable<List<? extends Base>> {

	ExcelFile excelFile;
	
	Limit limit;
	
	boolean isHeaderThread;
	
	private final static Logger log = LoggerFactory
			.getLogger(ExcelUtilityParallelReader.class);
	

	public ExcelUtilityParallelReader(ExcelFile excelFile, Limit limit,boolean isHeaderThread) {
		super();
		this.excelFile = excelFile;
		this.limit = limit;
		this.isHeaderThread = isHeaderThread;
	}

	@Override
	public List<? extends Base> call() throws Exception {
		// TODO Auto-generated method stub
		log.info("Running Thread for set starting : "+ limit.getStartRow());
		return ExcelUtility.readXlFile(excelFile.getSheet(), excelFile.getFileTemplate(), excelFile.getClazz(), 
				isHeaderThread, limit.getStartRow(), limit.getEndRow(),excelFile.getValidator());

	}
	
	

}
