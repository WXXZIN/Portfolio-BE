package com.wxxzin.portfolio.server.common.auth.security.device.service;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public interface DeviceIdExtractor {
    Optional<String> getDeviceId(HttpServletRequest request);
}
