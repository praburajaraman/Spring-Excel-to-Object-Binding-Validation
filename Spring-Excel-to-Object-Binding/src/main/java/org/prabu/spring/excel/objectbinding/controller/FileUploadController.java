package org.prabu.spring.excel.objectbinding.controller;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.prabu.spring.excel.objectbinding.domain.User;
import org.prabu.spring.excel.objectbinding.handler.FileUploadTemplateHandler;
import org.prabu.spring.excel.objectbinding.utils.ExcelUtility;
import org.springframework.beans.factory.annotation.Autowired;
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
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name, 
            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
            	Workbook workbook;
				List<User> userList;
				if (file.getOriginalFilename().endsWith("xls")) {
					workbook = new HSSFWorkbook(file.getInputStream());
				} else if (file.getOriginalFilename().endsWith("xlsx")) {
					workbook = new XSSFWorkbook(file.getInputStream());
				} else {					
					return " Uploading fail Invalid File!";
				}
				
				userList = ExcelUtility.readXlFile(workbook, fileUploadTemplateHandler.getUserfileTemplate(), User.class);
				
                System.out.println(userList);
				
				return "You successfully uploaded " + name + " into " + name + "-uploaded !";
                
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
    
}
