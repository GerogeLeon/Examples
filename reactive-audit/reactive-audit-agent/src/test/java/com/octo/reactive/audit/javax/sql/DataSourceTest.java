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

package com.octo.reactive.audit.javax.sql;

import com.octo.reactive.audit.TestTools;
import com.octo.reactive.audit.lib.NetworkReactiveAuditException;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataSourceTest
{
	private final DataSource x = (DataSource) TestTools.createProxy(DataSource.class);

	@Test(expected = NetworkReactiveAuditException.class)
	public void getConnection()
			throws SQLException
	{
        TestTools.strict.commit();
		x.getConnection();
	}

	@Test(expected = NetworkReactiveAuditException.class)
	public void getConnection_up()
			throws SQLException
	{
        TestTools.strict.commit();
		x.getConnection(null, null);
	}

}
