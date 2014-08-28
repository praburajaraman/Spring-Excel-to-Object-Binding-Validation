package org.prabu.spring.excel.objectbinding.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.prabu.spring.excel.objectbinding.domain.Base;
import org.prabu.spring.excel.objectbinding.domain.ExcelFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExcelUtilityParallelProcessor {
	
	ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*16);
	
	private final static Logger log = LoggerFactory
			.getLogger(ExcelUtilityParallelProcessor.class);
	
	public <T extends Base> List<T> readExcelInParallel(ExcelFile excelFile) throws Exception{
		
		List<T> list = new ArrayList<T>();
		log.info("start time ~~~~~~~~~~~~~~~");
		
		try{
			List<Future<List<? extends Base>>> results = new ArrayList<Future<List<? extends Base>>>(excelFile.getNoOfthreadsToProcess());
			
			for (int i  :  excelFile.getSheetThreads().keySet()) {
				results.add(executorService.submit(new ExcelUtilityParallelReader(excelFile, excelFile.getSheetThreads().get(i),i==1)));
			}
			
			log.info("list assembling ~~~~~~~~~~~~~~~");
			
			for(Future<List<? extends Base>> result : results){
				list.addAll((Collection<? extends T>) result.get());
			}
		}
		
		catch (Exception e) {
            throw e;
        }
		
		log.info("end time ~~~~~~~~~~~~~~~");
		return list;
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		try {
			executorService.shutdown();
			}
		 	catch (Exception e) {
			// TODO: handle exception
		 		log.info(e.getMessage());
		 		e.printStackTrace();
		 	}
	}

}
