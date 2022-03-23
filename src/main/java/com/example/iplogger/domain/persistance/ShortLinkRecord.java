package com.example.iplogger.domain.persistance;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkRecord {
    @Id
    private String loggerId;
    private String originateUrl;
    private String shortLink;
    private String statisticsUrl;

    private int counter;

    @Column(updatable = false)
    private LocalDateTime created;
    private LocalDateTime lastRequest;

    @PrePersist
    void onCreate(){
        created = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate(){
        lastRequest = LocalDateTime.now();
    }
}
