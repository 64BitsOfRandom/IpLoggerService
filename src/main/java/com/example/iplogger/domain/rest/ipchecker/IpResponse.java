package com.example.iplogger.domain.rest.ipchecker;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class IpResponse {
    private String browser;
    private String city;
    @Embedded
    private Coordinates coordinates;
    private String country;
    private String ip;
    private String isp;

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinates{
        private double latitude;
        private double longitude;
    }
}
