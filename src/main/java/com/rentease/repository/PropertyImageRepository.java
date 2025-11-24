package com.rentease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentease.entity.Property;
import com.rentease.entity.PropertyImage;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long>{
	
	// Get all images for a specific property
    List<PropertyImage> findByProperty(Property property);

}
