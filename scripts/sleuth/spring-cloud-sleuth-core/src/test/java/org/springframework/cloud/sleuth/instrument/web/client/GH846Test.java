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

package org.springframework.cloud.sleuth.instrument.web.client;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = GH846Test.App.class, webEnvironment=WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class GH846Test {

	@Autowired
	private MyBean myBean;
	
	@Test
	public void doit() throws Exception {
		int count = myBean.listAndCount();
		Assert.assertEquals("Change detected in RestTemplate interceptor *after* @PostConstruct", count, myBean.getCountAtPostConstruct());
	}
	
	@EnableAutoConfiguration
	@Configuration
	static class App {
		@Bean
		public RestTemplate myRestTemplate() {
			return new RestTemplate();
		}
		
		@Bean
		public MyBean myBean() {
			return new MyBean();
		}
	}

	static class MyBean {
		@Autowired
		private RestTemplate restTemplate;
		
		/** Number of interceptors registered in the RestTemplate during @PostConstruct */ 
		private int countAtPostConstruct;
		
		@PostConstruct
		public void init() {
			countAtPostConstruct = listAndCount();
		}
		
		public int listAndCount() {
			for(ClientHttpRequestInterceptor interceptor: restTemplate.getInterceptors()) {
				System.out.println(interceptor);
			}
			return restTemplate.getInterceptors().size();
		}

		public int getCountAtPostConstruct() {
			return countAtPostConstruct;
		}
	}
}
