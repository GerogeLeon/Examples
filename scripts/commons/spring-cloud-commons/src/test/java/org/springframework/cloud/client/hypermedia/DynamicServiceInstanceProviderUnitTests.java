/*
 * Copyright 2015 the original author or authors.
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
package org.springframework.cloud.client.hypermedia;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamicServiceInstanceProviderUnitTests {

	@Mock DiscoveryClient client;

	@Test
	public void returnsNoServiceInCaseNoneIsAvailable() {
		assertThat(new DynamicServiceInstanceProvider(client, "service").getServiceInstance(), is(nullValue()));
	}

	@Test
	public void returnsFirstServiceInCaseMultipleOnesAreAvailable() {

		ServiceInstance first = mock(ServiceInstance.class);
		ServiceInstance second = mock(ServiceInstance.class);

		when(client.getInstances(anyString())).thenReturn(Arrays.asList(first, second));

		assertThat(new DynamicServiceInstanceProvider(client, "service").getServiceInstance(), is(first));
	}
}
