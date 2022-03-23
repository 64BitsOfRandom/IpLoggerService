package com.example.iplogger.controller.shortener;

import com.example.iplogger.domain.persistance.LoggerStatisticsRecord;
import com.example.iplogger.domain.persistance.ShortLinkRecord;
import com.example.iplogger.domain.rest.ipchecker.IpResponse;
import com.example.iplogger.repository.shortlink.ShortLinkRepository;
import com.example.iplogger.repository.statistics.LoggerStatisticsRecordsRepository;
import com.example.iplogger.service.IpInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Обрабатывает созданные короткие ссылки и обновляет счётчики.
 * Дата последнего обращения логируется средствами JPA @see ShortLinkRepository
 */
@Slf4j
@RestController
@RequestMapping("/link")
@AllArgsConstructor
public class LoggingController {
    private IpInfoService ipInfoService;
    private ShortLinkRepository linkRepository;
    private LoggerStatisticsRecordsRepository statisticsRecordsRepository;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/{id}")
    public void logAndRedirect(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<ShortLinkRecord> link = linkRepository.findById(id);
        String ipAddress = request.getRemoteAddr();
        IpResponse invokersInfo = ipInfoService.getInfoByIp(ipAddress);
        if (link.isEmpty()) {
            log.info("Requested logger {} does not exist. Request source: {}", id, request.getRemoteAddr());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
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
        response.sendRedirect(linkRecord.getOriginateUrl());
    }

}
