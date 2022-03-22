package com.example.iplogger.controller;

import com.example.iplogger.domain.persistance.IpData;
import com.example.iplogger.domain.rest.ipchecker.IpRequest;
import com.example.iplogger.domain.rest.ipchecker.IpResponse;
import com.example.iplogger.service.IpInfoService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * на вход приходит IP, на выходе - данные от провайдера. Сейчас реализован ipwhois.
 * TODO: сделать  интеграцию ipregistry.co (он точнее)
 * TODO: обеспечить модульность и переключаемость источника
 * TODO: может кэш?
 */

@Slf4j
@RestController
@RequestMapping("/ip-check")
public class IpCheckController {
    //TODO: поменять репу, продумать как сохранять
//    private final PagingAndSortingRepository<IpData, String> repo;
    private final IpInfoService ipInfoService;

    @GetMapping
    public IpResponse getInfoByIp(@RequestBody IpRequest request, @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        log.info("Recieved request for IP: {}", request);
        IpResponse response = ipInfoService.getInfoByIp(request.getIp());
        response.setBrowser(getBrowserByUserAgent(userAgent));
        //TODO: сохранить в БД
//        repo.save(ipData);
        return response;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/me")
    public IpResponse getOwnIpData(HttpServletRequest req, @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        String remoteAddr = req.getRemoteAddr();
        log.info("Recieved request for IP: {}", remoteAddr);
        IpResponse infoByIp = ipInfoService.getInfoByIp(remoteAddr);
        infoByIp.setBrowser(getBrowserByUserAgent(userAgent));
//        repo.save(infoByIp);
        return infoByIp;
    }

    private String getBrowserByUserAgent(String userAgentHeader) {
        if (!StringUtils.hasLength(userAgentHeader)){
            log.info("User-Agent Header is Empty: no info provided");
            return "NOT_DEFINED";
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentHeader);
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    @Autowired
    public IpCheckController(IpInfoService ipInfoService, PagingAndSortingRepository<IpData, String> repo) {
        this.ipInfoService = ipInfoService;
//        this.repo = repo;
    }
}
