/*
 * Copyright (c) 2010 Lockheed Martin Corporation
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
package org.eurekastreams.web.client.model;

import org.eurekastreams.server.action.request.directory.GetDirectorySearchResultsRequest;
import org.eurekastreams.server.domain.PagedSet;
import org.eurekastreams.server.search.modelview.PersonModelView;
import org.eurekastreams.web.client.events.data.GotOrganizationEmployeesResponseEvent;
import org.eurekastreams.web.client.ui.Session;

/**
 * Organization Employees Model.
 *
 */
public class OrganizationEmployeesModel extends BaseModel implements Fetchable<GetDirectorySearchResultsRequest>
{
    /**
     * Singleton.
     */
    private static OrganizationEmployeesModel model = new OrganizationEmployeesModel();

    /**
     * Gets the singleton.
     *
     * @return the singleton.
     */
    public static OrganizationEmployeesModel getInstance()
    {
        return model;
    }

    /**
     * {@inheritDoc}
     */
    public void fetch(final GetDirectorySearchResultsRequest request, final boolean useClientCacheIfAvailable)
    {
        super.callReadAction("getEmployeesForOrg", request, new OnSuccessCommand<PagedSet<PersonModelView>>()
        {
            public void onSuccess(final PagedSet<PersonModelView> response)
            {
                Session.getInstance().getEventBus()
                        .notifyObservers(new GotOrganizationEmployeesResponseEvent(response));
            }
        }, useClientCacheIfAvailable);
    }
}