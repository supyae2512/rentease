package com.rentease.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentease.entity.Property;
import com.rentease.entity.PropertyImage;
import com.rentease.exceptions.CustomException;
import com.rentease.service.PropertyImageService;
import com.rentease.service.PropertyService;
import com.rentease.utils.AWSHelper;
import com.rentease.utils.ApiResponse;
import com.rentease.utils.GoogleHelper;

import jakarta.servlet.http.HttpServletRequest;

import com.rentease.utils.Helper;

@RestController
@RequestMapping("/api/property-images")
@CrossOrigin
public class PropertyImageController {

	@Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyImageService propertyImageService;
    
    @Autowired
    private GoogleHelper googleHelper;
    
    @Autowired
    private AWSHelper awsHelper;
    

    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadImage(@RequestParam("propertyId") Long propertyId,
                                     @RequestParam("file") MultipartFile file,
                                     HttpServletRequest request) throws IOException {

        Optional<Property> propertyOpt = propertyService.getPropertyById(propertyId);
        if (propertyOpt.isEmpty()) {
            throw new RuntimeException("Property not found!");
        }

        Property property = propertyOpt.get();
       
        
        // Create a unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        
        System.out.println(fileName);
        
         String publicUrl = googleHelper.uploadFileToGcs(fileName, file);        
       
        // Save metadata in DB
        PropertyImage image = new PropertyImage();
        image.setProperty(property);
        image.setFileName(fileName);
        image.setImageUrl(publicUrl);
        image.setContentType(file.getContentType());
//        
//         // return propertyImageService.addImage(image);
//        
        // Convert entity to JSON string
        PropertyImage propertyImage = propertyImageService.addImage(image);
        
        System.out.println(propertyImage);
           
        ApiResponse<?> response = new ApiResponse<>(
                200,
                "Image Upload Successfully",
                request.getRequestURI(),
                propertyImage
        );

        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/{propertyId}")
    public List<PropertyImage> getImages(@PathVariable Long propertyId) {
        Optional<Property> property = propertyService.getPropertyById(propertyId);
        if (property.isEmpty()) {
            throw new RuntimeException("Property not found!");
        }
        return propertyImageService.getImagesByProperty(property.get());
    }
    
    @GetMapping("/get/{propertyImageId}")
    public PropertyImage getImage(@PathVariable Long propertyImageId) {
    	Optional<PropertyImage> propertyImage = propertyImageService.findById(propertyImageId);
    	if (propertyImage.isEmpty()) {
    		throw new RuntimeException("Property Image not found!");
    	}
    	return propertyImage.get();
    }

    @DeleteMapping("/{propertyImageId}")
    public  ResponseEntity<ApiResponse<?>> deleteImage(
    		@PathVariable Long propertyImageId,
    		HttpServletRequest request) throws IOException {
    	
    	Optional<PropertyImage> propertyImage = propertyImageService.findById(propertyImageId);
    	if (propertyImage.isEmpty()) {
    		throw new CustomException("Property Image not found!");
    	}
    	
    	// Get Image Object Name;
    	String publicUrl = propertyImage.get().getImageUrl();
    	
    	// Get Object Path; remove domain and bucket name;
    	// GCS version; 
//    	String prefix = "https://storage.cloud.google.com/" + bucketName + "/";
//        String objectName = publicUrl.replace(prefix, "");    	    	
//    	Boolean isDeleted = googleHelper.deleteFileFromGcs(objectName);
    	
    	
    	// AWS s3 version;
    	String prefix = "https://amzn-s3-rentease.s3.amazonaws.com/";
        String objectName = publicUrl.replace(prefix, "");    	    	
    	Boolean isDeleted = awsHelper.deleteFileFromS3(objectName);
    	
    	
    	if (!isDeleted) {
    		throw new RuntimeException("Failed to delete the image!");
    	}
    	
        propertyImageService.deleteImage(propertyImageId);
        
        ApiResponse<?> response = new ApiResponse<>(
                200,
                "Image Delete Successfully",
                request.getRequestURI(),
                null  
        );

        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/upload-multiple")
    public ResponseEntity<ApiResponse<?>> uploadMultipleImages(
            @RequestParam("propertyId") Long propertyId,
            @RequestParam("files") List<MultipartFile> files,
            HttpServletRequest request) throws IOException {

        Optional<Property> propertyOpt = propertyService.getPropertyById(propertyId);
        if (propertyOpt.isEmpty()) {
            throw new RuntimeException("Property not found!");
        }

        Property property = propertyOpt.get();

        List<PropertyImage> savedImages = new ArrayList<>();

        for (MultipartFile file : files) { 
        	
        	if (file.getSize() > 0) {
        		
        		// Create unique filename
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                System.out.println(fileName);

                // Upload to GCS
                // String publicUrl = googleHelper.uploadFileToGcs(fileName, file);
                String publicUrl = awsHelper.uploadToS3(file, fileName);
                

                // Save DB metadata
                PropertyImage img = new PropertyImage();
                img.setProperty(property);
                img.setFileName(fileName);
                img.setImageUrl(publicUrl);
                img.setContentType(file.getContentType());

                savedImages.add(propertyImageService.addImage(img));
        	}
            
            
        }

        ApiResponse<?> response = new ApiResponse<>(
                200,
                "Images uploaded successfully",
                request.getRequestURI(),
                savedImages
        );

        return ResponseEntity.ok(response);
    }


}
