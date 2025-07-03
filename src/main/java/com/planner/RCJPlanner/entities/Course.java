package com.planner.RCJPlanner.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Course {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String provider; // e.g., Coursera, Udemy
    private String link;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Course(Long id, String title, String description, String provider, String link) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.provider = provider;
		this.link = link;
	}
	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public Course(String title, String description, String provider, String link) {
		super();
		this.title = title;
		this.description = description;
		this.provider = provider;
		this.link = link;
	}
	
    
}
