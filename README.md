Spring-Excel-to-Object-Binding
==============================

Spring Excel Upload File to Object Collection Binding

This is something all programmers who wants to implement a solution of

1. Uploading a file(Excel in here)

2. Parsing file using Apache POI

3. Binding excel rows in to colelction of POJO

4. (Re)Using validator for POJO in dynamic binding

5. Have all this process run in Parallel for Chunks/blocks of data from Excel for high performance.  

First thing this solution tried to do is have generic as much as possible and KISS. with just addition of your target
POJO and file configuration this solution will be up and running - 

You have the List of objects with its data type errors and validation errors. 

Also with Rest service features this project can server well as EXCEL to JSON or EXCEL to XML or whatever spring restservices supports in its @ResponseBody producing media types. 

And other things I try to do here is have the File processed in parallel, kind of split the sheet in chunks and have concurrent threads process it for faster object binding.

lets get Started:
This Solution is built on Spring Framework. base of this is Spring getting started tutorial on file upload - http://spring.io/guides/gs/uploading-files/ read thru this on what basics of this.

Design
I need to know what is my target object to be bindined to. remember this is POJO. no deep nesting is covered at this time. if someone interested to contribute?

User - id, name, dob. like to cover diff data types

public class User extends Base{

	private Long id;
	
	private String name;
	
	private Date dob;
	
	}

I need to configure Template file which says how is my uploaded file is going to look like. lets use JSON file to configure file template. use Spring's Jackson API object mapper to load it in context

{
  "templateId" :  1,
  "templateName" :  "USER",
  "dbSchemaName" :  "",
  "dbTableName" :  "",
  "columnTemplates" :  [
                        {
                            "id": 1,
                            "pos": 0,
                            "templateName": "ID",
                            "dbColumnName": "ID",
                            "beanColumnName": "id",
                            "type": "DECIMAL"
                          },
                          {
                            "id": 2,
                            "pos": 1,
                            "templateName": "Name",
                            "dbColumnName": "NAME",
                            "beanColumnName": "name",
                            "type": "VARCHAR"
                          },
                          {
                            "id": 3,
                            "pos": 2,
                            "templateName": "DOB",
                            "dbColumnName": "DOB",
                            "beanColumnName": "dob",
                            "type": "TIMESTAMP"
                          }
                        ] }
  

Have this Template available in spring context. have a Template handler to handle each template, which is intern has association for FileTemplate and ColumnTemplate. FileTemplate has Maps to help in getting column config's.

Add dependency in POM for Apacahe POI and Commons BeanUtils

    <dependency>
		  <groupId>commons-beanutils</groupId>
		  <artifactId>commons-beanutils</artifactId>
		</dependency>
		<dependency>
		  <groupId>org.apache.poi</groupId>
		  <artifactId>poi</artifactId>
		  <version>3.10-FINAL</version>
	 </dependency>
	 <dependency>
      <groupId>org.apache.poi</groupId>
  		<artifactId>poi-ooxml</artifactId>
    	<version>3.10-FINAL</version>
		</dependency>

With all the configuration and dependency set. Now lets look at the ExcelUtility so called "Dynamic excel to Object Collection Binder!"

As mentioned earlier to have this as generic and KISS.

iterate thru all the rows in excel {
  use fileTemplate(file config) to iterate thru all the columns
  {
    do switch case to check for each Column Type and get respective cell value from cell.
    create target object and use beanutils to set each prop value
    handle for cell/data type parsing exception. add it to validation errors
  }
}

ExcelUtility.java

public class ExcelUtility {

	private final static Logger log = LoggerFactory
			.getLogger(ExcelUtility.class);

	public static <T extends Base>  List<T> readXlFile(Workbook workbook,
			FileTemplate fileTemplate, Class<T> clazz){
		return readXlFile(workbook, fileTemplate, clazz, true);
	}
	public static <T extends Base>  List<T> readXlFile(Workbook workbook,
			FileTemplate fileTemplate, Class<T> clazz, boolean hasHeaderRow) {
		List<T> objList = null;
		BindException bindException;
		T obj ;
		ColumnTemplate column;

		try {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			objList = new ArrayList<T>();

			while (rowIterator.hasNext()) {
				obj = clazz.newInstance();
				
				Row row = (Row) rowIterator.next();
				bindException = new BindException(obj, ""+ row.getRowNum());

				if (hasHeaderRow && row.getRowNum() < 1) {
					continue; // just skip the row if row number 0  if header row is true
				}
				

					for (Integer pos : fileTemplate.getColumnTemplatesMap()
							.keySet()) {
						column = fileTemplate.getColumnTemplateByPos(pos);
						switch (column.getType()) {
						case TIMESTAMP:
							BeanUtils.setProperty(
									obj,
									column.getBeanColumnName(),
									getDateCellValue(row.getCell(pos),
											bindException, column));
							break;
						case DECIMAL:
							BeanUtils.setProperty(
									obj,
									column.getBeanColumnName(),
									getDecimalCellValue(row.getCell(pos),
											bindException, column));
							break;

						default:
							BeanUtils.setProperty(
									obj,
									column.getBeanColumnName(),
									getStringCellValue(row.getCell(pos),
											bindException, column));
							break;
						}
					}
				
				obj.setErrors(bindException);
				obj.setRow(row.getRowNum()+1);
				objList.add(obj);
			}
		} catch (Exception e) {
			log.error("File contain some invalid values" + e.getMessage());
			e.printStackTrace();
		}

		return objList;
	}
	
	private static Date getDateCellValue(Cell cell ,
			Errors result, ColumnTemplate columnTemplate) {
		Date parsedDate = null;
		if(cell==null){
			return parsedDate;
		}
		try {
		parsedDate = cell.getDateCellValue();
		} catch (IllegalStateException | NumberFormatException e ) {
			log.error(e.getMessage());
			result.rejectValue(columnTemplate.getBeanColumnName(),
					INVALID_DATE_VALUE, cell.toString());
		}
		return parsedDate;
	}

	private static String getStringCellValue(Cell cell, Errors errors,
			ColumnTemplate columnTemplate) {

		String rtrnVal = "";
		if(cell==null){
			return rtrnVal;
		}
		try {

			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				rtrnVal = "" + cell.getNumericCellValue();
			} else {
				rtrnVal = cell.getStringCellValue();
			}
		} catch (Exception exception) {
			log.error(exception.getMessage());
			exception.printStackTrace();
			errors.rejectValue(columnTemplate.getBeanColumnName(),
					INVALID_STRING_VALUE, cell.toString());
		}
		return rtrnVal;
	}

	private static BigDecimal getDecimalCellValue(Cell cell, Errors errors,
			ColumnTemplate columnTemplate) {
		BigDecimal rtrnVal = new BigDecimal(0);
		if(cell==null){
			return rtrnVal;
		}
		try {
			if (cell != null) {
				if (cell.toString().trim().length() == 0)
					return rtrnVal = new BigDecimal(0);
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					rtrnVal = BigDecimal.valueOf(cell.getNumericCellValue());
					return rtrnVal = rtrnVal.setScale(3,
							BigDecimal.ROUND_CEILING).stripTrailingZeros();
				}
			}
		} catch (Exception exception) {
			log.error(exception.getMessage());
			errors.rejectValue(columnTemplate.getBeanColumnName(),
					INVALID_DECIMAL_VALUE, cell.toString());
		}

		return rtrnVal;
	}
}


hope this helps. download the code and try it yourself. post your feedback

planning to do validation in POJO, this is when we have to use validator to validate form inputs(if you already have one, just to make use of it). and respond back to use what errors they had in excel.

This is my File :

ID	Name	DOB

1	Prabu	8/25/2014

2	Ilan	8/25/2014

And binded Object List:

[User [id=1, name=Prabu, dob=Mon Aug 25 00:00:00 EDT 2014], User [id=2, name=Ilan, dob=Mon Aug 25 00:00:00 EDT 2014]]

Want to contibute. write to me.
