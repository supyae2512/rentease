package com.rentease.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.threeten.bp.LocalDate;

import com.google.cloud.storage.BlobId;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
public class AWSHelper {

	private  String accessKey;
    private  String secretKey;
    private  Region awsRegion;
    private  String bucketName;
    private S3Client s3Client;
    // private final S3Presigner presigner;
    
	
    public AWSHelper(
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.bucket}") String bucketName
    ) {
		
		// System.out.print("Access key " + accessKey);
		
		this.bucketName = bucketName;
		this.awsRegion = Region.of(region);
		
		AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        
//        this.presigner = S3Presigner.builder()
//                .region(awsRegion)
//                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//                .build();

	}
	
	public String uploadToS3(MultipartFile file, String fileName) throws IOException {

		System.out.println("Bucket name :" + this.bucketName);
		System.out.println("Region name :" + this.awsRegion);
		
		String today = LocalDate.now().toString();
		
		String awsPath = String.format("%s/%s", today, fileName);
		
		PutObjectRequest putReq = PutObjectRequest.builder()
		        .bucket(bucketName)
		        .key(awsPath)
		        .contentType(file.getContentType())
		        .build();
		
		

        s3Client.putObject(putReq, RequestBody.fromBytes(file.getBytes()));
        
        // S3 public url for now. Later I will use CDN for access 
        return "https://" + bucketName + ".s3.amazonaws.com/" + awsPath;
        

//		    GetObjectRequest getReq = GetObjectRequest.builder()
//		        .bucket(bucketName)
//		        .key(fileName)
//		        .build();
//
//		    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//		        .signatureDuration(java.time.Duration.ofHours(1)) // URL this valid for 1 hour
//		        .getObjectRequest(getReq)
//		        .build();
//
//		    PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
//
//		    return presignedRequest.url().toString();
    }
	
	public Boolean deleteFileFromS3(String awsPath) {
	    
		DeleteObjectRequest deleteReq = DeleteObjectRequest.builder()
	            .bucket(bucketName)
	            .key(awsPath)   // eg. "2025-01-09/jp-002.jpeg"
	            .build();

		DeleteObjectResponse deleted = s3Client.deleteObject(deleteReq);
	   
	    if (deleted.sdkHttpResponse().isSuccessful()) {
	    	return true;
	    } else {
	    	return false;
	    }
	}

}
