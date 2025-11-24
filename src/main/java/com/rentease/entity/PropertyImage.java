package com.rentease.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "property_images")
public class PropertyImage {

	 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String imageUrl;
	    private String fileName;
	    private String contentType;
	    
	    private LocalDateTime createdAt = LocalDateTime.now();

	    @ManyToOne
	    @JoinColumn(name = "property_id", nullable = false)
	    @JsonBackReference
	    private Property property;

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getFileName() {
			return fileName;
		}

		public Property getProperty() {
			return property;
		}

		public void setProperty(Property property) {
			this.property = property;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

}
