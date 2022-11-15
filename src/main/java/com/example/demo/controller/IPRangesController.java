package com.example.demo.controller;

import com.example.demo.domainValue.ValidRegions;
import com.example.demo.model.IPRanges;
import com.example.demo.model.Prefix;
import com.example.demo.model.mapper.IPRangesMapper;
import com.example.demo.service.AWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@RestController
@RequestMapping("v1/ipranges")
public class IPRangesController {

    @Autowired
    AWSService awsService;
    @Autowired
    IPRangesMapper ipRangesMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public Flux<String> findIPRanges(@RequestParam ValidRegions validRegion){
        return awsService.getIpRanges()
                .map(ipRangesMapper::toPrefixes)
                .flatMapIterable(IPRanges -> IPRanges)
                .filter(((Function<Prefix, Boolean>) Prefix -> Prefix.getRegion().startsWith(validRegion.name().toLowerCase()))::apply)
                .map(Prefix::getIpPrefix)
                .map(prefix -> prefix.concat(System.lineSeparator()));
    }

}
