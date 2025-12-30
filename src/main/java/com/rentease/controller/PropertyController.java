package com.rentease.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentease.dto.PropertyDTO;
import com.rentease.dto.PropertyHomeResponse;
import com.rentease.dto.PropertyWidgetResponse;
import com.rentease.entity.Property;
import com.rentease.entity.Property.PropertyStatus;
import com.rentease.entity.Property.PropertyType;
import com.rentease.entity.User;
import com.rentease.exceptions.CustomException;
import com.rentease.service.PropertyService;
import com.rentease.service.UserService;
import com.rentease.utils.ApiResponse;
import com.rentease.utils.JWT;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/property")
@CrossOrigin
public class PropertyController {

	public PropertyController() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private JWT jwtUtil;

    
    @PostMapping("/create")
    public ResponseEntity<?> createProperty(
            @RequestBody PropertyDTO dto,
            HttpServletRequest request) {
    	
    	// Extract token from cookie or header
        String token = jwtUtil.extractToken(request);                
        
        if (token == null) {
            return ResponseEntity
            		.status(HttpStatus.UNAUTHORIZED)
            		.body(new ApiResponse<>(
                            401,
                            "Missing Token",
                            request.getRequestURI(),
                            null
                    ));
        }
        
        Long advertiserId = jwtUtil.extractUserId(token);
        if (advertiserId == null) {
        	return ResponseEntity
            		.status(HttpStatus.UNAUTHORIZED)
            		.body(new ApiResponse<>(
                            401,
                            "Invalid Token",
                            request.getRequestURI(),
                            null
                    ));
        }

        Optional<User> advertiser = userService.getUserById(advertiserId);
        if (advertiser.isEmpty()) {
            throw new RuntimeException("Advertiser not found");
        }
        System.out.println(advertiser.toString());

        Property property =  propertyService.createProperty(dto, advertiser.get());
        
        ApiResponse<?> response = new ApiResponse<>(
                200,
                "Property Created Successfully",
                request.getRequestURI(),
                property
        );
        return ResponseEntity.ok(response);
    }
    
//    @PutMapping("/update/{id}")
//    public Property updateProperty(
//            @PathVariable Long id,
//            @RequestBody PropertyDTO dto,
//            @RequestParam Long advertiserId) {
//
//        return propertyService.updateProperty(id, dto, advertiserId);
//    }
    
    @PutMapping("/update/{propertyId}")
    public ResponseEntity<?> updateProperty(@PathVariable Long propertyId, 
                                            @RequestBody PropertyDTO dto,
                                            HttpServletRequest request) {

        Property property = propertyService.getPropertyById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setCountry(dto.getCountry());
        property.setCity(dto.getCity());
        property.setAddress(dto.getAddress());
        property.setPostalCode(dto.getPostalCode());

        property.setPropertyType(dto.getPropertyType());
        property.setPropertyStatus(dto.getPropertyStatus());
        property.setLeaseTerm(dto.getLeaseTerm());
        property.setMinLeaseMonths(dto.getMinLeaseMonths());
        property.setCurrency(dto.getCurrency());
        
        property.setPrice(dto.getPrice());
        property.setAvailableDate(dto.getAvailableDate());

       
        propertyService.updateProperty(propertyId, dto);
        
        ApiResponse<?> response = new ApiResponse<>(
                200,
                "Property Updated Successfully",
                request.getRequestURI(),
                property
        );

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProperty(
    		@PathVariable Long id, 
    		HttpServletRequest request) {
    	
    	
    	// Extract token from cookie or header
        String token = jwtUtil.extractToken(request);                
        
        if (token == null) {
            return ResponseEntity
            		.status(HttpStatus.UNAUTHORIZED)
            		.body(new ApiResponse<>(
                            401,
                            "Missing Token",
                            request.getRequestURI(),
                            null
                    ));
        }
        
        Long advertiserId = jwtUtil.extractUserId(token);
        if (advertiserId == null) {
        	return ResponseEntity
            		.status(HttpStatus.UNAUTHORIZED)
            		.body(new ApiResponse<>(
                            401,
                            "Invalid Token",
                            request.getRequestURI(),
                            null
                    ));
        }
        
    	Optional<Property> property = propertyService.getPropertyById(id);
    	
    	if (property.isEmpty()) {
    		throw new CustomException("Property not found");
    	}
        propertyService.deleteProperty(id, advertiserId);
        
        ApiResponse<?> response = new ApiResponse<>(
                200,
                "Property Deleted Successfully",
                request.getRequestURI(),
                null  
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public Optional<Property> getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }
    
    @GetMapping("/list")
    public Page<Property> listProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Property.PropertyStatus status,
            @RequestParam(required = false) Property.PropertyType type
    ) {
        return propertyService.listProperties(page, size, city, country, minPrice, maxPrice, status, type);
    }


    @GetMapping("/all")
    public List<Property> getAllActiveProperties() {
        return propertyService.getAllActiveProperties();
    }

    @GetMapping("/city/{city}")
    public List<Property> getByCity(@PathVariable String city) {
        return propertyService.getPropertiesByCity(city);
    }

    @GetMapping("/price-range")
    public List<Property> getByPriceRange(@RequestParam Double min, @RequestParam Double max) {
        return propertyService.getPropertiesByPriceRange(min, max);
    }
    
    
    @GetMapping("/widget-data")
    public PropertyWidgetResponse getWidgetData() throws IOException {

        PropertyWidgetResponse response = new PropertyWidgetResponse();

        // 1. Property Types
        List<PropertyWidgetResponse.TypeCount> typeCounts =
                Arrays.stream(PropertyType.values())
                        .map(type -> new PropertyWidgetResponse.TypeCount(
                                type.name(),
                                propertyService.countByType(type)
                        ))
                        .collect(Collectors.toList());
        response.setPropertyTypes(typeCounts);

        // 2. Status
        List<PropertyWidgetResponse.TypeCount> statusCounts =
                Arrays.stream(PropertyStatus.values())
                        .map(st -> new PropertyWidgetResponse.TypeCount(
                                st.name(),
                                propertyService.countByStatus(st)
                        ))
                        .collect(Collectors.toList());
        response.setStatuses(statusCounts);

        // 3. Amenities list (STATIC)
        response.setAmenities(Arrays.asList(
                "Elevator","Garden","Gym","Parking","Security","Swimming Pool",
                "WiFi","Disabled Access","Air Conditioning","Laundry","Heater"
        ));
        
        List<String> cityList = propertyService.getCityList();
    	response.setPropertyCities(cityList);

        return response;
    }
    
    
    @GetMapping("/home-data")
    public PropertyHomeResponse getHomeData() throws IOException {

    	PropertyHomeResponse response = new PropertyHomeResponse();

        // 1. Property Types
    	PropertyType[] propertyTypes = PropertyType.values();
    	List<PropertyType> propertyTypeList = Arrays.asList(propertyTypes);
    	
    	// 2. Property Cities 
    	
    	List<String> cityList = propertyService.getCityList();
    	response.setPropertyCities(cityList);
    	response.setPropertyTypes(propertyTypeList);
        

        // 2. Status
//        List<PropertyWidgetResponse.TypeCount> statusCounts =
//                Arrays.stream(PropertyStatus.values())
//                        .map(st -> new PropertyWidgetResponse.TypeCount(
//                                st.name(),
//                                propertyService.countByStatus(st)
//                        ))
//                        .collect(Collectors.toList());
//        response.setStatuses(statusCounts);
//
//        // 3. Amenities list (STATIC)
//        response.setAmenities(Arrays.asList(
//                "Elevator","Garden","Gym","Parking","Security","Swimming Pool",
//                "WiFi","Disabled Access","Air Conditioning","Laundry","Heater"
//        ));

        return response;
    }
}
