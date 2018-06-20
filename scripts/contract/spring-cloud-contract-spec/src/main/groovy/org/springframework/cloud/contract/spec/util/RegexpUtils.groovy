/*
 *  Copyright 2013-2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.springframework.cloud.contract.spec.util

import groovy.transform.CompileStatic

import java.util.regex.Pattern

/**
 * Useful utility methods to work with regular expressions
 *
 * @since 1.0.2
 */
@CompileStatic
class RegexpUtils {

	private final static Pattern SPECIAL_REGEX_CHARS = Pattern.compile('[{}()\\[\\].+*?^$\\\\|]')

	static String escapeSpecialRegexChars(String str) {
		return SPECIAL_REGEX_CHARS.matcher(str).replaceAll('\\\\\\\\$0')
	}

	static String escapeSpecialRegexWithSingleEscape(String str) {
		return SPECIAL_REGEX_CHARS.matcher(str).replaceAll('\\\\$0')
	}

}
