package com.example.iplogger.repository;

import com.example.iplogger.domain.IpData;
import com.example.iplogger.domain.providers.ipWhoIsProvider.IpWhoIsInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpInfoRepository extends PagingAndSortingRepository<IpData, String> {
}
