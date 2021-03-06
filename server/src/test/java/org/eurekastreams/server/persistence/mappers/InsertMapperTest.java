/*
 * Copyright (c) 2009-2010 Lockheed Martin Corporation
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
package org.eurekastreams.server.persistence.mappers;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.eurekastreams.server.domain.stream.Activity;
import org.eurekastreams.server.persistence.mappers.requests.PersistenceRequest;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * Test class for InsertMapper class.
 * 
 */
public class InsertMapperTest
{
    /**
     * mock context.
     */
    private final Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    /**
     * Test execute method.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testExecute()
    {
        final EntityManager entityManager = context.mock(EntityManager.class);
        final PersistenceRequest req = context.mock(PersistenceRequest.class);
        final Activity msg = context.mock(Activity.class);

        InsertMapper sut = new InsertMapper();
        sut.setEntityManager(entityManager);

        context.checking(new Expectations()
        {
            {
                allowing(req).getDomainEnity();
                will(returnValue(msg));

                oneOf(entityManager).persist(msg);

                oneOf(msg).getId();
                will(returnValue(2L));
            }
        });

        assertEquals(2L, sut.execute(req).longValue());
        context.assertIsSatisfied();
    }

}
