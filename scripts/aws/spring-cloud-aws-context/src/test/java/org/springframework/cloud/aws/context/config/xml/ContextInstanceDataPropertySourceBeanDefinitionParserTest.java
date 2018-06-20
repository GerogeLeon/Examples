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

package org.springframework.cloud.aws.context.config.xml;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.cloud.aws.context.MetaDataServer;
import org.springframework.cloud.aws.context.support.env.AwsCloudEnvironmentCheckUtils;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientConfigurationUtils;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Agim Emruli
 */
public class ContextInstanceDataPropertySourceBeanDefinitionParserTest {

    @Test
    public void parseInternal_singleElementDefined_beanDefinitionCreated() throws Exception {
        //Arrange
        HttpServer httpServer = MetaDataServer.setupHttpServer();
        HttpContext instanceIdHttpContext = httpServer.createContext("/latest/meta-data/instance-id", new MetaDataServer.HttpResponseWriterHandler("testInstanceId"));
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);

        //Act
        reader.loadBeanDefinitions(new ClassPathResource(getClass().getSimpleName() + "-context.xml", getClass()));

        //Assert
        BeanFactoryPostProcessor postProcessor = beanFactory.getBean("AmazonEc2InstanceDataPropertySourcePostProcessor", BeanFactoryPostProcessor.class);
        assertNotNull(postProcessor);
        assertEquals(1, beanFactory.getBeanDefinitionCount());

        httpServer.removeContext(instanceIdHttpContext);
    }

    @Test
    public void parseInternal_missingAwsCloudEnvironment_missingBeanDefinition() throws Exception {
        //Arrange
        HttpServer httpServer = MetaDataServer.setupHttpServer();
        HttpContext instanceIdHttpContext = httpServer.createContext("/latest/meta-data/instance-id", new MetaDataServer.HttpResponseWriterHandler(null));
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);

        //Act
        reader.loadBeanDefinitions(new ClassPathResource(getClass().getSimpleName() + "-context.xml", getClass()));

        //Assert
        assertFalse(beanFactory.containsBean("AmazonEc2InstanceDataPropertySourcePostProcessor"));

        httpServer.removeContext(instanceIdHttpContext);
    }

    @Test
    public void parseInternal_singleElementWithUserTagsMapDefined_userTagMapCreatedAlongWithPostProcessor() throws Exception {
        //Arrange
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);

        //Act
        reader.loadBeanDefinitions(new ClassPathResource(getClass().getSimpleName() + "-userTagsMap.xml", getClass()));

        //Assert
        assertTrue(beanFactory.containsBeanDefinition("myUserTags"));
        assertTrue(beanFactory.containsBeanDefinition(AmazonWebserviceClientConfigurationUtils.getBeanName(AmazonEC2Client.class.getName())));
    }

    @Test
    public void parseInternal_singleElementWithCustomAmazonEc2Client_userTagMapCreatedWithCustomEc2Client() throws Exception {
        //Arrange
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);

        //Act
        reader.loadBeanDefinitions(new ClassPathResource(getClass().getSimpleName() + "-customEc2Client.xml", getClass()));

        //Assert
        assertTrue(beanFactory.containsBeanDefinition("myUserTags"));

        ConstructorArgumentValues.ValueHolder valueHolder = beanFactory.getBeanDefinition("myUserTags").
                getConstructorArgumentValues().getArgumentValue(0, BeanReference.class);
        BeanReference beanReference = (BeanReference) valueHolder.getValue();
        assertEquals("amazonEC2Client", beanReference.getBeanName());
        assertFalse(beanFactory.containsBeanDefinition(AmazonWebserviceClientConfigurationUtils.getBeanName(AmazonEC2Client.class.getName())));
    }

    @Test
    public void parseInternal_singleElementWithCustomAttributeAndValueSeparator_postProcessorCreatedWithCustomAttributeAndValueSeparator() throws Exception {
        //Arrange
        HttpServer httpServer = MetaDataServer.setupHttpServer();
        HttpContext instanceIdHttpContext = httpServer.createContext("/latest/meta-data/instance-id", new MetaDataServer.HttpResponseWriterHandler("testInstanceId"));
        HttpContext userDataHttpContext = httpServer.createContext("/latest/user-data", new MetaDataServer.HttpResponseWriterHandler("a=b/c=d"));

        GenericApplicationContext applicationContext = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);

        //Act
        reader.loadBeanDefinitions(new ClassPathResource(getClass().getSimpleName() + "-customAttributeAndValueSeparator.xml", getClass()));

        applicationContext.refresh();

        //Assert
        assertEquals("b", applicationContext.getEnvironment().getProperty("a"));
        assertEquals("d", applicationContext.getEnvironment().getProperty("c"));

        httpServer.removeContext(instanceIdHttpContext);
        httpServer.removeContext(userDataHttpContext);
    }

    @Before
    public void restContextInstanceDataCondition() throws IllegalAccessException {
        Field field = ReflectionUtils.findField(AwsCloudEnvironmentCheckUtils.class, "isCloudEnvironment");
        assertNotNull(field);
        ReflectionUtils.makeAccessible(field);
        field.set(null, null);
    }

    @After
    public void destroyMetaDataServer() throws Exception {
        MetaDataServer.shutdownHttpServer();
    }
}
