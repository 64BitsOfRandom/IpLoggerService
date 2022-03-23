package com.example.iplogger.domain.persistance;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggerStatisticsRecord {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private LocalDateTime requestTime;

    private String loggerId,
            browser,
            ipAddress,
            isp,
            city,
            country;
    private double latitude, longitude;

    @PrePersist
    public void onCreate() {
        requestTime = LocalDateTime.now();
    }
}
