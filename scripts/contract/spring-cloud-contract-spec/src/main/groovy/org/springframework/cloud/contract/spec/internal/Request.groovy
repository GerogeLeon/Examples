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

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringEscapeUtils

import org.springframework.cloud.contract.spec.util.RegexpUtils
import repackaged.nl.flotsam.xeger.Xeger

import java.util.regex.Pattern
/**
 * Represents the request side of the HTTP communication
 *
 * @author Marcin Grzejszczak
 * @author Tim Ysewyn
 * @since 1.0.0
 */
@Slf4j
@TypeChecked
@EqualsAndHashCode
@ToString(includePackage = false, includeNames = true)
class Request extends Common {

	@Delegate ClientPatternValueDslProperty property = new ClientPatternValueDslProperty()
	@Delegate HttpMethods httpMethods = new HttpMethods()

	DslProperty method
	Url url
	UrlPath urlPath
	Headers headers
	Cookies cookies
	Body body
	Multipart multipart
	BodyMatchers bodyMatchers

	Request() {
	}

	Request(Request request) {
		this.method = request.method
		this.url = request.url
		this.urlPath = request.urlPath
		this.headers = request.headers
		this.cookies = request.cookies
		this.body = request.body
		this.multipart = request.multipart
	}

	void method(String method) {
		this.method = toDslProperty(method)
	}

	void method(HttpMethods.HttpMethod httpMethod) {
		this.method = toDslProperty(httpMethod.toString())
	}

	void method(DslProperty method) {
		this.method = toDslProperty(method)
	}

	void url(Object url) {
		this.url = new Url(url)
	}

	void url(DslProperty url) {
		this.url = new Url(url)
	}

	void url(Object url, @DelegatesTo(UrlPath) Closure closure) {
		this.url = new Url(url)
		closure.delegate = this.url
		closure()
	}

	void url(DslProperty url, @DelegatesTo(UrlPath) Closure closure) {
		this.url = new Url(url)
		closure.delegate = this.url
		closure()
	}

	void urlPath(String path) {
		this.urlPath = new UrlPath(path)
	}

	void urlPath(DslProperty path) {
		this.urlPath = new UrlPath(path)
	}

	void urlPath(String path, @DelegatesTo(UrlPath) Closure closure) {
		this.urlPath = new UrlPath(path)
		closure.delegate = urlPath
		closure()
	}

	void urlPath(DslProperty path, @DelegatesTo(UrlPath) Closure closure) {
		this.urlPath = new UrlPath(path)
		closure.delegate = urlPath
		closure()
	}

	void headers(@DelegatesTo(RequestHeaders) Closure closure) {
		this.headers = new RequestHeaders()
		closure.delegate = headers
		closure()
	}

	void cookies(@DelegatesTo(RequestCookies) Closure closure) {
		this.cookies = new RequestCookies()
		closure.delegate = cookies
		closure()
	}

	void body(Map<String, Object> body) {
		this.body = new Body(convertObjectsToDslProperties(body))
	}

	void body(List body) {
		this.body = new Body(convertObjectsToDslProperties(body))
	}

	void body(DslProperty dslProperty) {
		this.body = new Body(dslProperty)
	}

	void body(Object bodyAsValue) {
		this.body = new Body(bodyAsValue)
	}

	Body getBody() {
		return body
	}

	void multipart(Map<String, Object> body) {
		this.multipart = new Multipart(convertObjectsToDslProperties(body))
	}

	void multipart(List multipartAsList) {
		this.multipart = new Multipart(convertObjectsToDslProperties(multipartAsList))
	}

	void multipart(DslProperty dslProperty) {
		this.multipart = new Multipart(dslProperty)
	}

	void multipart(Object multipartAsValue) {
		this.multipart = new Multipart(multipartAsValue)
	}

	MatchingStrategy equalTo(Object value) {
		return new MatchingStrategy(value, MatchingStrategy.Type.EQUAL_TO)
	}

	MatchingStrategy containing(Object value) {
		return new MatchingStrategy(value, MatchingStrategy.Type.CONTAINS)
	}

	MatchingStrategy matching(Object value) {
		return new MatchingStrategy(value, MatchingStrategy.Type.MATCHING)
	}

	MatchingStrategy notMatching(Object value) {
		return new MatchingStrategy(value, MatchingStrategy.Type.NOT_MATCHING)
	}

	MatchingStrategy equalToXml(Object value) {
		return new MatchingStrategy(value, MatchingStrategy.Type.EQUAL_TO_XML)
	}

	MatchingStrategy equalToJson(Object value) {
		return new MatchingStrategy(value, MatchingStrategy.Type.EQUAL_TO_JSON)
	}

	MatchingStrategy absent() {
		return new MatchingStrategy(true, MatchingStrategy.Type.ABSENT)
	}

	void assertThatSidesMatch(Object stubSide, OptionalProperty testSide) {
		throw new IllegalStateException("Optional can be used only for the stub side of the request!")
	}

	DslProperty value(ClientDslProperty client) {
		Object clientValue = client.clientValue
		if (client.clientValue instanceof Pattern && client.isSingleValue()) {
			clientValue = StringEscapeUtils.escapeJava(new Xeger(((Pattern)client.clientValue).pattern()).generate())
		} else if (client.clientValue instanceof Pattern && !client.isSingleValue()) {
			clientValue = client.serverValue
		}
		return new DslProperty(client.clientValue, clientValue)
	}

	DslProperty $(ClientDslProperty client) {
		return value(client)
	}

	DslProperty value(Pattern client) {
		return value(new ClientDslProperty(client))
	}

	DslProperty $(Pattern client) {
		return value(client)
	}

	/**
	 * @deprecated Deprecated in favor of bodyMatchers to support other future bodyMatchers too
	 */
	@Deprecated
	void stubMatchers(@DelegatesTo(BodyMatchers) Closure closure) {
		log.warn("stubMatchers method is deprecated. Please use bodyMatchers instead")
		bodyMatchers(closure)
	}

	void bodyMatchers(@DelegatesTo(BodyMatchers) Closure closure) {
		this.bodyMatchers = new BodyMatchers()
		closure.delegate = this.bodyMatchers
		closure()
	}

	@Override
	DslProperty value(ClientDslProperty client, ServerDslProperty server) {
		if (server.clientValue instanceof Pattern) {
			throw new IllegalStateException("You can't have a regular expression for the request on the server side")
		}
		return super.value(client, server)
	}

	@Override
	DslProperty value(ServerDslProperty server, ClientDslProperty client) {
		if (server.clientValue instanceof Pattern) {
			throw new IllegalStateException("You can't have a regular expression for the request on the server side")
		}
		return super.value(server, client)
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class ServerRequest extends Request {
		ServerRequest(Request request) {
			super(request)
		}
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class ClientRequest extends Request {
		ClientRequest(Request request) {
			super(request)
		}
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class RequestHeaders extends Headers {

		@Override
		DslProperty matching(String value) {
			return $(c(regex("${RegexpUtils.escapeSpecialRegexWithSingleEscape(value)}.*")),
					p(value))
		}
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class RequestCookies extends Cookies {

		@Override
		DslProperty matching(String value) {
			return $(c(regex("${RegexpUtils.escapeSpecialRegexWithSingleEscape(value)}.*")),
					p(value))
		}
	}

	@CompileStatic
	@EqualsAndHashCode(includeFields = true)
	@ToString(includePackage = false)
	private class ClientPatternValueDslProperty extends PatternValueDslProperty<ClientDslProperty> {

		@Override
		protected ClientDslProperty createProperty(Pattern pattern, Object generatedValue) {
			return new ClientDslProperty(pattern, generatedValue)
		}
	}
}