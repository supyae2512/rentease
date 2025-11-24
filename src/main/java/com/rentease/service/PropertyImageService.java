package com.rentease.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentease.entity.Property;
import com.rentease.entity.PropertyImage;
import com.rentease.repository.PropertyImageRepository;


@Service
public class PropertyImageService {

	@Autowired
    private PropertyImageRepository propertyImageRepository;

    public PropertyImage addImage(PropertyImage image) {
        return propertyImageRepository.save(image);
    }

    public List<PropertyImage> getImagesByProperty(Property property) {
        return propertyImageRepository.findByProperty(property);
    }

    public void deleteImage(Long id) {
        propertyImageRepository.deleteById(id);
    }

	public Optional<PropertyImage> findById(Long propertyImageId) {
		return propertyImageRepository.findById(propertyImageId);
	}

}
