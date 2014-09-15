package org.prabu.spring.excel.objectbinding.handler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FileTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3810715254328582921L;

	private long templateId;
	
	private String templateName;
	
	private String dbSchemaName;
	
	private String dbTableName;
	
	/**
	 * 
	 */
	private String validator;
	
	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	private List<ColumnTemplate> columnTemplates;
	
	private Map<Integer, ColumnTemplate> columnTemplatesMap;
	
	private Map<String, Integer> columnTemplatesBeanColumnName;

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public Map<Integer, ColumnTemplate> getColumnTemplatesMap() {
		initColumnTemplatesMap();
		return columnTemplatesMap;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDbSchemaName() {
		return dbSchemaName;
	}

	public void setDbSchemaName(String dbSchemaName) {
		this.dbSchemaName = dbSchemaName;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public List<ColumnTemplate> getColumnTemplates() {
		return columnTemplates;
	}

	public void setColumnTemplates(List<ColumnTemplate> columnTemplates) {
		this.columnTemplates = columnTemplates;
	}

	@Override
	public String toString() {
		return "FileTemplate [templateId=" + templateId + ", templateName="
				+ templateName + ", dbSchemaName=" + dbSchemaName
				+ ", dbTableName=" + dbTableName + ", columnTemplates="
				+ columnTemplates + "]";
	}
	
	public ColumnTemplate getColumnTemplateByPos(int pos){
		initColumnTemplatesMap();
		return columnTemplatesMap.get(pos);
	}
	public ColumnTemplate getColumnTemplateByBeanColumnName(String columnName){
		initColumnTemplatesMap();
		return columnTemplatesMap.get(columnTemplatesBeanColumnName.get(columnName));
	}
	
	private void initColumnTemplatesMap(){
		if(columnTemplatesMap==null && columnTemplates.size()>0){
			columnTemplatesMap = new TreeMap<Integer, ColumnTemplate>();
			columnTemplatesBeanColumnName = new HashMap<String, Integer>();
			for(ColumnTemplate columnTemplate : columnTemplates){
				columnTemplatesMap.put(columnTemplate.getPos(), columnTemplate);
				columnTemplatesBeanColumnName.put(columnTemplate.getBeanColumnName(),
						columnTemplate.getPos());
			}
		}
	}
	

}
