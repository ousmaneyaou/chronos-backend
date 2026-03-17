package com.GimPay.Integration_APIs.controllers;

import com.GimPay.Integration_APIs.services.GimPayService;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestHmacController {

    private final GimPayService gimPayService;

    public TestHmacController(GimPayService gimPayService) {
        this.gimPayService = gimPayService;
    }

    /**
     * Endpoint de test pour comparer le HMAC Java avec le générateur en ligne.
     * Appelle : GET /api/test/hmac?dt=2503131200
     * Compare le hash retourné avec celui du générateur https://lightbox-uat.gimpay.org/js/hmac.html
     */
    @GetMapping("/hmac")
    public Map<String, String> testHmac(@RequestParam String dt) {
        String hash = gimPayService.generateSecureHash(dt);
        Map<String, String> result = new LinkedHashMap<>();
        result.put("dateTimeLocalTrxn", dt);
        result.put("hashGenere", hash);
        result.put("instruction", "Compare ce hash avec le générateur https://lightbox-uat.gimpay.org/js/hmac.html");
        return result;
    }
}