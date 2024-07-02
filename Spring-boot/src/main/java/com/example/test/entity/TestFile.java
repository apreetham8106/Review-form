package com.example.test.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="r_file")
public class TestFile {



	    @Id
	    @GeneratedValue(generator =  "genfileid")
		@GenericGenerator(name = "genfileid", strategy = "com.example.test.generator.FileIDGenerator")
		private String id;
	    
	    private String fileName;
	    private String filePath;
	    private String fileType;
	    
	    
	    

		@ManyToOne
	    @JoinColumn(name="test_id")
	    private Test test;


		public String getId() {
			return id;
		}


		public void setId(String id) {
			this.id = id;
		}


		public String getFileName() {
			return fileName;
		}


		public void setFileName(String fileName) {
			this.fileName = fileName;
		}


		public String getFilePath() {
			return filePath;
		}


		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}


		public String getFileType() {
			return fileType;
		}


		public void setFileType(String fileType) {
			this.fileType = fileType;
		}


		


		public Test getTest() {
			return test;
		}


		public void setTest(Test test) {
			this.test = test;
		}
		
		
}


