package com.wxxzin.portfolio.server.common.auth.security.device.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.device.dto.response.DeviceResponseDTO;
import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceService;
import com.wxxzin.portfolio.server.common.auth.security.model.PrincipalUser;
import com.wxxzin.portfolio.server.common.response.success.ApiSuccessResponse;
import com.wxxzin.portfolio.server.common.response.success.SuccessMessage;

@RestController
@Slf4j
@RequestMapping("/api/v1/user/device")
@Tag(name = "Device", description = "기기 관련 API")
public class DeviceController {
    
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    @Operation(summary = "기기 리스트 조회", description = "유저의 기기 리스트를 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<Map<String, List<DeviceResponseDTO>>>> getDeviceList(
        @AuthenticationPrincipal PrincipalUser principalUser
    ) {

        Long userId = principalUser.getUserId();

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_DEVICE_LIST_RETRIEVE_SUCCESS.getMessage(),
                    deviceService.getDeviceList(userId)
                )
            );
    }

    @GetMapping("/{deviceId}")
    @Operation(summary = "기기 정보 조회", description = "유저의 기기 정보를 조회합니다.")
    public ResponseEntity<ApiSuccessResponse<DeviceResponseDTO>> getDeviceDetail(
        @AuthenticationPrincipal PrincipalUser principalUser,
        @PathVariable(value = "deviceId") String deviceId
    ) {

        Long userId = principalUser.getUserId();

        return ResponseEntity
            .status(200)
            .body(
                ApiSuccessResponse.of(
                    SuccessMessage.AUTH_DEVICE_INFO_RETRIEVE_SUCCESS.getMessage(),
                    deviceService.getDeviceDetail(deviceId, userId)
                )
            );
    }
}
