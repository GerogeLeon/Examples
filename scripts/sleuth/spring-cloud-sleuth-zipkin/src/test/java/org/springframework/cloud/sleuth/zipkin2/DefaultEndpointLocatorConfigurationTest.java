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

package org.springframework.cloud.sleuth.zipkin2;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Matcin Wielgus
 */
public class DefaultEndpointLocatorConfigurationTest {

	@Test
	public void endpointLocatorShouldDefaultToServerPropertiesEndpointLocator() {
		ConfigurableApplicationContext ctxt = new SpringApplication(
				EmptyConfiguration.class).run("--spring.jmx.enabled=false");
		assertThat(ctxt.getBean(EndpointLocator.class))
				.isInstanceOf(DefaultEndpointLocator.class);
		ctxt.close();
	}

	@Test
	public void endpointLocatorShouldDefaultToServerPropertiesEndpointLocatorEvenWhenDiscoveryClientPresent() {
		ConfigurableApplicationContext ctxt = new SpringApplication(
				ConfigurationWithRegistration.class).run("--spring.jmx.enabled=false");
		assertThat(ctxt.getBean(EndpointLocator.class))
				.isInstanceOf(DefaultEndpointLocator.class);
		ctxt.close();
	}

	@Test
	public void endpointLocatorShouldRespectExistingEndpointLocator() {
		ConfigurableApplicationContext ctxt = new SpringApplication(
				ConfigurationWithCustomLocator.class).run("--spring.jmx.enabled=false");
		assertThat(ctxt.getBean(EndpointLocator.class))
				.isSameAs(ConfigurationWithCustomLocator.locator);
		ctxt.close();
	}

	@Test
	public void endpointLocatorShouldSetServiceNameToServiceId() {
		ConfigurableApplicationContext ctxt = new SpringApplication(
				ConfigurationWithRegistration.class).run("--spring.jmx.enabled=false",
				"--spring.zipkin.locator.discovery.enabled=true");
		assertThat(ctxt.getBean(EndpointLocator.class).local().serviceName())
				.isEqualTo("from-registration");
		ctxt.close();
	}

	@Test
	public void endpointLocatorShouldAcceptServiceNameOverride() {
		ConfigurableApplicationContext ctxt = new SpringApplication(
				ConfigurationWithRegistration.class).run("--spring.jmx.enabled=false",
				"--spring.zipkin.locator.discovery.enabled=true",
				"--spring.zipkin.service.name=foo");
		assertThat(ctxt.getBean(EndpointLocator.class).local().serviceName())
				.isEqualTo("foo");
		ctxt.close();
	}

	@Test
	public void endpointLocatorShouldRespectExistingEndpointLocatorEvenWhenAskedToBeDiscovery() {
		ConfigurableApplicationContext ctxt = new SpringApplication(
				ConfigurationWithRegistration.class,
				ConfigurationWithCustomLocator.class).run("--spring.jmx.enabled=false",
				"--spring.zipkin.locator.discovery.enabled=true");
		assertThat(ctxt.getBean(EndpointLocator.class))
				.isSameAs(ConfigurationWithCustomLocator.locator);
		ctxt.close();
	}

	@Configuration
	@EnableAutoConfiguration
	public static class EmptyConfiguration {
	}

	@Configuration
	@EnableAutoConfiguration
	public static class ConfigurationWithRegistration {
		@Bean public Registration getRegistration() {
			return new Registration() {
				@Override
				public String getServiceId() {
					return "from-registration";
				}

				@Override
				public String getHost() {
					return null;
				}

				@Override
				public int getPort() {
					return 0;
				}

				@Override
				public boolean isSecure() {
					return false;
				}

				@Override
				public URI getUri() {
					return null;
				}

				@Override
				public Map<String, String> getMetadata() {
					return null;
				}
			};
		}
	}

	@Configuration
	@EnableAutoConfiguration
	public static class ConfigurationWithCustomLocator {
		static EndpointLocator locator = Mockito.mock(EndpointLocator.class);

		@Bean public EndpointLocator getEndpointLocator() {
			return locator;
		}
	}
	public static final byte[] ADDRESS1234 = { 1, 2, 3, 4 };
	Environment environment = new MockEnvironment();

	@Test
	public void portDefaultsTo8080() throws UnknownHostException {
		DefaultEndpointLocator locator = new DefaultEndpointLocator(null,
				new ServerProperties(), environment, new ZipkinProperties(),
				localAddress(ADDRESS1234));

		assertThat(locator.local().port()).isEqualTo(8080);
	}

	@Test
	public void portFromServerProperties() throws UnknownHostException {
		ServerProperties properties = new ServerProperties();
		properties.setPort(1234);

		DefaultEndpointLocator locator = new DefaultEndpointLocator(null,
				properties, environment, new ZipkinProperties(),localAddress(ADDRESS1234));

		assertThat(locator.local().port()).isEqualTo(1234);
	}

	@Test
	public void portDefaultsToLocalhost() throws UnknownHostException {
		DefaultEndpointLocator locator = new DefaultEndpointLocator(null,
				new ServerProperties(), environment, new ZipkinProperties(), localAddress(ADDRESS1234));

		assertThat(locator.local().ipv4()).isEqualTo("1.2.3.4");
	}

	@Test
	public void hostFromServerPropertiesIp() throws UnknownHostException {
		ServerProperties properties = new ServerProperties();
		properties.setAddress(InetAddress.getByAddress(ADDRESS1234));

		DefaultEndpointLocator locator = new DefaultEndpointLocator(null,
				properties, environment, new ZipkinProperties(),
				localAddress(new byte[] { 4, 4, 4, 4 }));

		assertThat(locator.local().ipv4()).isEqualTo("1.2.3.4");
	}

	@Test
	public void appNameFromProperties() throws UnknownHostException {
		ServerProperties properties = new ServerProperties();
		ZipkinProperties zipkinProperties = new ZipkinProperties();
		zipkinProperties.getService().setName("foo");

		DefaultEndpointLocator locator = new DefaultEndpointLocator(null,
				properties, environment, zipkinProperties,localAddress(ADDRESS1234));

		assertThat(locator.local().serviceName()).isEqualTo("foo");
	}

	@Test
	public void negativePortFromServerProperties() throws UnknownHostException {
		ServerProperties properties = new ServerProperties();
		properties.setPort(-1);

		DefaultEndpointLocator locator = new DefaultEndpointLocator(null,
				properties, environment, new ZipkinProperties(),localAddress(ADDRESS1234));

		assertThat(locator.local().port()).isEqualTo(8080);
	}

	private InetUtils localAddress(byte[] address) throws UnknownHostException {
		InetUtils mocked = Mockito.spy(new InetUtils(new InetUtilsProperties()));
		Mockito.when(mocked.findFirstNonLoopbackAddress())
				.thenReturn(InetAddress.getByAddress(address));
		return mocked;
	}
}