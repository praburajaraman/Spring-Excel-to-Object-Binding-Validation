package org.prabu.spring.excel.objectbinding.controller;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.prabu.spring.excel.objectbinding.domain.Base;
import org.prabu.spring.excel.objectbinding.domain.User;
import org.prabu.spring.excel.objectbinding.handler.FileUploadTemplateHandler;
import org.prabu.spring.excel.objectbinding.utils.ExcelUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class FileUploadController {
	
	@Autowired
	FileUploadTemplateHandler  fileUploadTemplateHandler;
	
	private final static Logger log = LoggerFactory
			.getLogger(FileUploadController.class);
    
    public FileUploadTemplateHandler getFileUploadTemplateHandler() {
		return fileUploadTemplateHandler;
	}

	public void setFileUploadTemplateHandler(
			FileUploadTemplateHandler fileUploadTemplateHandler) {
		this.fileUploadTemplateHandler = fileUploadTemplateHandler;
	}

	@RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
    
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public  @ResponseBody String handleFileUpload(@RequestParam("name") String name, 
            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
            	List<Base> userList;
            	userList = convertXl(file);
				
				return "You successfully uploaded " + userList + " into " + name + "-uploaded !";
                
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
    
    @RequestMapping(value="/getJson", method=RequestMethod.POST,produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody <T extends Base > List<T> getJson(@RequestParam("name") String name, 
            @RequestParam("file") MultipartFile file){
    	try{
    		return convertXl(file);	
    	}catch(Exception e){
    		log.info(e.getMessage());
    		e.printStackTrace();
    	}
    	return null;
    	
    }
    
    @RequestMapping(value="/getXml", method=RequestMethod.POST,produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody <T extends Base > List<T> getXml(@RequestParam("name") String name, 
            @RequestParam("file") MultipartFile file){
    	try{
    		return convertXl(file);	
    	}catch(Exception e){
    		log.info(e.getMessage());
    		e.printStackTrace();
    	}
    	return null;
    	
    }
    private <T extends Base> List<T> convertXl( MultipartFile file) throws Exception{
    	List<T> list = null;
    			  if (!file.isEmpty()) {
    		            try {
    		            	Workbook workbook;
    						if (file.getOriginalFilename().endsWith("xls")) {
    							workbook = new HSSFWorkbook(file.getInputStream());
    						} else if (file.getOriginalFilename().endsWith("xlsx")) {
    							workbook = new XSSFWorkbook(file.getInputStream());
    						} else {					
    							return list;
    						}
    						
    						list = (List<T>)ExcelUtility.readXlFile(workbook, fileUploadTemplateHandler.getUserfileTemplate(), User.class);
    						
    		                System.out.println(list);
    						
    						return list;
    		                
    		            } catch (Exception e) {
    		                throw new Exception(e.getMessage());
    		            }
    		        } else {
    		        	 throw new Exception("You failed to upload   because the file was empty.");
    		        }
    }
    
}
