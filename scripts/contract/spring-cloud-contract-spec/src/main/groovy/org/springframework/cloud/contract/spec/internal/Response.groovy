/*
 *  Copyright 2013-2018 the original author or authors.
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

package org.springframework.cloud.contract.spec.internal

import groovy.util.logging.Slf4j

import java.util.regex.Pattern

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked
import repackaged.nl.flotsam.xeger.Xeger

import org.springframework.cloud.contract.spec.util.RegexpUtils
/**
 * Represents the response side of the HTTP communication
 *
 * @author Marcin Grzejszczak
 * @author Tim Ysewyn
 * @since 1.0.0
 */
@Slf4j
@TypeChecked
@EqualsAndHashCode
@ToString(includePackage = false, includeFields = true)
class Response extends Common {

	@Delegate ServerPatternValueDslProperty property = new ServerPatternValueDslProperty()
	@Delegate HttpStatus httpStatus = new HttpStatus()
	
	DslProperty status
	DslProperty delay
	Headers headers
	Cookies cookies
	Body body
	boolean async
	ResponseBodyMatchers bodyMatchers

	Response() {
	}

	Response(Response response) {
		this.status = response.status
		this.headers = response.headers
		this.cookies = response.cookies
		this.body = response.body
	}

	void status(int status) {
		this.status = toDslProperty(status)
	}

	void status(DslProperty status) {
		this.status = toDslProperty(status)
	}

	void headers(@DelegatesTo(ResponseHeaders) Closure closure) {
		this.headers = new ResponseHeaders()
		closure.delegate = headers
		closure()
	}

	void cookies(@DelegatesTo(ResponseCookies) Closure closure) {
		this.cookies = new ResponseCookies()
		closure.delegate = cookies
		closure()
	}

	void body(Map<String, Object> body) {
		this.body = new Body(convertObjectsToDslProperties(body))
	}

	void body(List body) {
		this.body = new Body(convertObjectsToDslProperties(body))
	}

	void body(Object bodyAsValue) {
		this.body = new Body(bodyAsValue)
	}

	void fixedDelayMilliseconds(int timeInMilliseconds) {
		this.delay = toDslProperty(timeInMilliseconds)
	}

	void async() {
		this.async = true
	}

	void assertThatSidesMatch(OptionalProperty stubSide, Object testSide) {
		throw new IllegalStateException("Optional can be used only in the test side of the response!")
	}

	DslProperty value(ServerDslProperty server) {
		Object value = server.clientValue
		if (server.clientValue instanceof Pattern && server.isSingleValue()) {
			value = new Xeger(((Pattern)server.clientValue).pattern()).generate()
		} else if (server.clientValue instanceof Pattern && !server.isSingleValue()) {
			value = server.serverValue
		}
		return new DslProperty(value, server.serverValue)
	}

	DslProperty $(ServerDslProperty server) {
		return value(server)
	}

	DslProperty value(Pattern server) {
		return value(new ServerDslProperty(server))
	}

	DslProperty $(Pattern server) {
		return value(server)
	}

	/**
	 * @deprecated Deprecated in favor of bodyMatchers to support other future bodyMatchers too
	 */
	@Deprecated
	void testMatchers(@DelegatesTo(ResponseBodyMatchers) Closure closure) {
		log.warn("testMatchers method is deprecated. Please use bodyMatchers instead")
		bodyMatchers(closure)
	}

	void bodyMatchers(@DelegatesTo(ResponseBodyMatchers) Closure closure) {
		this.bodyMatchers = new ResponseBodyMatchers()
		closure.delegate = this.bodyMatchers
		closure()
	}

	FromRequest fromRequest() {
		return new FromRequest()
	}

	@Override
	DslProperty value(ClientDslProperty client, ServerDslProperty server) {
		if (client.clientValue instanceof Pattern) {
			throw new IllegalStateException("You can't have a regular expression for the response on the client side")
		}
		return super.value(client, server)
	}

	@Override
	DslProperty value(ServerDslProperty server, ClientDslProperty client) {
		if (client.clientValue instanceof Pattern) {
			throw new IllegalStateException("You can't have a regular expression for the response on the client side")
		}
		return super.value(server, client)
	}

	@CompileStatic
	@EqualsAndHashCode(callSuper = true)
	@ToString(includePackage = false)
	private class ServerResponse extends Response {
		ServerResponse(Response request) {
			super(request)
		}
	}

	@CompileStatic
	@EqualsAndHashCode(callSuper = true)
	@ToString(includePackage = false)
	private class ClientResponse extends Response {
		ClientResponse(Response request) {
			super(request)
		}
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class ResponseHeaders extends Headers {

		@Override
		DslProperty matching(String value) {
			return $(p(notEscaped(Pattern.compile("${RegexpUtils.escapeSpecialRegexWithSingleEscape(value)}.*"))),
					c(value))
		}
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class ResponseCookies extends Cookies {

		@Override
		DslProperty matching(String value) {
			return $(p(regex("${RegexpUtils.escapeSpecialRegexWithSingleEscape(value)}.*")), c(value))
		}
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class ServerPatternValueDslProperty extends PatternValueDslProperty<ServerDslProperty> {

		@Override
		protected ServerDslProperty createProperty(Pattern pattern, Object generatedValue) {
			return new ServerDslProperty(pattern, generatedValue)
		}
	}
}

