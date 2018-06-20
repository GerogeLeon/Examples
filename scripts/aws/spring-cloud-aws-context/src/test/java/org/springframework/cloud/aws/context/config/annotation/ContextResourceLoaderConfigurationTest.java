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

package org.springframework.cloud.aws.context.config.annotation;


import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ContextResourceLoaderConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() throws Exception {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void regionProvider_withConfiguredRegion_staticRegionProviderConfigured() throws Exception {
        //Arrange
        this.context = new AnnotationConfigApplicationContext(ApplicationConfigurationWithResourceLoader.class);

        //Act
        ApplicationBean bean =
                this.context.getBean(ApplicationBean.class);

        //Assert
        assertNotNull(bean.getResourceLoader());
        assertTrue(PathMatchingSimpleStorageResourcePatternResolver.class.isInstance(bean.getResourceLoader()));
    }

    @EnableContextResourceLoader
    static class ApplicationConfigurationWithResourceLoader {

        @Bean
        public ApplicationBean appBean() {
            return new ApplicationBean();
        }

    }

    static class ApplicationBean {

        @Autowired
        private ResourceLoader resourceLoader;

        private ResourceLoader getResourceLoader() {
            return this.resourceLoader;
        }
    }
}
