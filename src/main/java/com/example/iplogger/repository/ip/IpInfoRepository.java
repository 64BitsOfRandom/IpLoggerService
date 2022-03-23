package com.example.iplogger.repository.ip;

import com.example.iplogger.domain.persistance.IpData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpInfoRepository extends PagingAndSortingRepository<IpData, String> {
}
