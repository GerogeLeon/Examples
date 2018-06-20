/*
 * Copyright 2013-2017 the original author or authors.
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

package org.springframework.cloud.gateway.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.PRESERVE_HOST_HEADER_ATTRIBUTE;

/**
 * @author Spencer Gibb
 */
public class PreserveHostHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory {

	public GatewayFilter apply() {
		return apply(o -> {});
	}

    public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			exchange.getAttributes().put(PRESERVE_HOST_HEADER_ATTRIBUTE, true);
			return chain.filter(exchange);
		};
	}
}
