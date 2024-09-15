package org.avenga.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public String uploadFile(String bucketName, String key, String content) {
        try {
            InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, content.length()));

            return key;
        } catch (S3Exception e) {
            throw new RuntimeException("Error occurred while uploading file to S3", e);
        }
    }
}
