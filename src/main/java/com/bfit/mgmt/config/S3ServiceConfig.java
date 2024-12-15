package com.bfit.mgmt.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class S3ServiceConfig {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	@Value("${aws.s3.base-url}")
	private String s3BaseUrl;

	public String uploadFile(MultipartFile multipartFile, String memberId) {
		try {
			File file = convertMultipartFileToFile(multipartFile);
			String fileName = memberId + "_" + multipartFile.getOriginalFilename();
//			s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName)
//					.contentType(file.getContentType()).build(),
//					RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
			s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
			file.delete();
			log.info(fileName + "File Uploaded Successfully");
			return s3BaseUrl + "/" + fileName;
		} catch (AmazonS3Exception amazonS3Exception) {
			log.error("Access Denied", amazonS3Exception);
			throw new RuntimeException("Access Denied", amazonS3Exception);
		} catch (Exception e) {
			log.error("File upload failed", e);
			throw new RuntimeException("File upload failed", e);
		}
		
		
	}

	private File convertMultipartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
			fileOutputStream.write(file.getBytes());
		} catch (IOException e) {
			log.error("Error Converting MultipartFile to File", e);
			throw new RuntimeException("Error Converting MultipartFile to File", e);
		}
		return convertedFile;
	}

}
