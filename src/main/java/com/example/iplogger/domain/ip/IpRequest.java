package com.example.iplogger.domain.ip;

import lombok.Data;

@Data
public class IpRequest{
    private String ip;
    private String provider;
}
