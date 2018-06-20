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

package org.springframework.cloud.aws.autoconfigure.mail;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class MailSenderAutoConfigurationTest {

    @Test
    public void mailSender_MailSenderWithJava_configuresJavaMailSender() throws Exception {
        //Arrange
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MailSenderAutoConfiguration.class);

        //Act
        context.refresh();

        //Assert
        assertNotNull(context.getBean(MailSender.class));
        assertNotNull(context.getBean(JavaMailSender.class));
        assertSame(context.getBean(MailSender.class), context.getBean(JavaMailSender.class));
    }
}
