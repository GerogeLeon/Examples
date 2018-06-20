/*
 * Copyright 2017-2018 the original author or authors.
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

import java.util.Collections;
import java.util.Map;

import org.springframework.vault.core.util.PropertyTransformer;
import org.springframework.vault.core.util.PropertyTransformers;

/**
 * Support class for {@link SecretBackendMetadata} implementations. Implementing classes
 * are required to implement {@link #getPath()} to derive name and variables from the
 * path.
 *
 * @author Mark Paluch
 * @since 1.1
 */
public abstract class SecretBackendMetadataSupport implements SecretBackendMetadata {

	@Override
	public String getName() {
		return getPath();
	}

	@Override
	public PropertyTransformer getPropertyTransformer() {
		return PropertyTransformers.noop();
	}

	@Override
	public Map<String, String> getVariables() {
		return Collections.singletonMap("path", getPath());
	}
}
