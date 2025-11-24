package com.rentease.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentease.entity.Property;
import com.rentease.entity.Property.PropertyStatus;
import com.rentease.entity.Property.PropertyType;
import com.rentease.entity.User;

public interface PropertyRepository extends JpaRepository<Property, Long>  {
	
	// Get all active properties for public viewers
    List<Property> findByIsActiveTrue();

    // Get all properties posted by a specific advertiser
    List<Property> findByAdvertiser(User advertiser);

    // Optional: filter by city
    List<Property> findByCityIgnoreCaseAndIsActiveTrue(String city);

    // Optional: filter by price range
    List<Property> findByPriceBetweenAndIsActiveTrue(Double minPrice, Double maxPrice);
    
    
    @Query("""
    	    SELECT p FROM Property p
    	    WHERE (:city IS NULL OR p.city = :city)
    	      AND (:country IS NULL OR p.country = :country)
    	      AND (:minPrice IS NULL OR p.price >= :minPrice)
    	      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    	      AND (:status IS NULL OR p.propertyStatus = :status)
    	      AND (:type IS NULL OR p.propertyType = :type)
    	""")
    	Page<Property> searchProperties(
    	        @Param("city") String city,
    	        @Param("country") String country,
    	        @Param("minPrice") Double minPrice,
    	        @Param("maxPrice") Double maxPrice,
    	        @Param("status") Property.PropertyStatus status,
    	        @Param("type") Property.PropertyType type,
    	        Pageable pageable
    	);

	long countByPropertyType(PropertyType type);

	long countByPropertyStatus(PropertyStatus status);

}
