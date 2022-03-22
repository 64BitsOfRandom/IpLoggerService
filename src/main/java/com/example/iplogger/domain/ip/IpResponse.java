package com.example.iplogger.domain.ip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IpResponse {
    private String browser;
    private String city;
    private Coordinates coordinates;
    private String country;
    private String ip;
    private String isp;

    @Data
    @AllArgsConstructor
    public static class Coordinates{
        private double latitude;
        private double longitude;
    }
}
