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

package cn.chenyunlong.qing.security.exception;

import cn.chenyunlong.qing.security.entity.ConnectionData;
import cn.chenyunlong.qing.security.entity.ConnectionKey;
import cn.chenyunlong.qing.security.repository.UsersConnectionRepository;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serial;

/**
 * Thrown by a {@link UsersConnectionRepository} when attempting to fetch a {@link ConnectionData} and no such
 * connection exists with the provided key.
 *
 * @author Keith Donald
 * @see UsersConnectionRepository#getConnection(String, ConnectionKey)
 */
public final class NoSuchConnectionException extends ConnectionRepositoryException {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final ConnectionKey connectionKey;

    public NoSuchConnectionException(ConnectionKey connectionKey) {
        super("No such connection exists with key " + connectionKey);
        this.connectionKey = connectionKey;
    }

    /**
     * The invalid key value.
     *
     * @return The invalid key value.
     */
    @SuppressWarnings("unused")
    public ConnectionKey getConnectionKey() {
        return connectionKey;
    }

}
