package com.example.iplogger.service;

import com.example.iplogger.domain.rest.ipchecker.IpResponse;
import com.example.iplogger.domain.rest.providers.ipWhoIsProvider.IpWhoIsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class IpInfoService {
    private final RestTemplate template;
    @Value("${application.provider.url: http://localhost:8080}")
    private String providerUrl;

    public IpResponse getInfoByIp(String targetAddress) {
        String urlToApi = providerUrl.concat(targetAddress);
        ResponseEntity<IpWhoIsInfo> entity = template.getForEntity(urlToApi, IpWhoIsInfo.class);
        IpWhoIsInfo body = entity.getBody();
        if(body == null){
            throw new IllegalArgumentException("Inappropriate data received from geoIP provider");
        }
        return body.getIpInfo();
    }


    @Autowired
    public IpInfoService(RestTemplate template) {
        this.template = template;
    }
}
