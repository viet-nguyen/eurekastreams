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
package org.eurekastreams.server.search.bridge;

import org.eurekastreams.server.domain.EntityType;
import org.eurekastreams.server.domain.stream.Activity;
import org.hibernate.search.bridge.StringBridge;

/**
 * Class bridge to get for activity app source.
 */
public class ActivitySourceClassBridge implements StringBridge
{
    /** Prefix for applications. */
    public static final char APPLICATION_PREFIX = 'a';

    /** Prefix for plugins. */
    public static final char PLUGIN_PREFIX = 'p';

    /**
     * Convert the input Message or Activity object into the name of the source app.
     *
     * @param msgObject
     *            the Message or Activity
     * @return the input Message object with name of the source app.
     */
    @Override
    public String objectToString(final Object msgObject)
    {
        Activity activity = (Activity) msgObject;
        EntityType appType = activity.getAppType();
        Long appId = activity.getAppId();

        if (appType != null && appId != null)
        {
            char prefix;
            switch (appType)
            {
            case APPLICATION:
                prefix = APPLICATION_PREFIX;
                break;
            case PLUGIN:
                prefix = PLUGIN_PREFIX;
                break;
            default:
                return "0";
            }
            return prefix + Long.toString(activity.getAppId());
        }

        return "0";
    }
}
