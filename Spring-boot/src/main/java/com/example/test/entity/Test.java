package com.example.test.entity;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="reviews")
public class Test {
	
	@Id
	@GeneratedValue(generator =  "genid")
	@GenericGenerator(name = "genid", strategy = "com.example.test.generator.CustomIdGenerator")
	private String id;
	
	@Column(name = "r_name", length = 100)
	private String name;
	@Column(name ="r_designation", length = 100)
	private String designation;
	@Column(name = "r_department", length = 100)
	private String department;
	@Column(name = "r_id_no", length = 100)
	private String id_no;
	@Column(name = "r_mobile_no", length = 100)
	private String mobile_no;
	@Column(name="r_dob",length = 100)
	private String dob;
	@Column(name = "r_reviews", length = 200)
	private String reviews;

	
	 @JsonIgnore
	 @OneToMany(mappedBy="test", cascade = CascadeType.ALL)
	 private List<TestFile> files;
	
	 
	 public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	
	public List<TestFile> getFiles() {
		return files;
	}
	public void setFiles(List<TestFile> files) {
		this.files = files;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getId_no() {
		return id_no;
	}
	public void setId_no(String id_no) {
		this.id_no = id_no;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getReviews() {
		return reviews;
	}
	public void setReviews(String reviews) {
		this.reviews = reviews;
	}
		   
}
