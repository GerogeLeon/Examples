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
 */

package org.springframework.cloud.gateway.route.builder;

import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractChangeRequestUriGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AddRequestHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AddRequestParameterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AddResponseHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.HystrixGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.PrefixPathGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.PreserveHostHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RedirectToGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RemoveRequestHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RemoveResponseHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RequestHeaderToRequestUriGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SaveSessionGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SecureHeadersGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SetPathGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SetRequestHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SetResponseHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SetStatusGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory.Config;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import reactor.retry.Repeat;
import reactor.retry.Retry;

public class GatewayFilterSpec extends UriSpec {

	private static final Log log = LogFactory.getLog(GatewayFilterSpec.class);

	public GatewayFilterSpec(Route.Builder routeBuilder, RouteLocatorBuilder.Builder builder) {
		super(routeBuilder, builder);
	}
	
	public GatewayFilterSpec filter(GatewayFilter gatewayFilter) {
		if (gatewayFilter instanceof Ordered) {
			this.routeBuilder.filter(gatewayFilter);
			return this;
		}
		return this.filter(gatewayFilter, 0);
	}

	public GatewayFilterSpec filter(GatewayFilter gatewayFilter, int order) {
		if (gatewayFilter instanceof Ordered) {
			this.routeBuilder.filter(gatewayFilter);
			log.warn("GatewayFilter already implements ordered "+gatewayFilter.getClass()
					+ "ignoring order parameter: "+order);
			return this;
		}
		this.routeBuilder.filter(new OrderedGatewayFilter(gatewayFilter, order));
		return this;
	}

	public GatewayFilterSpec filters(GatewayFilter... gatewayFilters) {
		this.routeBuilder.filters(gatewayFilters);
		return this;
	}

	public GatewayFilterSpec filters(Collection<GatewayFilter> gatewayFilters) {
		this.routeBuilder.filters(gatewayFilters);
		return this;
	}

	public GatewayFilterSpec addRequestHeader(String headerName, String headerValue) {
		return filter(getBean(AddRequestHeaderGatewayFilterFactory.class)
				.apply(c -> c.setName(headerName).setValue(headerValue)));
	}

	public GatewayFilterSpec addRequestParameter(String param, String value) {
		return filter(getBean(AddRequestParameterGatewayFilterFactory.class)
				.apply(c -> c.setName(param).setValue(value)));
	}

	public GatewayFilterSpec addResponseHeader(String headerName, String headerValue) {
		return filter(getBean(AddResponseHeaderGatewayFilterFactory.class)
				.apply(c -> c.setName(headerName).setValue(headerValue)));
	}

	/**
	 * Depends on `spring-cloud-starter-netflix-hystrix`, {@see http://cloud.spring.io/spring-cloud-netflix/}
	 */
	public GatewayFilterSpec hystrix(Consumer<HystrixGatewayFilterFactory.Config> configConsumer) {
		HystrixGatewayFilterFactory factory;
		try {
			factory = getBean(HystrixGatewayFilterFactory.class);
		}
		catch (NoSuchBeanDefinitionException e) {
			throw new NoSuchBeanDefinitionException(HystrixGatewayFilterFactory.class, "This is probably because Hystrix is missing from the classpath, which can be resolved by adding dependency on 'org.springframework.cloud:spring-cloud-starter-netflix-hystrix'");
		}
		return filter(factory.apply(this.routeBuilder.getId(), configConsumer));
	}

	
	
	@SuppressWarnings("unchecked")
	public <T, R> GatewayFilterSpec modifyRequestBody(Class<T> inClass, Class<R> outClass, RewriteFunction<T, R> rewriteFunction) {
		return filter(getBean(ModifyRequestBodyGatewayFilterFactory.class)
				.apply(c -> ((ModifyRequestBodyGatewayFilterFactory.Config<T, R>) c).setRewriteFunction(inClass, outClass, rewriteFunction)));
	}
	
	@SuppressWarnings("unchecked")
	public <T, R> GatewayFilterSpec modifyRequestBody(Consumer<ModifyRequestBodyGatewayFilterFactory.Config> configConsumer) {
		return filter(getBean(ModifyRequestBodyGatewayFilterFactory.class)
				.apply(configConsumer));
	}
	
	

	@SuppressWarnings("unchecked")
	public <T, R> GatewayFilterSpec modifyResponseBody(Class<T> inClass, Class<R> outClass, RewriteFunction<T, R> rewriteFunction) {
		return filter(getBean(ModifyResponseBodyGatewayFilterFactory.class)
				.apply(c -> ((ModifyResponseBodyGatewayFilterFactory.Config<T, R>) c).setRewriteFunction(inClass, outClass, rewriteFunction)));
	}
	
	@SuppressWarnings("unchecked")
	public <T, R> GatewayFilterSpec modifyResponseBody(Consumer<ModifyResponseBodyGatewayFilterFactory.Config<T, R>> configConsumer) {
		return filter(getBean(ModifyResponseBodyGatewayFilterFactory.class)
				.apply(configConsumer));
	}
	
	

	public GatewayFilterSpec prefixPath(String prefix) {
		return filter(getBean(PrefixPathGatewayFilterFactory.class)
				.apply(c -> c.setPrefix(prefix)));
	}

	public GatewayFilterSpec preserveHostHeader() {
		return filter(getBean(PreserveHostHeaderGatewayFilterFactory.class).apply());
	}

	public GatewayFilterSpec redirect(int status, URI url) {
		return redirect(String.valueOf(status), url.toString());
	}

	public GatewayFilterSpec redirect(int status, String url) {
		return redirect(String.valueOf(status), url);
	}

	public GatewayFilterSpec redirect(String status, URI url) {
		return redirect(status, url.toString());
	}

	public GatewayFilterSpec redirect(String status, String url) {
		return filter(getBean(RedirectToGatewayFilterFactory.class).apply(status, url));
	}

	public GatewayFilterSpec redirect(HttpStatus status, URL url) {
		return filter(getBean(RedirectToGatewayFilterFactory.class).apply(status, url));
	}

	public GatewayFilterSpec removeRequestHeader(String headerName) {
		return filter(getBean(RemoveRequestHeaderGatewayFilterFactory.class)
				.apply(c -> c.setName(headerName)));
	}

	public GatewayFilterSpec removeResponseHeader(String headerName) {
		return filter(getBean(RemoveResponseHeaderGatewayFilterFactory.class)
				.apply(c -> c.setName(headerName)));
	}

	public GatewayFilterSpec requestRateLimiter(Consumer<RequestRateLimiterGatewayFilterFactory.Config> configConsumer) {
		return filter(getBean(RequestRateLimiterGatewayFilterFactory.class).apply(configConsumer));
	}

    public RequestRateLimiterSpec requestRateLimiter() {
		return new RequestRateLimiterSpec(getBean(RequestRateLimiterGatewayFilterFactory.class));
	}

	public class RequestRateLimiterSpec {
		private final RequestRateLimiterGatewayFilterFactory filter;

		public RequestRateLimiterSpec(RequestRateLimiterGatewayFilterFactory filter) {
			this.filter = filter;
		}

		public <C, R extends RateLimiter<C>> RequestRateLimiterSpec rateLimiter(Class<R> rateLimiterType,
																				Consumer<C> configConsumer) {
			R rateLimiter = getBean(rateLimiterType);
			C config = rateLimiter.newConfig();
			configConsumer.accept(config);
			rateLimiter.getConfig().put(routeBuilder.getId(), config);
			return this;
		}

		public GatewayFilterSpec configure(Consumer<RequestRateLimiterGatewayFilterFactory.Config> configConsumer) {
			filter(this.filter.apply(configConsumer));
			return GatewayFilterSpec.this;
		}

		// useful when nothing to configure
		public GatewayFilterSpec and() {
			return configure(config -> {});
		}

	}

	public GatewayFilterSpec rewritePath(String regex, String replacement) {
		return filter(getBean(RewritePathGatewayFilterFactory.class)
				.apply(c -> c.setRegexp(regex).setReplacement(replacement)));
	}

	/**
	 * 5xx errors and GET are retryable
	 * @param retries max number of retries
	 */
	public GatewayFilterSpec retry(int retries) {
		return filter(getBean(RetryGatewayFilterFactory.class)
				.apply(retryConfig -> retryConfig.setRetries(retries)));
	}

	public GatewayFilterSpec retry(Consumer<RetryGatewayFilterFactory.RetryConfig> retryConsumer) {
		return filter(getBean(RetryGatewayFilterFactory.class).apply(retryConsumer));
	}

	public GatewayFilterSpec retry(Repeat<ServerWebExchange> repeat, Retry<ServerWebExchange> retry) {
		return filter(getBean(RetryGatewayFilterFactory.class).apply(repeat, retry));
	}

	@Deprecated
	public GatewayFilterSpec retry(Repeat<ServerWebExchange> repeat) {
		return filter(getBean(RetryGatewayFilterFactory.class).apply(repeat));
	}

	@SuppressWarnings("unchecked")
	public GatewayFilterSpec secureHeaders() {
		return filter(getBean(SecureHeadersGatewayFilterFactory.class).apply(c -> {}));
	}

	public GatewayFilterSpec setPath(String template) {
		return filter(getBean(SetPathGatewayFilterFactory.class)
				.apply(c -> c.setTemplate(template)));
	}

	public GatewayFilterSpec setRequestHeader(String headerName, String headerValue) {
		return filter(getBean(SetRequestHeaderGatewayFilterFactory.class)
				.apply(c -> c.setName(headerName).setValue(headerValue)));
	}

	public GatewayFilterSpec setResponseHeader(String headerName, String headerValue) {
		return filter(getBean(SetResponseHeaderGatewayFilterFactory.class)
				.apply(c -> c.setName(headerName).setValue(headerValue)));
	}

	public GatewayFilterSpec setStatus(int status) {
		return setStatus(String.valueOf(status));
	}

	public GatewayFilterSpec setStatus(HttpStatus status) {
		return setStatus(status.toString());
	}

	public GatewayFilterSpec setStatus(String status) {
		return filter(getBean(SetStatusGatewayFilterFactory.class)
				.apply(c -> c.setStatus(status)));
	}

	@SuppressWarnings("unchecked")
	public GatewayFilterSpec saveSession() {
		return filter(getBean(SaveSessionGatewayFilterFactory.class).apply(c -> {}));
	}

	public GatewayFilterSpec stripPrefix(int parts) {
		return filter(getBean(StripPrefixGatewayFilterFactory.class)
				.apply(c -> c.setParts(parts)));
	}

	public GatewayFilterSpec requestHeaderToRequestUri(String headerName) {
		return filter(getBean(RequestHeaderToRequestUriGatewayFilterFactory.class)
				.apply(c -> c.setName(headerName)));
	}

	public GatewayFilterSpec changeRequestUri(
			Function<ServerWebExchange, Optional<URI>> determineRequestUri) {
		return filter(
				new AbstractChangeRequestUriGatewayFilterFactory<Object>(Object.class) {
					@Override
					protected Optional<URI> determineRequestUri(
							ServerWebExchange exchange, Object config) {
						return determineRequestUri.apply(exchange);
					}
				}.apply(c -> {
				}));
	}

	private String routeId() {
		return routeBuilder.getId();
	}
}
