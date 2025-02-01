package com.wxxzin.portfolio.server;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Tag(name = "Health", description = "서버 상태 체크 API")
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    @Operation(summary = "서버 상태 체크", description = "서버 상태를 체크합니다.")
    public ResponseEntity<Void> checkHealth() {
        return ResponseEntity.ok().build();
    }
}
