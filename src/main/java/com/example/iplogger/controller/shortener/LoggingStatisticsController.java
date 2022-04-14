package com.example.iplogger.controller.shortener;

import com.example.iplogger.domain.persistance.LoggerStatisticsRecord;
import com.example.iplogger.domain.persistance.ShortLinkRecord;
import com.example.iplogger.domain.rest.logger.LoggerIdRequest;
import com.example.iplogger.domain.rest.logger.LoggerResponse;
import com.example.iplogger.domain.rest.logger.statistics.LoggerStatisticsResponse;
import com.example.iplogger.domain.suppliers.RandomCharSequenceSupplier;
import com.example.iplogger.repository.shortlink.ShortLinkRepository;
import com.example.iplogger.repository.statistics.LoggerStatisticsRecordsRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/statistics")
//@Operation(tags = "Сбор и учёт статистики"))
//@OpenAPIDefinition(info = "Сбор и учёт статистики")
//@OpenAPIDefinition(tags = {@Tag(name = "Сбор и учёт статистики", description = "My controller")})
public class LoggingStatisticsController {
    @Value("${application.host.url}")
    private String hostUrl;
    @Value("${application.paths.path-to-image}")
    private String pathToImage;
    private final RandomCharSequenceSupplier shortIdSupplier;
    private final ShortLinkRepository linkRepository;
    private final LoggerStatisticsRecordsRepository statisticsRecordsRepository;

    @Autowired
    public LoggingStatisticsController(RandomCharSequenceSupplier shortIdSupplier, ShortLinkRepository linkRepository, LoggerStatisticsRecordsRepository statisticsRecordsRepository) {
        this.shortIdSupplier = shortIdSupplier;
        this.linkRepository = linkRepository;
        this.statisticsRecordsRepository = statisticsRecordsRepository;
    }

    /**
     * @param id идентификатор логера
     * @return Возвращает общую информацию по логеру
     */
    @GetMapping("/{id}")
    @Tag(name = "Интерфейс для работы со статистикой логирования")
    @Operation(summary = "Получение общей статистики по идентификатору логера")
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
     * @return Возвращает детальную информацию по логеру
     */
    @GetMapping("/{id}/detailed")
    @Tag(name = "Интерфейс для работы со статистикой логирования")
    @Operation(summary = "Получение детальной статистики запросов по идентификатору логера")
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
     * Метод создаёт новый логер для URL
     * @return ShortLinkRecord - информация по записи о логере
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/create")
    @Tag(name = "Интерфейс для работы со статистикой логирования")
    @Operation(summary = "Создание логера")
    public LoggerResponse createLink(@RequestBody LoggerIdRequest request) {
        String generatedId = shortIdSupplier.get();
        String shortLink = hostUrl + "/link/" + generatedId;
        String statisticsLink = hostUrl + "/statistics/" + generatedId;
        ShortLinkRecord linkRecord = ShortLinkRecord.builder()
                .loggerId(generatedId)
                .originateUrl(request.getOriginateUrl())
                .shortLink(shortLink)
                .loggingImageUrl(shortLink + pathToImage)
                .statisticsUrl(statisticsLink)
                .detailedStatisticsUrl(statisticsLink + "/detailed")
                .build();
        linkRepository.save(linkRecord);
        return LoggerResponse.builder()
                .loggerId(generatedId)
                .originateUrl(request.getOriginateUrl())
                .shortLink(shortLink)
                .statisticsUrl(statisticsLink)
                .loggingImageUrl(linkRecord.getLoggingImageUrl())
                .detailedStatisticsUrl(linkRecord.getDetailedStatisticsUrl()).build();
    }
}
