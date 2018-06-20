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

package com.octo.reactive.audit.java.io;

import com.octo.reactive.audit.AbstractFileAudit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.FileOutputStream;

import static com.octo.reactive.audit.lib.Latency.HIGH;

// Nb methods: 1
@Aspect
public class FlushableAudit extends AbstractFileAudit
{
	@Before("call(* java.io.Flushable.flush())")
	public void flush(JoinPoint thisJoinPoint)
	{
		if ((thisJoinPoint.getTarget() instanceof FileOutputStream) ||
				(thisJoinPoint.getTarget().getClass().getName().equals("java.net.SocketOutputStream")))
			latency(HIGH, thisJoinPoint);
	}

}
