package com.bfit.mgmt.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

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

	public String uploadFile(MultipartFile multipartFile) {
		UUID id = UUID.randomUUID();
		String fileName = id + "_" + multipartFile.getOriginalFilename();
		try {
			File file = convertMultipartFileToFile(multipartFile);
			s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
			file.delete();
            log.info("{} File Uploaded Successfully", fileName);
		} catch (AmazonS3Exception amazonS3Exception) {
			log.error("Access Denied", amazonS3Exception);
		} catch (Exception e) {
			log.error("File upload failed", e);
		}
		return s3BaseUrl + "/" + fileName;

	}

	private File convertMultipartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
			fileOutputStream.write(file.getBytes());
		} catch (IOException e) {
			log.error("Error Converting MultipartFile to File", e);
		}
		return convertedFile;
	}

	public byte[] getFile(String fileName) {
		try (S3Object s3Object = s3Client.getObject(bucketName, fileName);
				S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
			return IOUtils.toByteArray(inputStream);
		} catch (AmazonS3Exception e) {
			log.error("Amazon S3 error while fetching file '{}': {}", fileName, e.getMessage(), e);
		} catch (IOException e) {
			log.error("IO error while reading file from S3: {}", fileName, e);
		}
		return null;
	}

	public void deleteFile(String fileUrl) {
		try {
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			s3Client.deleteObject(bucketName, fileName);
			log.info("Existing profile deleted succeffully");
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
		}
	}

}
