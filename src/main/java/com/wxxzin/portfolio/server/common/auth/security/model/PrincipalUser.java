package com.wxxzin.portfolio.server.common.auth.security.model;

import com.wxxzin.portfolio.server.domain.user.enums.Provider;

public interface PrincipalUser {
    Long getUserId();
    Provider getProvider();
}
