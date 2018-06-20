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

package org.springframework.cloud.sleuth.instrument.rxjava;

import java.util.Arrays;

import brave.Tracer;
import brave.Tracing;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rx.plugins.RxJavaSchedulersHook;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} that
 * enables support for RxJava via {@link RxJavaSchedulersHook}.
 *
 * @author Shivang Shah
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(TraceAutoConfiguration.class)
@ConditionalOnBean(Tracing.class)
@ConditionalOnClass(RxJavaSchedulersHook.class)
@ConditionalOnProperty(value = "spring.sleuth.rxjava.schedulers.hook.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SleuthRxJavaSchedulersProperties.class)
public class RxJavaAutoConfiguration {

	@Bean
	SleuthRxJavaSchedulersHook sleuthRxJavaSchedulersHook(Tracer tracer,
			SleuthRxJavaSchedulersProperties sleuthRxJavaSchedulersProperties) {
		return new SleuthRxJavaSchedulersHook(tracer,
				Arrays.asList(sleuthRxJavaSchedulersProperties.getIgnoredthreads()));
	}
}
