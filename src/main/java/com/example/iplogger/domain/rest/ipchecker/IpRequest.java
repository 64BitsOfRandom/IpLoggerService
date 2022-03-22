package com.example.iplogger.domain.rest.ipchecker;

import lombok.Data;

@Data
public class IpRequest{
    private String ip;
    private String provider;
}
