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

package org.springframework.boot.groovy.cloud;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.cli.compiler.StreamRabbitCompilerAutoConfiguration;
import org.springframework.core.annotation.AliasFor;

/**
 * Pseudo annotation used to trigger {@link StreamRabbitCompilerAutoConfiguration} and
 * Redis.
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@org.springframework.cloud.stream.annotation.EnableBinding
public @interface EnableBinding {

	@AliasFor(annotation = org.springframework.cloud.stream.annotation.EnableBinding.class, attribute = "value")
	Class<?>[] value() default {};

	String transport() default "rabbit";
}
