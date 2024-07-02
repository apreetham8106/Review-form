package com.example.test.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.example.test.DTO.TestWithFilesDTO;
import com.example.test.entity.TestFile;
import com.example.test.entity.Test;
import com.example.test.exception.FileAlreadyExistsException;
import com.example.test.service.TestService;

import jakarta.servlet.http.HttpServletResponse;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Test")

public class TestController {
	
	@Autowired
	private TestService testService;
	
//	@PostMapping("/")
//	private String addTest(@RequestBody Test test) {
//		testService.addTest(test);
//		return "jjjj";
//	}
	
	//post
	@PostMapping("/")
	public ResponseEntity<Test> saveTest(@RequestBody Test test) {
		Test savedTest = testService.saveTest(test);
		return new ResponseEntity<>(savedTest, HttpStatus.CREATED);
    }
	
	//post files
	@PostMapping("/{id}")
    public ResponseEntity<Void> uploadFiles(@PathVariable String id, @RequestParam("files") MultipartFile[] files) {
		        try {
		            for (MultipartFile file : files) {
		                testService.saveFile(id, file);
		            }
		            return new ResponseEntity<>(HttpStatus.CREATED);
		        } catch (FileAlreadyExistsException e) {
		            // Handle FileAlreadyExistsException here
		            return new ResponseEntity<>(HttpStatus.CONFLICT); // You can customize the HTTP status as needed
	        } 
	            catch (IOException e) {
		            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    }
	
//	@GetMapping("/")
//	private List<Test> getTest(){
//		return testService.getTest();
//	}
//	
//	@GetMapping("/{id}")
//	private Test getById(@PathVariable String id) {
//		return testService.getById(id);
//	}
	
	
	  @GetMapping("/")
	    public ResponseEntity<List<TestWithFilesDTO>> getAllTestWithFiles() {
	        List<TestWithFilesDTO> testWithFiles = testService.getAllTestWithFiles();
	        return new ResponseEntity<>(testWithFiles, HttpStatus.OK);
	    }
	  
	  //get by id
	  @GetMapping("/{id}")
	    public ResponseEntity<TestWithFilesDTO> getTestWithFilesById(@PathVariable String id) {
	        TestWithFilesDTO testWithFiles = testService.getTestWithFilesById(id);

	        if (testWithFiles != null) {
	            return new ResponseEntity<>(testWithFiles, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	  
	  
	  
	  
	
//	@DeleteMapping("/{id}")
//		private String deleteById(@PathVariable String id) {
//			testService.deleteById(id);
//			return "Deleted";
//		}
//	@DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTestAndFilesById(@PathVariable String id) {
//        testService.deleteTestAndFilesById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
	
	
	
	
	  @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonAndFilesById(@PathVariable String id) {
		  testService.deletePersonAndFilesById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	
	
	
	
	
	
	
	
	
	
//	@PutMapping("/{id}")
//		private String updateById(@RequestBody Test test, @PathVariable String id) {
//			testService.updateById(test,id);
//			return "Updated";
//	}
	
	//update all only person
	  @PutMapping("/{id}")
	    public ResponseEntity<Test> updateTestDetails(@PathVariable String id, @RequestBody Test updatedTest) {
	        Test updated = testService.updateTestDetails(id, updatedTest);

	        if (updated != null) {
	            return new ResponseEntity<>(updated, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	  
	  
	  //update files
	  @PutMapping("/{id}/files")
	    public ResponseEntity<List<TestFile>> updateFilesForTest(
	            @PathVariable String id,
	            @RequestParam("files") MultipartFile[] updatedFiles
	    ) {
	        List<TestFile> updated = testService.updateFilesForTest(id, updatedFiles);

	        if (updated != null) {
	            return new ResponseEntity<>(updated, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	  
	  @GetMapping("/{id}/{filename:.+}")
	    public void downloadUserFile(
	            @PathVariable String id,
	            @PathVariable String filename,
	            HttpServletResponse response
	    ) {
	        try {
	            TestFile file = testService.getUserFile(id, filename);

	            if (file != null) {
	                response.setContentType(file.getFileType());
	                response.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());

	                try (InputStream inputStream = new FileInputStream(new File(file.getFilePath()))) {
	                    IOUtils.copy(inputStream, response.getOutputStream());
	                }

	                response.flushBuffer();
	            } else {
	                // If the file is not found
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            }
	        } catch (IOException e) {
	            logErrorDetails(e);
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        } catch (Exception e) {
	            logErrorDetails(e);
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	    }

	    private void logErrorDetails(Exception e) {
	        // Log error details using your preferred logging framework
	        // For example, using SLF4J: logger.error("Error occurred: " + e.getMessage(), e);
	        System.err.println("Error occurred: " + e.getMessage());
	        e.printStackTrace();
	    }
	 
	  	  
	   
	    
	    //delete filename
	    @DeleteMapping("/{id}/delete/{filename:.+}")
	    public ResponseEntity<Void> deleteTestAndFilesByFilename(
	        @PathVariable String id,
	        @PathVariable String filename
	    ) {
	        testService.deleteTestAndFilesByFilename(id, filename);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	  
	}

