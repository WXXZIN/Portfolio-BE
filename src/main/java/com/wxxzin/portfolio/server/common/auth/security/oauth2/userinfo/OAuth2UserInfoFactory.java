package com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo;

import java.util.Map;

import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.implement.GoogleOAuth2UserInfo;
import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.implement.KakaoOAuth2UserInfo;
import com.wxxzin.portfolio.server.common.auth.security.oauth2.userinfo.implement.NaverOAuth2UserInfo;
import com.wxxzin.portfolio.server.common.exception.UserAuthException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
        switch (provider) {
            case GOOGLE -> {
                return new GoogleOAuth2UserInfo(attributes);
            }
            case KAKAO -> {
                return new KakaoOAuth2UserInfo(attributes);
            }
            case NAVER -> {
                return new NaverOAuth2UserInfo(attributes);
            }
            default ->
                throw new UserAuthException(ErrorMessage.NOT_SUPPORTED_PROVIDER);
        }
    }
}
