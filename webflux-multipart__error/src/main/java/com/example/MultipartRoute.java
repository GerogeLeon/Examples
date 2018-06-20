/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@Configuration
public class MultipartRoute {

    @Value("#{systemProperties['user.dir']}")
    private String dir;

    /**
     * lbd做了修复, 未验
     */
    @Bean
    RouterFunction<ServerResponse> multipartRouter() {
        return RouterFunctions.route(RequestPredicates.POST("/chatApi/uploadChatImg"), request ->
                request.body(BodyExtractors.toMultipartData()).flatMap(parts -> {
                            Map<String, Part> multipartMap = parts.toSingleValueMap();
                            FilePart filePart = (FilePart) multipartMap.get("image");
                            filePart.transferTo(new File(dir + "/" + filePart.filename()));
                            return ServerResponse.ok().body(
                                    StringDecoder.textPlainOnly().decode(multipartMap.get("submit-name").content(), ResolvableType.forType(String.class), MediaType.TEXT_PLAIN, Collections.emptyMap())
//                                    Mono.just((String)multipartMap.get("submit-name"))

                                    , String.class);

                        }
                )).and(RouterFunctions.resources("/**", new ClassPathResource("static/")));
    }
}
