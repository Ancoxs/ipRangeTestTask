package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Prefix {
    @JsonProperty("ip_prefix")
    @JsonAlias("ipv6_prefix")
    String ipPrefix;
    @JsonProperty("region")
    String region;
    @JsonProperty("service")
    String service;
    @JsonProperty("network_border_group")
    String networkBorderGroup;
}
