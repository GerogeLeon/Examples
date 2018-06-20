/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.ResolvableType;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hantsy
 * <p>
 * //sample code: https://github.com/spring-projects/spring-framework/blob/master/spring-webflux/src/test/java/org/springframework/web/reactive/result/method/annotation/MultipartIntegrationTests.java
 */
@RestController
public class MultipartController {
    
    @PostMapping("/chatApi/uploadChatImg")
    Mono<String> requestBodyMap(@RequestBody Mono<MultiValueMap<String, Part>> partsMono) {
        return partsMono.map(this::partMapDescription);
    }

    @PostMapping("/chatApi/uploadChatImg4")
    Flux<String> requestBodyMapSync(@RequestBody MultiValueMap<String, Part> parts) {
        Flux<DataBuffer> dataBufferFlux = parts.getFirst("sign").content();
        Mono<String> signMono = StringDecoder.textPlainOnly().decodeToMono(dataBufferFlux, ResolvableType.forType(String.class),
                MediaType.TEXT_PLAIN, Collections.emptyMap());
        return Mono.just("sign:").concatWith(signMono);
    }

    @PostMapping(value = "/chatApi/uploadChatImg2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Mono<String> requestBodyFlux(@RequestBody Flux<Part> parts) {
        return partFluxDescription(parts);
    }

    @PostMapping("/chatApi/uploadChatImg3")
    Flux<String> requestPartMap(@RequestPart("image") FilePart fileParts,
                                @RequestPart("image") Mono<FilePart> filePartsMono,
                                @RequestPart("image") Flux<FilePart> filePartsFlux,
                                @RequestPart("uid") String uid,
                                @RequestPart("sign") String sign
    ) {
        System.out.println("fileParts = [" + fileParts + "], filePartsMono = [" + filePartsMono + "], filePartsFlux = [" + filePartsFlux + "]");
        return Mono.just(fileParts.filename()).concatWith(Mono.just(";")).concatWith(filePartsMono.map(filePart -> filePart.filename()))
                .concatWith(Mono.just(";")).concatWith(filePartsFlux.map(filePart -> filePart.filename()))
                .concatWith(Mono.just("uid=" + uid + ";")).concatWith(Mono.just("sign" + sign))
                ;
    }

    private static Mono<String> partFluxDescription(Flux<? extends Part> partsFlux) {
        return partsFlux.log().collectList().map(MultipartController::partListDescription);
    }

    private static String partListDescription(List<? extends Part> parts) {
        return parts.stream().map(MultipartController::partDescription)
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static String partDescription(Part part) {
        return part instanceof FilePart ? part.name() + ":" + ((FilePart) part).filename() : part.name();
    }


    private String partMapDescription(MultiValueMap<String, Part> partsMap) {
        return partsMap.keySet().stream().sorted()
                .map(key -> partListDescription(partsMap.get(key)))
                .collect(Collectors.joining(",", "Map[", "]"));
    }
}
