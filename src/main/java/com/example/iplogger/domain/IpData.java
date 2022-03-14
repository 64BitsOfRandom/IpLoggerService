package com.example.iplogger.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Бин для наполнения внутреннего хранилища
 */
@Entity
public class IpData {
    @Id
    private Timestamp timestamp;
    private String remoteIpAddress;
    private String isp;
    private String country;
    private String city;
    private String browser;
}
