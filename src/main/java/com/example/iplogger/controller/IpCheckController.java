package com.example.iplogger.controller;

import com.example.iplogger.domain.IpData;
import com.example.iplogger.domain.IpRequest;
import com.example.iplogger.domain.providers.ipWhoIsProvider.IpWhoIsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * на вход приходит IP, на выходе - данные от провайдера. Сейчас реализован ipwhois.
 * TODO: выделить сбор инфо по ip в отдельный сервис
 * TODO: сделать  интеграцию ipregistry.co (он точнее)
 * TODO: обеспечить модульность и переключаемость источника
 * TODO: сделать реализацию внутреннего хранилища ???
 */

@RestController
@RequestMapping("/ip-check")
public class IpCheckController {
    private static final String PROVIDER_URL = "http://ipwhois.app/json/";
    private final RestTemplate template;
    private final PagingAndSortingRepository<IpData, String> repo;

    @GetMapping
    public IpData getInfoByIp(@RequestBody IpRequest request) {
        String urlToApi = PROVIDER_URL.concat(request.getIp());
        ResponseEntity<IpWhoIsInfo> entity = template.getForEntity(urlToApi, IpWhoIsInfo.class);
        IpWhoIsInfo body = entity.getBody();
        if(body == null){
            throw new IllegalArgumentException("Inappropriate data received from geoIP provider");
        }
        //todo: IpWhoIsInfo -> IpData
//        return repo.save(body);
        return null;
    }

    @Autowired
    public IpCheckController(RestTemplate template, PagingAndSortingRepository<IpData, String> repo) {
        this.template = template;
        this.repo = repo;
    }
}
