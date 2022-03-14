package com.example.iplogger.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ShortLinkRecord {
    @Id
    private String shortLinkId;
    private String targetUrl;
    private int counter;
}
