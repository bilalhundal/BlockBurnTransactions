package com.bilal.hundal1.services;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadToS3(String key, String data) {
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(System.getenv("BUCKET_NAME"))
                .key(key)
                .build(), RequestBody.fromString(data));
    	System.out.println("Data is stored");
    }
}
