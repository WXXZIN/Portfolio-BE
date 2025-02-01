package com.wxxzin.portfolio.server.common.auth.security.device.service.implement;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceIdExtractor;

@Component
public class DeviceIdExtractorImpl implements DeviceIdExtractor {

    private static final String DEVICE_ID_PREFIX = "deviceId";

    @Override
    public Optional<String> getDeviceId(HttpServletRequest request) {
        Cookie [] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(DEVICE_ID_PREFIX)) {
                    return Optional.of(cookie.getValue());
                }
            }
        }

        return Optional.empty();
    }
}
