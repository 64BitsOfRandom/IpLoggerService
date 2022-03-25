package com.example.iplogger.controller.shortener;

import com.example.iplogger.domain.persistance.LoggerStatisticsRecord;
import com.example.iplogger.domain.persistance.ShortLinkRecord;
import com.example.iplogger.domain.rest.ipchecker.IpResponse;
import com.example.iplogger.repository.shortlink.ShortLinkRepository;
import com.example.iplogger.repository.statistics.LoggerStatisticsRecordsRepository;
import com.example.iplogger.service.IpInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Обрабатывает созданные короткие ссылки и обновляет счётчики.
 * Дата последнего обращения логируется средствами JPA @see ShortLinkRepository
 */
@Slf4j
@RestController
@RequestMapping("/link")
public class LoggingController {
    private static final String EMPTY_URL = "";

    @Value("${application.paths.path-to-image}")
    private String pathToImage;
    private final IpInfoService ipInfoService;
    private final ShortLinkRepository linkRepository;
    private final LoggerStatisticsRecordsRepository statisticsRecordsRepository;

    @Autowired
    public LoggingController(IpInfoService ipInfoService, ShortLinkRepository linkRepository, LoggerStatisticsRecordsRepository statisticsRecordsRepository) {
        this.ipInfoService = ipInfoService;
        this.linkRepository = linkRepository;
        this.statisticsRecordsRepository = statisticsRecordsRepository;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{id}")
    public void logAndRedirect(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl = logRequestAndGetRedirectUrl(id, request);
        if (redirectUrl.isEmpty()) {
            log.info("Requested logger {} does not exist. Request source: {}", id, request.getRemoteAddr());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.sendRedirect(redirectUrl);
    }

    @GetMapping(value = "/{id}/image.jpg", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable String id, HttpServletRequest request) throws IOException {
        InputStream input = getClass().getResourceAsStream(pathToImage);
        if (input == null) {
            log.error("Unable to retrieve image resource {}; check resource availability", pathToImage);
        }
        logRequestAndGetRedirectUrl(id, request);
        return input == null ? null : input.readAllBytes();
    }

    private String logRequestAndGetRedirectUrl(String id, HttpServletRequest request) {
        Optional<ShortLinkRecord> link = linkRepository.findById(id);
        String ipAddress = request.getRemoteAddr();
        IpResponse invokersInfo = ipInfoService.getInfoByIp(ipAddress);
        if (link.isEmpty()) {
            log.warn("Attempt to invoke logger with non-existing id");
            return EMPTY_URL;
        }
        ShortLinkRecord linkRecord = link.get();
        boolean incSuccessful = linkRepository.incrementCountById(id);
        if (!incSuccessful) {
            log.warn("Update logger {} info failed", id);
        }
        LoggerStatisticsRecord statisticsRecord = LoggerStatisticsRecord.builder().loggerId(id)
                .ipAddress(ipAddress)
                .isp(invokersInfo.getIsp())
                .city(invokersInfo.getCity())
                .country(invokersInfo.getCountry())
                .latitude(invokersInfo.getCoordinates().getLatitude())
                .longitude(invokersInfo.getCoordinates().getLongitude()).build();
        statisticsRecordsRepository.save(statisticsRecord);
        return linkRecord.getOriginateUrl();
    }

}
