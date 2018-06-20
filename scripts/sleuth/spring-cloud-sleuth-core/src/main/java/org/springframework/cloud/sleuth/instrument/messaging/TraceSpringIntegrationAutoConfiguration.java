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

package org.springframework.cloud.sleuth.instrument.messaging;

import brave.Tracing;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.interceptor.GlobalChannelInterceptorWrapper;
import org.springframework.integration.config.GlobalChannelInterceptor;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} that registers a Sleuth version of the
 * {@link org.springframework.messaging.support.ChannelInterceptor}.
 *
 * @author Spencer Gibb
 * @since 1.0.0
 *
 * @see TracingChannelInterceptor
 */
@Configuration
@ConditionalOnClass(GlobalChannelInterceptor.class)
@ConditionalOnBean(Tracing.class)
@AutoConfigureAfter({ TraceAutoConfiguration.class })
@OnMessagingEnabled
@ConditionalOnProperty(value = "spring.sleuth.integration.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SleuthMessagingProperties.class)
public class TraceSpringIntegrationAutoConfiguration {

	@Bean
	public GlobalChannelInterceptorWrapper tracingGlobalChannelInterceptorWrapper(
			TracingChannelInterceptor interceptor,
			SleuthMessagingProperties properties) {
		GlobalChannelInterceptorWrapper wrapper = new GlobalChannelInterceptorWrapper(interceptor);
		wrapper.setPatterns(properties.getIntegration().getPatterns());
		return wrapper;
	}

	@Bean
	TracingChannelInterceptor traceChannelInterceptor(Tracing tracing) {
		return new TracingChannelInterceptor(tracing);
	}

}
