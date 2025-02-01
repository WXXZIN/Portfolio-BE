package com.wxxzin.portfolio.server.common.auth.service;

import com.wxxzin.portfolio.server.domain.user.entity.BaseUserEntity;

public interface RevokeService {
    void revokeAccess(BaseUserEntity baseUserEntity);
}
