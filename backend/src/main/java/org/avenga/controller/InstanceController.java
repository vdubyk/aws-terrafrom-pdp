package org.avenga.controller;

import lombok.RequiredArgsConstructor;
import org.avenga.service.InstanceMetadataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instance")
public class InstanceController {

    private final InstanceMetadataService instanceMetadataService;
    @GetMapping("/health-check")
    public String getHealthCheck() {
        return new String("OK") ;
    }
    @GetMapping("/public-ip")
    public String getPublicIp() {
        return instanceMetadataService.getPublicIp();
    }

    @GetMapping("/private-ip")
    public String getPrivateIp() {
        return instanceMetadataService.getPrivateIp();
    }
}
