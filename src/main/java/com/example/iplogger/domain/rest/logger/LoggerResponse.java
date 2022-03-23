package com.example.iplogger.domain.rest.logger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoggerResponse {
    private String loggerId;
    private String originateUrl;
    private String shortLink;
    private String statisticsUrl;
    private String detailedStatisticsUrl;
}
