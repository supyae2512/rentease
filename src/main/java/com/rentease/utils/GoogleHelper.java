package com.rentease.utils;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.threeten.bp.LocalDate;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Component
public class GoogleHelper {
	
	GoogleCredentials credentials;
	Storage storage;
	
    @Autowired
    private Helper helper;
	
	@Value("${spring.cloud.gcp.project-id}")
	private String projectId;
	
    @Value("${gcp.bucket.name}")
    private String bucketName;
	
	public GoogleHelper() throws IOException {
		credentials = GoogleCredentials.fromStream(
	            new ClassPathResource("su-gcs-gcp-1470198650891-f8de93367193.json").getInputStream()
			    ).createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));

		storage = StorageOptions.newBuilder()
	            .setCredentials(credentials)
	            .setProjectId(projectId)
	            .build()
	            .getService();
	}
	
	
	public String uploadFileToGcs(String fileName, MultipartFile file) throws IOException {
		
		String today = LocalDate.now().toString();
		
		String gcsPath = String.format("%s/%s", today, fileName);
		
//		byte[] bytes = file.getBytes();
//        bytes = helper.compressMedia(bytes);
		
		// Upload the file
		BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, gcsPath)
	            .setContentType(file.getContentType())
	            .build();
        
        storage.create(blobInfo, file.getBytes());
        
        // Return public URL 
//      return String.format("https://storage.cloud.google.com/%s/%s", bucketName, bytes);
        return String.format("https://storage.cloud.google.com/%s/%s", bucketName, gcsPath);
	}


	public Boolean deleteFileFromGcs(String objectName) {
	    BlobId blobId = BlobId.of(bucketName, objectName);
	   
	    boolean deleted = storage.delete(blobId);

	    if (!deleted) {
	        return false;
	    }
	    
	    return true;
	}

}
