/*
 * Copyright 2015 the original author or authors.
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

package cn.chenyunlong.qing.security.entity;

import java.io.Serializable;

/**
 * The unique business key for a {@link ConnectionData} instance.
 * A composite key that consists of the providerId (e.g. "facebook") plus providerUserId (e.g. "125660").
 * Provides the basis for connection equals() and hashCode().
 *
 * @param providerId 第三方服务商
 * @param providerUserId 第三方用户id
 * @author Keith Donald
 */
public record ConnectionKey(String providerId, String providerUserId) implements Serializable {

    /**
     * Creates a new {@link ConnectionKey}.
     *
     * @param providerId the id of the provider e.g. facebook
     * @param providerUserId id of the provider user account e.g. '125660'
     */
    public ConnectionKey {
    }

    /**
     * The id of the provider as it is registered in the system.
     * This value should never change.
     * Never null.
     *
     * @return The id of the provider as it is registered in the system.
     */
    @Override
    public String providerId() {
        return providerId;
    }

    /**
     * The id of the external provider user representing the remote end of the connection.
     * May be null if this information is not exposed by the provider.
     * This value should never change.
     * Must be present to support sign-in by the provider user.
     * Must be present to establish multiple connections with the provider.
     *
     * @return The id of the external provider user representing the remote end of the connection.
     */
    @Override
    public String providerUserId() {
        return providerUserId;
    }

    // object identity

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConnectionKey other)) {
            return false;
        }
        boolean sameProvider = providerId.equals(other.providerId);
        return providerUserId != null ? sameProvider && providerUserId.equals(other.providerUserId) : sameProvider && other.providerUserId == null;
    }

    @Override
    public String toString() {
        return providerId + ":" + providerUserId;
    }

}
