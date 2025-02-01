package com.wxxzin.portfolio.server.common.auth.security.device.util;

import java.util.UUID;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.device.repository.DeviceRepository;

@Component
@Slf4j
public class DeviceIdGenerator {

    private final DeviceRepository deviceRepository;

    public DeviceIdGenerator(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public String createDeviceId(Long userId) {
        UUID uuid = UUID.randomUUID();
        String deviceId;

        do {
            deviceId = createShortKey(uuid.toString());
        } while (deviceRepository.existsByBaseUserEntityIdAndDeviceId(userId, deviceId));

        return deviceId;
    }

    private static String createShortKey(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte [] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            byte [] shortHash = new byte[8];
            System.arraycopy(hash, 0, shortHash, 0, 8);

            return toBase62(shortHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toBase62(byte [] bytes) {
        final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        BigInteger bigInt = new BigInteger(1, bytes);
        StringBuilder sb = new StringBuilder();

        while (bigInt.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = bigInt.divideAndRemainder(BigInteger.valueOf(62));
            sb.insert(0, BASE62.charAt(divmod[1].intValue()));
            bigInt = divmod[0];
        }

        return sb.toString();
    }
}
