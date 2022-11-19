package com.example.demo.controller;

import com.example.demo.domainValue.ValidRegions;
import com.example.demo.model.Prefix;
import com.example.demo.model.mapper.IPRangesMapper;
import com.example.demo.service.AWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@RestController
@RequestMapping("/v1/ipranges")
public class IPRangesController {

    @Autowired
    AWSService awsService;
    @Autowired
    IPRangesMapper ipRangesMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public @ResponseBody Flux<String> findIPRanges(@RequestParam ValidRegions validRegion) {
        Flux<String> result;
        if (validRegion.name().equals("ALL")) {
            result = awsService.getIpRanges()
                    .map(ipRangesMapper::toPrefixes)
                    .flatMapIterable(prefixes -> prefixes)
                    .map(Prefix::getIpPrefix)
                    .map(prefix -> prefix.concat(System.lineSeparator()));
        } else{
            result = awsService.getIpRanges()
                    .map(ipRangesMapper::toPrefixes)
                    .flatMapIterable(prefixes -> prefixes)
                    .filter(((Function<Prefix, Boolean>) Prefix -> Prefix.getRegion().startsWith(validRegion.name().toLowerCase()))::apply)
                    .map(Prefix::getIpPrefix)
                    .map(prefix -> prefix.concat(System.lineSeparator()));
        }
        return result;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("The specified region is not a valid one",
                name, type, value);
        return message;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingRequestParameter(MissingServletRequestParameterException ex){
        String name = ex.getParameterName();
        String type = ex.getParameterType();
        String message = String.format("The specified region should not be null",
                name, type);
        return message;
    }

}
