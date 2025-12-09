package com.rentease.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.rentease.dto.PropertyDTO;
import com.rentease.dto.PropertyDetailDTO;
import com.rentease.entity.Property;
import com.rentease.entity.Property.PropertyStatus;
import com.rentease.entity.Property.PropertyType;
import com.rentease.entity.PropertyDetail;
import com.rentease.entity.PropertyImage;
import com.rentease.entity.User;
import com.rentease.repository.PropertyDetailRepository;
import com.rentease.repository.PropertyRepository;
import com.rentease.utils.GoogleHelper;

@Service
public class PropertyService {


	@Autowired
    private PropertyRepository propertyRepository;
	
	@Autowired
    private PropertyDetailRepository propertyDetailRepository;
	
	@Autowired
	private GoogleHelper googleHelper;
	
    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

//    public Property createProperty(Property property) {
//        property.setCreatedAt(LocalDateTime.now());
//        property.setUpdatedAt(LocalDateTime.now());
//        property.setIsActive(true);
//        return propertyRepository.save(property);
//    }
	public Property createProperty(PropertyDTO dto, User advertiser) {

        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setPrice(dto.getPrice());

        property.setAddress(dto.getAddress());
        property.setCity(dto.getCity());
        property.setPostalCode(dto.getPostalCode());
        property.setCountry(dto.getCountry());

        property.setLatitude(dto.getLatitude());
        property.setLongitude(dto.getLongitude());
        
        property.setCurrency(dto.getCurrency());

        property.setPropertyStatus(dto.getPropertyStatus());
        property.setPropertyType(dto.getPropertyType());

        property.setAdvertiser(advertiser);
        property.setIsActive(true);
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
        
        property.setLeaseTerm(dto.getLeaseTerm());
        property.setMinLeaseMonths(dto.getMinLeaseMonths());

        // Save Property first (required for 1-to-1 mapping)
        Property savedProperty = propertyRepository.save(property);


        if (dto.getPropertyDetail() != null) {
            PropertyDetailDTO detailDTO = dto.getPropertyDetail();

            PropertyDetail detail = new PropertyDetail();
            detail.setBedrooms(detailDTO.getBedrooms());
            detail.setBathrooms(detailDTO.getBathrooms());
            detail.setKitchens(detailDTO.getKitchens());
            detail.setSquareFeet(detailDTO.getSquareFeet());
            detail.setYearBuilt(detailDTO.getYearBuilt());

            detail.setHasParking(detailDTO.getHasParking());
            detail.setHasSwimmingPool(detailDTO.getHasSwimmingPool());
            detail.setHasElevator(detailDTO.getHasElevator());
            detail.setHasGarden(detailDTO.getHasGarden());
            detail.setHasSecurity(detailDTO.getHasSecurity());
            detail.setHasGym(detailDTO.getHasGym());

            detail.setFurnishType(detailDTO.getFurnishType());

            // JSON column
            detail.setAdditionalFeatures(detailDTO.getAdditionalFeatures());
            
            detail.setProperty(savedProperty);

            savedProperty.setPropertyDetail(detail);
            propertyDetailRepository.save(detail);
        }

        return savedProperty;
    }

	public Property updateProperty(Long id, PropertyDTO dto) {

	    Property property = propertyRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Property not found"));

	    // Validate advertiser (optional but recommended)
//	    if (!property.getAdvertiser().getId().equals(advertiserId)) {
//	        throw new RuntimeException("You are not authorized to update this listing.");
//	    }

	    // Update base fields
	    property.setTitle(dto.getTitle());
	    property.setDescription(dto.getDescription());
	    property.setPrice(dto.getPrice());
	    property.setAddress(dto.getAddress());
	    property.setCity(dto.getCity());
	    property.setPostalCode(dto.getPostalCode());
	    property.setCountry(dto.getCountry());
	    property.setLatitude(dto.getLatitude());
	    property.setLongitude(dto.getLongitude());

	    property.setPropertyStatus(dto.getPropertyStatus());
	    property.setPropertyType(dto.getPropertyType());

	    property.setUpdatedAt(LocalDateTime.now());

	    if (dto.getPropertyDetail() != null) {

	        PropertyDetailDTO detailDTO = dto.getPropertyDetail();
	        PropertyDetail detail = property.getPropertyDetail();

	        if (detail == null) {
	            detail = new PropertyDetail();
	            detail.setProperty(property);
	            property.setPropertyDetail(detail);
	        }

	        detail.setBedrooms(detailDTO.getBedrooms());
	        detail.setBathrooms(detailDTO.getBathrooms());
	        detail.setKitchens(detailDTO.getKitchens());
	        detail.setSquareFeet(detailDTO.getSquareFeet());
	        detail.setYearBuilt(detailDTO.getYearBuilt());

	        detail.setHasParking(detailDTO.getHasParking());
	        detail.setHasSwimmingPool(detailDTO.getHasSwimmingPool());
	        detail.setHasElevator(detailDTO.getHasElevator());
	        detail.setHasGarden(detailDTO.getHasGarden());
	        detail.setHasSecurity(detailDTO.getHasSecurity());
	        detail.setHasGym(detailDTO.getHasGym());

	        detail.setFurnishType(detailDTO.getFurnishType());
	        detail.setAdditionalFeatures(detailDTO.getAdditionalFeatures());
	    }

	    return propertyRepository.save(property);
	}


//    public void deleteProperty(Long id) {
//        propertyRepository.deleteById(id);
//    }
	public void deleteProperty(Long id, Long advertiserId) {

	    Property property = propertyRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Property not found"));

	    // Authorization check
	    if (!property.getAdvertiserId().equals(advertiserId)) {
	        throw new RuntimeException("Not authorized to delete this listing.");
	    }

	    // Optional: Remove files from Google Cloud Storage
	    if (property.getImages() != null) {
	        for (PropertyImage img : property.getImages()) {
	        	String publicUrl = img.getImageUrl();
	        	// Get Object Path; remove domain and bucket name;
	        	String prefix = "https://storage.cloud.google.com/" + bucketName + "/";
	            String objectName = publicUrl.replace(prefix, "");
	        	
	            googleHelper.deleteFileFromGcs(objectName);
	        }
	    }

	    // Cascade DELETE will remove detail + images
	    propertyRepository.delete(property);
	}
	
	public Page<Property> listProperties(
	        int page, 
	        int size,
	        String city,
	        String country,
	        Double minPrice,
	        Double maxPrice,
	        Property.PropertyStatus status,
	        Property.PropertyType type
	) {

	    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

	    return propertyRepository.searchProperties(
	            city,
	            country,
	            minPrice,
	            maxPrice,
	            status,
	            type,
	            pageable
	    );
	    
	}



    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    public List<Property> getAllActiveProperties() {
        return propertyRepository.findByIsActiveTrue();
    }

    public List<Property> getPropertiesByAdvertiser(User advertiser) {
        return propertyRepository.findByAdvertiser(advertiser);
    }

    public List<Property> getPropertiesByCity(String city) {
        return propertyRepository.findByCityIgnoreCaseAndIsActiveTrue(city);
    }

    public List<Property> getPropertiesByPriceRange(Double minPrice, Double maxPrice) {
        return propertyRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice);
    }
    
    public long countByType(PropertyType type) {
        return propertyRepository.countByPropertyType(type);
    }

    public long countByStatus(PropertyStatus status) {
        return propertyRepository.countByPropertyStatus(status);
    }
    
    public void validateOwner(Long propertyId, Long userId) {
        Property p = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!p.getAdvertiserId().equals(userId)) {
            throw new RuntimeException("Permission denied");
        }
    }

}
