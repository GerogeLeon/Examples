/*
 * Copyright 2013-2018 the original author or authors.
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
 *
 */

package org.springframework.cloud.gateway.filter;

import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AdaptCachedBodyGlobalFilter implements GlobalFilter, Ordered {

	public static final String CACHED_REQUEST_BODY_KEY = "cachedRequestBody";

	@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Flux<DataBuffer> body = exchange.getAttributeOrDefault(CACHED_REQUEST_BODY_KEY, null);
        if (body != null) {
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return body;
                }
            };
            return chain.filter(exchange.mutate().request(decorator).build());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10; //probably needs to change if combined with other filters that modify request body
    }
}
