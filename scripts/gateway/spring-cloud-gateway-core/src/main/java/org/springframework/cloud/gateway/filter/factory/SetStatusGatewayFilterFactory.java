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

import java.util.Arrays;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

/**
 * @author Spencer Gibb
 */
public class SetStatusGatewayFilterFactory extends AbstractGatewayFilterFactory<SetStatusGatewayFilterFactory.Config> {

	public static final String STATUS_KEY = "status";

	public SetStatusGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(STATUS_KEY);
	}

	@Override
	public GatewayFilter apply(Config config) {
		final HttpStatus status = ServerWebExchangeUtils.parse(config.status);
		final Integer intStatus;
		if (status == null) {
			intStatus = Integer.parseInt(config.status);
		} else {
			intStatus = null;
		}
		return (exchange, chain) -> {

			// option 1 (runs in filter order)
			/*exchange.getResponse().beforeCommit(() -> {
				exchange.getResponse().setStatusCode(finalStatus);
				return Mono.empty();
			});
			return chain.filter(exchange);*/

			// option 2 (runs in reverse filter order)
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				// check not really needed, since it is guarded in setStatusCode,
				// but it's a good example
				if (!exchange.getResponse().isCommitted()) {
					if (status != null) { // standard status
						setResponseStatus(exchange, status);
					} else if (intStatus != null && exchange.getResponse() instanceof AbstractServerHttpResponse) { //non-standard
						((AbstractServerHttpResponse)exchange.getResponse()).setStatusCodeValue(intStatus);
					}
				}
			}));
		};
	}

	public static class Config {
		//TODO: relaxed HttpStatus converter
		private String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

}
