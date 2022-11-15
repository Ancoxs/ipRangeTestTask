package com.example.demo.model.mapper;

import com.example.demo.model.IPRanges;
import com.example.demo.model.Prefix;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IPRangesMapper {

    public List<Prefix> toPrefixes(IPRanges ipRanges){
        return ipRanges.getPrefixes();
    }

}
