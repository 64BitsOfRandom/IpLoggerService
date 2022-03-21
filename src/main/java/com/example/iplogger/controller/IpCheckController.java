package com.example.iplogger.controller;

import com.example.iplogger.domain.IpData;
import com.example.iplogger.domain.IpRequest;
import com.example.iplogger.service.IpInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * на вход приходит IP, на выходе - данные от провайдера. Сейчас реализован ipwhois.
 * TODO: сделать  интеграцию ipregistry.co (он точнее)
 * TODO: обеспечить модульность и переключаемость источника
 * TODO: сделать реализацию внутреннего хранилища ???
 * TODO: может кэш?
 */

@Slf4j
@RestController
@RequestMapping("/ip-check")
public class IpCheckController {
    private final PagingAndSortingRepository<IpData, String> repo;
    private final IpInfoService ipInfoService;

    @GetMapping
    public IpData getInfoByIp(@RequestBody IpRequest request) {
        log.info("Recieved request for IP: {}", request);
        IpData ipData = ipInfoService.getInfoByIp(request.getIp());
        repo.save(ipData);
        return ipData;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/me")
        public IpData getOwnIpData(HttpServletRequest req){
        String remoteAddr = req.getRemoteAddr();
        IpData infoByIp = ipInfoService.getInfoByIp(remoteAddr);
        return repo.save(infoByIp);
    }

    @Autowired
    public IpCheckController(IpInfoService ipInfoService, PagingAndSortingRepository<IpData, String> repo) {
        this.ipInfoService = ipInfoService;
        this.repo = repo;
    }
}
