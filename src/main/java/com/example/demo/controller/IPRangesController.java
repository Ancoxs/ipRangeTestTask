package com.example.demo.controller;

import com.example.demo.domainValue.ValidRegions;
import com.example.demo.model.IPRanges;
import com.example.demo.model.Prefix;
import com.example.demo.model.mapper.IPRangesMapper;
import com.example.demo.service.AWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Flux;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("v1/ipranges")
public class IPRangesController {

    @Autowired
    AWSService awsService;
    @Autowired
    IPRangesMapper ipRangesMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public Flux<String> findIPRanges(@RequestParam ValidRegions validRegion) {
        Flux<String> result;
        if (validRegion.name().equals("ALL")) {
            result = awsService.getIpRanges()
                    .map(ipRangesMapper::toPrefixes)
                    .flatMapIterable(IPRanges -> IPRanges)
                    .map(Prefix::getIpPrefix)
                    .map(prefix -> prefix.concat(System.lineSeparator()));
        } else{
            result = awsService.getIpRanges()
                    .map(ipRangesMapper::toPrefixes)
                    .flatMapIterable(IPRanges -> IPRanges)
                    .filter(((Function<Prefix, Boolean>) Prefix -> Prefix.getRegion().startsWith(validRegion.name().toLowerCase()))::apply)
                    .map(Prefix::getIpPrefix)
                    .map(prefix -> prefix.concat(System.lineSeparator()));
        }
        return result;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("The specified region is not a valid one",
                name, type, value);
        return message;
    }

}
