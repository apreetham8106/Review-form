package com.example.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.test.entity.Test;

public interface TestRepository extends JpaRepository<Test, String>{

	Optional<Test> findById(String id);



}
