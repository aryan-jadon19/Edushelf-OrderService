package com.edushelf.orderservice.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.edushelf.orderservice.exception.PriceNotFoundException;

@Service
public class PriceService {

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, BigDecimal> fallbackPrices = new HashMap<>();

    public PriceService() {
        // Add some sample data
        fallbackPrices.put("prod123-sku123", new BigDecimal("19.99"));
        fallbackPrices.put("prod456-sku456", new BigDecimal("29.99"));
    }

    public BigDecimal getPrice(String productId, String sku) {
        String url = "http://price-service/api/prices?productId=" + productId + "&sku=" + sku;
        try {
            return restTemplate.getForObject(url, BigDecimal.class);
        } catch (RestClientException e) {
            // Fallback to hardcoded prices if the external service fails
            String key = productId + "-" + sku;
            if (fallbackPrices.containsKey(key)) {
                return fallbackPrices.get(key);
            } else {
                throw new PriceNotFoundException("Price not available for productId: " + productId + " and sku: " + sku);
            }
        }
    }
}
