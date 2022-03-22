package com.example.iplogger.domain.providers.ipWhoIsProvider;

import com.example.iplogger.domain.ip.IpResponse;
import com.example.iplogger.domain.providers.interfaces.IpInfoProvider;
import lombok.Data;

@Data
public class IpWhoIsInfo implements IpInfoProvider {
    private double completed_requests,
            currency_rates,
            latitude,
            longitude,
            timezone_gmtOffset,
            timezone_dstOffset;
    private String ip,
            success,
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
            asn,
            org,
            isp,
            timezone,
            timezone_name,
            timezone_gmt,
            currency,
            currency_code,
            currency_symbol,
            currency_plural;

    @Override
    public IpResponse getIpInfo() {
        return IpResponse.builder()
                .city(this.city)
                .country(this.country)
                .coordinates(new IpResponse.Coordinates(this.latitude, this.longitude))
                .ip(this.ip)
                .isp(this.isp)
                .build();
    }
}
