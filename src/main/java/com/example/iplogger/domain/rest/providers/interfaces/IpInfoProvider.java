package com.example.iplogger.domain.rest.providers.interfaces;

import com.example.iplogger.domain.rest.ipchecker.IpResponse;

/**
 * Данный интерфейс позволяет приводить данные от разных провайдеров к общему виду
 */
public interface IpInfoProvider {
    IpResponse getIpInfo();
}
