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
package cn.chenyunlong.security.exception;

import cn.chenyunlong.security.enums.ErrorCodeEnum;
import cn.chenyunlong.security.repository.UsersConnectionRepository;
import lombok.Getter;

/**
 * Base exception class for {@link UsersConnectionRepository} failures.
 *
 * @author Keith Donald
 */
@Getter
public class UpdateConnectionException extends RuntimeException {

    private final ErrorCodeEnum errorCodeEnum;
    private final Object data;

    @SuppressWarnings("unused")
    public UpdateConnectionException(ErrorCodeEnum errorCodeEnum, Object data) {
        super(errorCodeEnum.getMsg());
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
    }

    @SuppressWarnings("unused")
    public UpdateConnectionException(ErrorCodeEnum errorCodeEnum, Object data, Throwable cause) {
        super(errorCodeEnum.getMsg(), cause);
        this.errorCodeEnum = errorCodeEnum;
        this.data = data;
    }

}