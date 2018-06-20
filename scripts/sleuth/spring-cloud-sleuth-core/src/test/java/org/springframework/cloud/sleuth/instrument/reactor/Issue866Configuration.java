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

package org.springframework.cloud.sleuth.instrument.reactor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcin Grzejszczak
 */
@Configuration
public class Issue866Configuration {

	// we don't want to force direct dependencies between components
	// because Spring might just properly setup the context
	// we want to ensure that the HRBDRPP is always executed before
	// any other object is started
	public static TestHook hook;

	@Bean
	HookRegisteringBeanDefinitionRegistryPostProcessor overridingProcessorForTests() {
		TestHook hook = new TestHook();
		Issue866Configuration.hook = hook;
		return hook;
	}

	public static class TestHook extends HookRegisteringBeanDefinitionRegistryPostProcessor {
		public boolean executed = false;

		@Override public void postProcessBeanFactory(
				ConfigurableListableBeanFactory beanFactory) throws BeansException {
			super.postProcessBeanFactory(beanFactory);
			this.executed = true;
		}
	}
}

