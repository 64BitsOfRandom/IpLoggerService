package com.example.iplogger.domain.providers.interfaces;

import com.example.iplogger.domain.ip.IpResponse;

/**
 * Данный интерфейс позволяет приводить данные от разных провайдеров к общему виду
 */
public interface IpInfoProvider {
    IpResponse getIpInfo();
}
