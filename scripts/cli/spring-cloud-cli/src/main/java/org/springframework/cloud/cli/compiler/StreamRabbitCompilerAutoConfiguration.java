/*
 * Copyright 2013-2014 the original author or authors.
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
package org.springframework.cloud.cli.compiler;

import org.springframework.boot.cli.compiler.DependencyCustomizer;

/**
 * @author Dave Syer
 *
 */
public class StreamRabbitCompilerAutoConfiguration
		extends BaseStreamCompilerAutoConfiguration {

	@Override
	protected String getTransport() {
		return "rabbit";
	}

	@Override
	public void applyDependencies(DependencyCustomizer dependencies) {
		dependencies
				.ifAnyMissingClasses(
						"org.springframework.cloud.stream.binder.rabbit.config.RabbitServiceAutoConfiguration")
				.add("spring-cloud-starter-stream-rabbit");
	}

}
