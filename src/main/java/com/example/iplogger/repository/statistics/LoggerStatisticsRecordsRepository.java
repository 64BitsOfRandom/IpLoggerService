package com.example.iplogger.repository.statistics;

import com.example.iplogger.domain.persistance.LoggerStatisticsRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggerStatisticsRecordsRepository extends CrudRepository<LoggerStatisticsRecord, Long> {

    List<LoggerStatisticsRecord> findAllByLoggerIdOrderByRequestTime(String loggerId);

}
