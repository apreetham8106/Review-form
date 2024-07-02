package com.example.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.test.entity.TestFile;
import com.example.test.entity.Test;



public interface FileEntityRepository extends JpaRepository<TestFile, String>{
	
	List<TestFile> findByTest(Test test);
    // You can add custom query methods if needed

	TestFile findByTestAndFileName(Test test, String filename);

}
