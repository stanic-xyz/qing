/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.chenyunlong.qing.security.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * An implementation of an {@link AbstractAuthenticationToken} that represents an OAuth
 * 2.0 {@link Authentication}.
 * <p>
 * The {@link Authentication} associates an {@link org.springframework.security.core.userdetails.UserDetails} {@code Principal} to the
 * identifier of the {@link #getProviderId() Authorized Client}, which
 * the End-User ({@code Principal}) granted authorization to so that it can access it's
 * protected resources at the UserInfo Endpoint.
 *
 * @author Joe Grandja
 * @author YongWu zheng
 * @see AbstractAuthenticationToken
 * @since 5.0
 */
public class Auth2AuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    /**
     * 返回第三方服务商 id, 如: qq, github
     */
    @Getter
    private final String providerId;

    /**
     * Constructs an {@code Auth2AuthenticationToken} using the provided parameters.
     *
     * @param principal   the user {@code Principal} registered with the OAuth 2.0 Provider
     * @param authorities the authorities granted to the user
     * @param providerId  the providerId
     */
    public Auth2AuthenticationToken(Object principal,
                                    Collection<? extends GrantedAuthority> authorities,
                                    String providerId) {
        super(authorities);
        Assert.notNull(principal, "principal cannot be null");
        Assert.hasText(providerId, "providerId cannot be empty");
        this.principal = principal;
        this.providerId = providerId;
        this.setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        // Credentials are never exposed (by the Provider) for an OAuth2 User
        return "";
    }

}
