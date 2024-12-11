package com.bfit.mgmt.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceConfig {
	
	@Autowired
	private S3Client s3Client;


	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	@Value("${aws.s3.base-url}")
	private String s3BaseUrl;

	public String uploadFile(MultipartFile file, String memberId) {
		String fileName = memberId + "_" + file.getOriginalFilename();
		s3Client.putObject(
				PutObjectRequest.builder().bucket(bucketName).key(fileName).contentType(file.getContentType()).build(),
				Paths.get(file.getOriginalFilename()));
		return s3BaseUrl + "/" + fileName;

	}

}
