package com.example.iplogger.repository.shortlink;

import com.example.iplogger.domain.persistance.ShortLinkRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortLinkRepository extends CrudRepository<ShortLinkRecord, String> {

    default boolean incrementCountById(String id) {
        Optional<ShortLinkRecord> record = findById(id);
        if (record.isEmpty()) {
            return false;
        }
        ShortLinkRecord shortLinkRecord = record.get();
        shortLinkRecord.setCounter(shortLinkRecord.getCounter() + 1);
        save(shortLinkRecord);
        return true;
    }
}
