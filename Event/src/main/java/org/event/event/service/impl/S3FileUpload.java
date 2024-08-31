package org.event.event.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.event.event.exceptions.S3UploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3FileUpload {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public S3FileUpload(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }



    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String filePath = "events/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata);
            amazonS3.putObject(request);
            return amazonS3.getUrl(bucketName, filePath).toString();
        } catch (IOException e) {
            throw new S3UploadException("Failed to read file for S3 upload", e);
        } catch (Exception e) {
            throw new S3UploadException("Failed to upload file to S3", e);
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "-" + originalFilename;
    }

}

