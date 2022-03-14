package com.example.iplogger.domain.providers.ipWhoIsProvider;

import lombok.Data;

@Data
public class IpWhoIsInfo {

    private String ip;
    private String success,
            type,
            continent,
            continent_code,
            country,
            country_code,
            country_flag,
            country_capital,
            country_phone,
            country_neighbours,
            region,
            city,
            latitude,
            longitude,
            asn,
            org,
            isp,
            timezone,
            timezone_name,
            timezone_dstOffset,
            timezone_gmtOffset,
            timezone_gmt,
            currency,
            currency_code,
            currency_symbol,
            currency_rates,
            currency_plural,
            completed_requests;
}
