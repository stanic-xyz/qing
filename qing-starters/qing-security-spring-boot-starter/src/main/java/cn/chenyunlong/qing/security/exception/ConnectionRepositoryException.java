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

import cn.chenyunlong.qing.security.repository.UsersConnectionRepository;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serial;

/**
 * Base exception class for {@link UsersConnectionRepository} failures.
 *
 * @author Keith Donald
 */
public abstract class ConnectionRepositoryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public ConnectionRepositoryException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public ConnectionRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
