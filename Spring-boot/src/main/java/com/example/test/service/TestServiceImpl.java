package com.example.test.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.example.test.DTO.FilesDTO;
import com.example.test.DTO.TestWithFilesDTO;
import com.example.test.entity.TestFile;
import com.example.test.entity.Test;
import com.example.test.repository.FileEntityRepository;
import com.example.test.repository.TestRepository;

@Service
public class TestServiceImpl implements TestService {
	
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private FileEntityRepository fileEntityRepository;
	

	@Override
	public Test saveTest(Test test) {
		// TODO Auto-generated method stub
		return testRepository.save(test);
	}

	@Override
	public TestFile saveFile(String id, MultipartFile file) throws IOException{
		// TODO Auto-generated method stub
		Test test = getTestById(id);

        if (test == null) {
            throw new IllegalArgumentException("Person not found with id: " + id);
        }

       

        String folderPath = "E:\\Rahul\\files/";
 
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(folderPath, fileName);

        // Check if file with the same name already exists
//        if (Files.exists(filePath)) {
//            throw new FileAlreadyExistsException("File with the same name already exists: " + fileName);
//        }

        // Continue with the existing logic to save the file

        // Ensure the folder exists; create it if it doesn't
        Path folder = Path.of(folderPath);
        if (Files.notExists(folder)) {
            Files.createDirectories(folder);
        }

        // Save file content to the file system
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        TestFile fileEntity = new TestFile();
        fileEntity.setFileName(fileName);
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setTest(test);

        return fileEntityRepository.save(fileEntity);
    }
	
	public Test getTestById(String id) {
        return testRepository.findById(id).orElse(null);
    }
		
	

	@Override
	public List<TestWithFilesDTO> getAllTestWithFiles() {
		// TODO Auto-generated method stub
		 List<Test> people = testRepository.findAll();
	        return people.stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	
	}
	
	private TestWithFilesDTO convertToDTO(Test test) {
        TestWithFilesDTO dto = new TestWithFilesDTO();
        dto.setId(test.getId());
        dto.setName(test.getName());
        dto.setDesignation(test.getDesignation());
        dto.setDepartment(test.getDepartment());
        dto.setId_no(test.getId_no());
        dto.setMobile_no(test.getMobile_no());
        dto.setDob(test.getDob());
        dto.setReviews(test.getReviews());
        
        
        
      

        List<TestFile> files = fileEntityRepository.findByTest(test);
        List<FilesDTO> fileDTOs = files.stream()
                .map(fileEntity -> {
                    FilesDTO fileDTO = new FilesDTO();
                    fileDTO.setFileName(fileEntity.getFileName());
                    fileDTO.setFilePath(fileEntity.getFilePath());
                    fileDTO.setFileType(fileEntity.getFileType());
                    return fileDTO;
                })
                .collect(Collectors.toList());

        dto.setFiles(fileDTOs);
        
        
        
        
        
        

        return dto;
    }
	

	@Override
	public TestWithFilesDTO getTestWithFilesById(String id) {
		// TODO Auto-generated method stub
		Test test = testRepository.findById(id).orElse(null);

        if (test != null) {
            return convertToDTO(test);
        } else {
            return null; // Or throw an exception if person is not found
        }
	}
	
	@SuppressWarnings("unused")
	private TestWithFilesDTO convertToDTO1(Test test) {
        TestWithFilesDTO dto = new TestWithFilesDTO();
        dto.setId(test.getId());
        dto.setName(test.getName());
        dto.setDesignation(test.getDesignation());
        dto.setDepartment(test.getDepartment());
        dto.setId_no(test.getId_no());
        dto.setMobile_no(test.getMobile_no());
        dto.setDob(test.getDob());
        dto.setReviews(test.getReviews());

        List<TestFile> files = fileEntityRepository.findByTest(test);
        List<FilesDTO> fileDTOs = files.stream()
                .map(fileEntity -> {
                    FilesDTO fileDTO = new FilesDTO();
                    fileDTO.setFileName(fileEntity.getFileName());
                    fileDTO.setFilePath(fileEntity.getFilePath());
                    fileDTO.setFileType(fileEntity.getFileType());
                    return fileDTO;
                })
                .collect(Collectors.toList());

        dto.setFiles(fileDTOs);

        return dto;
    }

	@Override
	public void deleteTestAndFilesById(String id) {
		// TODO Auto-generated method stub
		Test test = testRepository.findById(id).orElse(null);

        if (test != null) {
            // Delete associated files
            List<TestFile> files = fileEntityRepository.findByTest(test);
            for (TestFile fileEntity : files) {
                // Instead of directly deleting the file, update a flag
//                fileEntity.setDeleteflag('N');
                fileEntityRepository.save(fileEntity);
            }

            // Update the delete flag for the person
//            test.setDeleteFlag('N');
            testRepository.save(test);
        }
    }
		
	

	@Override
	public Test updateTestDetails(String id, Test updatedTest) {
		// TODO Auto-generated method stub
		Test existingTest = testRepository.findById(id).orElse(null);

        if (existingTest != null) {
            // Update person details
            existingTest.setName(updatedTest.getName());
            existingTest.setDesignation(updatedTest.getDesignation());
            existingTest.setDepartment(updatedTest.getDepartment());
            existingTest.setId_no(updatedTest.getId_no());
            existingTest.setMobile_no(updatedTest.getMobile_no());
            existingTest.setDob(updatedTest.getDob());
            existingTest.setReviews(updatedTest.getReviews());
            
            

            return testRepository.save(existingTest);
        } else {
            return null; // Or throw an exception if person is not found
        }
		
	}

	@Override
	public List<TestFile> updateFilesForTest(String id, MultipartFile[] updatedFiles) {
		// TODO Auto-generated method stub
		Test test = testRepository.findById(id).orElse(null);

        if (test != null) {
            // Get existing files for the person
            List<TestFile> existingFiles = fileEntityRepository.findByTest(test);
            
            
            
            
            

            // Save updated files metadata and content for the person
            for (MultipartFile file : updatedFiles) {
                String folderPath = "C://Users//INTERNET//Desktop//files//";
                String fileName = file.getOriginalFilename();
                Path filePath = Path.of(folderPath, fileName);

                
                
             
                
                
                
                
                try {
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    // You may choose to throw an exception or handle it in another way
                }

                TestFile fileEntity = new TestFile();
                fileEntity.setFileName(fileName);
                fileEntity.setFileType(file.getContentType());
                fileEntity.setFilePath(filePath.toString());
                fileEntity.setTest(test);

                fileEntityRepository.save(fileEntity);
                existingFiles.add(fileEntity); // Add the new file to the list
            }

            return existingFiles;
        } else {
            return null; // Or throw an exception if person is not found
        }
	}

	@Override
	public TestFile getUserFile(String id, String filename) {
		// TODO Auto-generated method stub
		 try {
	            Test test = testRepository.findById(id).orElse(null);

	            if (test != null) {
	                return fileEntityRepository.findByTestAndFileName(test, filename);
	            } else {
	                // Handle the case when the person is not found
	                return null;
	            }
	        } catch (Exception e) {
	            // Log error details using your preferred logging framework
	            // For example, using SLF4J: logger.error("Error occurred: " + e.getMessage(), e);
	            System.err.println("Error occurred: " + e.getMessage());
	            e.printStackTrace();
	            throw new RuntimeException("Error occurred while retrieving user file.", e);
	        }
	}

	

	@Override
	public void deletePersonAndFilesById(String id) {
		
		
		
		Test person = testRepository.findById(id).orElse(null);

        if (person != null) {
            // Delete associated files
            List<TestFile> files = fileEntityRepository.findByTest(person);
            for (TestFile fileEntity : files) {
            	fileEntityRepository.delete(fileEntity);
                
                
                Path filePath = Paths.get(fileEntity.getFilePath());
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                    // You may choose to throw an exception or handle it in another way
                }
            }

            // Delete the person
            testRepository.delete(person);
	
		
	}
        
	}

	@Override
	public void deleteTestAndFilesByFilename(String id, String filename) {
		Test person = testRepository.findById(id).orElse(null);

	        if (person != null) {
	            TestFile fileToDelete = fileEntityRepository.findByTestAndFileName(person, filename);

	            if (fileToDelete != null) {
	                // Delete the file from the repository
	                fileEntityRepository.delete(fileToDelete);

	                // Delete the physical file from the file system
	                Path filePath = Paths.get(fileToDelete.getFilePath());
	                try {
	                    Files.deleteIfExists(filePath);
	                } catch (IOException e) {
	                    e.printStackTrace(); // Handle the exception appropriately
	                    // You may choose to throw an exception or handle it in another way
	                }
	            }
	        }
		
	}
	
}
