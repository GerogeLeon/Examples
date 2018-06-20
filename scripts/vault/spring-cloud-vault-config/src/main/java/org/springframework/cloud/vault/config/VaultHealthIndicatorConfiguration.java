/*
 * Copyright 2016-2018 the original author or authors.
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
package org.springframework.cloud.vault.config;

import java.util.Map;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultOperations;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} for Vault providing beans for the application context.
 *
 * @author Stuart Ingram
 * @author Mark Paluch
 * @since 1.1
 */
@Configuration
@ConditionalOnClass(HealthIndicator.class)
@ConditionalOnBean(VaultBootstrapConfiguration.class)
@ConditionalOnEnabledHealthIndicator("vault")
@ConditionalOnProperty(name = "spring.cloud.vault.enabled", matchIfMissing = true)
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
public class VaultHealthIndicatorConfiguration extends
		CompositeHealthIndicatorConfiguration<VaultHealthIndicator, VaultOperations> {

	private final Map<String, VaultOperations> vaultTemplates;

	public VaultHealthIndicatorConfiguration(Map<String, VaultOperations> vaultTemplates) {
		this.vaultTemplates = vaultTemplates;
	}

	@Bean
	@ConditionalOnMissingBean(name = { "vaultHealthIndicator" })
	public HealthIndicator vaultHealthIndicator() {
		return this.createHealthIndicator(this.vaultTemplates);
	}
}
