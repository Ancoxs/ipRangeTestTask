package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class IPRanges {
    @JsonProperty("syncToken")
    String syncToken;
    @JsonProperty("createDate")
    String createDate;
    @JsonProperty("prefixes")
    List<Prefix> prefixes;
}
