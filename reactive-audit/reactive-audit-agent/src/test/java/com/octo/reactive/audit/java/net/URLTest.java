/*
 * Copyright 2014 OCTO Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.octo.reactive.audit.java.net;

import com.octo.reactive.audit.TestTools;
import com.octo.reactive.audit.lib.FileReactiveAuditException;
import com.octo.reactive.audit.lib.NetworkReactiveAuditException;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static com.octo.reactive.audit.IOTestTools.HOST;
import static com.octo.reactive.audit.IOTestTools.PORT;

public class URLTest
{
	@Test(expected = NetworkReactiveAuditException.class)
	public void openStream_network()
			throws IOException
	{
        TestTools.strict.commit();
		URL url = new URL("http://" + HOST + ":" + PORT);
		url.openStream();
	}

	@Test(expected = FileReactiveAuditException.class)
	public void openStream_file()
			throws IOException
	{
        TestTools.strict.commit();
		URL url = new URL("file://" + HOST + ":" + PORT);
		url.openStream();
	}

	@Test(expected = FileReactiveAuditException.class)
	public void openStream_jarfile()
			throws IOException
	{
        TestTools.strict.commit();
		URL url = new URL("JAR:file:///toto!/toto" + HOST + ":" + PORT);
		url.openStream();
	}
	@Test(expected = NetworkReactiveAuditException.class)
	public void openConnection_http()
			throws IOException
	{
		TestTools.strict.commit();
		URL url = new URL("http://" + HOST + ":" + PORT);
		url.openConnection();
	}
}
