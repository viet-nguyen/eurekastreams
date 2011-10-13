/*
 * Copyright (c) 2011 Lockheed Martin Corporation
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
package org.eurekastreams.server.action.execution;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.eurekastreams.commons.actions.ExecutionStrategy;
import org.eurekastreams.commons.actions.context.PrincipalActionContext;
import org.eurekastreams.server.action.request.GetTokenForStreamRequest;
import org.eurekastreams.server.domain.EntityType;
import org.eurekastreams.server.persistence.mappers.DomainMapper;
import org.eurekastreams.server.service.email.TokenContentFormatter;
import org.eurekastreams.server.service.email.TokenEncoder;
import org.eurekastreams.server.testing.TestContextCreator;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * Tests GetTokenForStreamExecution.
 */
public class GetTokenForStreamExecutionTest
{
    /** Used for mocking objects. */
    private final JUnit4Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    /** Creates the token. */
    private final TokenEncoder tokenEncoder = context.mock(TokenEncoder.class);

    /** Builds the token content. */
    private final TokenContentFormatter tokenContentFormatter = context.mock(TokenContentFormatter.class);

    /** Gets the user's key. */
    private final DomainMapper<Long, byte[]> cryptoKeyDao = context.mock(DomainMapper.class);

    /**
     * Test.
     */
    @Test
    public void test()
    {
        final byte[] key = "key".getBytes();
        final String token = "thetoken";
        final long userId = 8L;
        final long groupId = 6L;
        final String tokenContent = "stuffToGoInTheToken";

        ExecutionStrategy<PrincipalActionContext> sut = new GetTokenForStreamExecution(tokenEncoder,
                tokenContentFormatter, cryptoKeyDao);

        context.checking(new Expectations()
        {
            {
                oneOf(cryptoKeyDao).execute(userId);
                will(returnValue(key));

                oneOf(tokenContentFormatter).buildForStream(EntityType.GROUP, groupId);
                will(returnValue(tokenContent));

                oneOf(tokenEncoder).encode(tokenContent, key);
                will(returnValue(token));
            }
        });

        Serializable result = sut.execute(TestContextCreator.createPrincipalActionContext(
                new GetTokenForStreamRequest(EntityType.GROUP, groupId), null, userId));
        context.assertIsSatisfied();
        assertEquals(token, result);
    }
}