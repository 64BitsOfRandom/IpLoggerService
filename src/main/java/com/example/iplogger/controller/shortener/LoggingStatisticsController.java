package com.example.iplogger.controller.shortener;

import com.example.iplogger.domain.persistance.LoggerStatisticsRecord;
import com.example.iplogger.domain.persistance.ShortLinkRecord;
import com.example.iplogger.domain.rest.logger.LoggerIdRequest;
import com.example.iplogger.domain.rest.logger.LoggerResponse;
import com.example.iplogger.domain.rest.logger.statistics.LoggerStatisticsResponse;
import com.example.iplogger.domain.suppliers.RandomCharSequenceSupplier;
import com.example.iplogger.repository.shortlink.ShortLinkRepository;
import com.example.iplogger.repository.statistics.LoggerStatisticsRecordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/statistics")
public class LoggingStatisticsController {
    @Value("${application.host.url}")
    private String hostUrl;
    private RandomCharSequenceSupplier shortIdSupplier;
    private ShortLinkRepository linkRepository;
    private LoggerStatisticsRecordsRepository statisticsRecordsRepository;

    @Autowired
    public LoggingStatisticsController(RandomCharSequenceSupplier shortIdSupplier, ShortLinkRepository linkRepository, LoggerStatisticsRecordsRepository statisticsRecordsRepository) {
        this.shortIdSupplier = shortIdSupplier;
        this.linkRepository = linkRepository;
        this.statisticsRecordsRepository = statisticsRecordsRepository;
    }

    /**
     * @param id идентификатор логера
     * @return Возвращает общую информацию по логгеру
     */
    @GetMapping("/{id}")
    public ShortLinkRecord getSummeryStatistics(@PathVariable String id, HttpServletResponse response) throws IOException {
        Optional<ShortLinkRecord> record = linkRepository.findById(id);
        if (record.isEmpty()) {
            log.info("Requested logger {} does not exist", id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return record.get();
    }

    /**
     * @param id идентификатор логера
     * @return Возвращает детальную информацию по логгеру
     */
    @GetMapping("/{id}/detailed")
    public LoggerStatisticsResponse getDetailedStatistics(@PathVariable String id, HttpServletResponse response)  throws IOException {
        List<LoggerStatisticsRecord> statisticsRecords = statisticsRecordsRepository.findAllByLoggerIdOrderByRequestTime(id);
        if(statisticsRecords.isEmpty()){
            log.info("Unable to retrieve info related to {}", id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return LoggerStatisticsResponse.getLoggerStatisticsResponse(statisticsRecords);
    }

    /**
     * Метод создаёт новый логгер для URL
     * @return ShortLinkRecord - информация по записи о логгере
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/create")
    public LoggerResponse createLink(@RequestBody LoggerIdRequest request) {
        String generatedId = shortIdSupplier.get();
        String shortLink = hostUrl + "/link/" + generatedId;
        String statisticsLink = hostUrl + "/statistics/" + generatedId;
        ShortLinkRecord linkRecord = ShortLinkRecord.builder()
                .loggerId(generatedId)
                .originateUrl(request.getOriginateUrl())
                .shortLink(shortLink)
                .statisticsUrl(statisticsLink)
                .detailedStatisticsUrl(statisticsLink + "/detailed")
                .build();
        linkRepository.save(linkRecord);
        return LoggerResponse.builder()
                .loggerId(generatedId)
                .originateUrl(request.getOriginateUrl())
                .shortLink(shortLink)
                .statisticsUrl(statisticsLink)
                .detailedStatisticsUrl(statisticsLink + "/detailed").build();
    }
}
