package com.example.iplogger.domain.persistance;

import com.example.iplogger.domain.rest.ipchecker.IpResponse;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Бин для наполнения внутреннего хранилища
 */
@Entity
@Table(name = "ip_requests")
@NoArgsConstructor
public class IpData {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private long id;
    @Column(updatable = false)
    private LocalDateTime created;
    private String originateIp;
    @Embedded
    private IpResponse response;

    @PrePersist
    void beforeSave(){
        created = LocalDateTime.now();
    }

    public IpData(IpResponse response, String originateIp) {
        this.response = response;
        this.originateIp = originateIp;
    }
}
