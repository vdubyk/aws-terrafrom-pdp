package org.avenga.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class InstanceMetadataService {

    private static final String METADATA_URL = "http://169.254.169.254/latest/meta-data/";

    public String getPublicIp() {
        return fetchMetadata("public-ipv4");
    }

    public String getPrivateIp() {
        return fetchMetadata("local-ipv4");
    }

    private String fetchMetadata(String endpoint) {
        try {
            URL url = new URL(METADATA_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return in.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch instance metadata", e);
        }
    }
}