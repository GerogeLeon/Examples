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

package org.springframework.cloud.sleuth.instrument.web.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.sleuth.util.ArrayListSpanReporter;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Issue469.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.mvc.view.prefix=/WEB-INF/jsp/",
		"spring.mvc.view.suffix=.jsp"})
public class Issue469Tests {

	@Autowired ArrayListSpanReporter reporter;
	@Autowired Environment environment;
	RestTemplate restTemplate = new RestTemplate();

	@Test
	public void should_not_result_in_tracing_exceptions_when_using_view_controllers() throws Exception {
		try {
			this.restTemplate
					.getForObject("http://localhost:" + port() + "/welcome", String.class);
		} catch (Exception e) {
			// JSPs are not rendered
			then(e).hasMessageContaining("404");
		}

		then(this.reporter.getSpans()).isNotEmpty();
	}

	private int port() {
		return this.environment.getProperty("local.server.port", Integer.class);
	}

}