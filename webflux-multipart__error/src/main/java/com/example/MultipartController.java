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
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.File;

@RestController
public class MultipartController {

	@Value("#{systemProperties['user.dir']}")
	private String dir;

    /**
     * lbd做了修复
     */
    @PostMapping("/chatApi/uploadChatImg2")
    Flux<DataBuffer> upload(@RequestBody MultiValueMap<String, Part> partFlux) {
        FilePart filePart = (FilePart) partFlux.get("image");
        filePart.transferTo(new File(dir + "/" + filePart.filename()));
        return partFlux.getFirst("submit-name").content();
    }
}
