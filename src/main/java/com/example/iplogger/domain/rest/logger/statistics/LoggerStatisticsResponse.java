package com.example.iplogger.domain.rest.logger.statistics;

import com.example.iplogger.domain.persistance.LoggerStatisticsRecord;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerStatisticsResponse {
    private List<LoggerStatisticsRecord> records;

    public static LoggerStatisticsResponse getLoggerStatisticsResponse(List<LoggerStatisticsRecord> records){
        return new LoggerStatisticsResponse(records);
    }
}
