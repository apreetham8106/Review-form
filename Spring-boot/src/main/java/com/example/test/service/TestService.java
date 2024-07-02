package com.example.test.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.test.DTO.TestWithFilesDTO;
import com.example.test.entity.TestFile;
import com.example.test.entity.Test;

@Service
public interface TestService {



	public Test saveTest(Test test);

	public TestFile saveFile(String id, MultipartFile file) throws IOException;

	public List<TestWithFilesDTO> getAllTestWithFiles();

	public TestWithFilesDTO getTestWithFilesById(String id);

	public void deleteTestAndFilesById(String id);

	public Test updateTestDetails(String id, Test updatedTest);

	public List<TestFile> updateFilesForTest(String id, MultipartFile[] updatedFiles);

	public TestFile getUserFile(String id, String filename);

	

	public void deletePersonAndFilesById(String id);

	public void deleteTestAndFilesByFilename(String id, String filename);

}
