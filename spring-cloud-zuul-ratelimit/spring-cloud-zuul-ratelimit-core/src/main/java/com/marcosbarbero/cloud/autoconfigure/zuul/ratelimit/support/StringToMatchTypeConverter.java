/*
 * Copyright 2012-2018 the original author or authors.
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

package com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties.Policy.MatchType;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties.Policy.Type;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Liel Chayoun
 */
public class StringToMatchTypeConverter implements Converter<String, MatchType> {

    private static final String DELIMITER = "=";

    @Override
    public MatchType convert(String type) {
        if (type.contains(DELIMITER)) {
            String[] matchType = type.split(DELIMITER);
            return new MatchType(Type.valueOf(matchType[0].toUpperCase()), matchType[1]);
        }
        return new MatchType(Type.valueOf(type.toUpperCase()), null);
    }
}
