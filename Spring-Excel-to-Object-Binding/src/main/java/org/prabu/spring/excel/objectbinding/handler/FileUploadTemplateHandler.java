package org.prabu.spring.excel.objectbinding.handler;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class FileUploadTemplateHandler {
	
	FileTemplate userfileTemplate;
	
	public FileUploadTemplateHandler() {
		initUserFileTemplate();
	}
	
	private void initUserFileTemplate(){
		ObjectMapper mapper = new ObjectMapper();
		 
		try {
			
			Resource resource = new ClassPathResource("properties/fileTemplate.json");
			System.out.println(resource.getFile());
			// read from file, convert it to class
			userfileTemplate = mapper.readValue(resource.getInputStream(), FileTemplate.class);
	 
			// display to console
			System.out.println(userfileTemplate);
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
	}

	public FileTemplate getUserfileTemplate() {
		return userfileTemplate;
	}

	public void setUserfileTemplate(FileTemplate userfileTemplate) {
		this.userfileTemplate = userfileTemplate;
	}


	
	

}
