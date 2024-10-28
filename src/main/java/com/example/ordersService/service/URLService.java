package com.example.ordersService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class URLService {
    @Value("${user.numberGenerateServiceURL}")
    private String numberGenerateServiceURL;


    public String getNumberGenerateServiceURL() {
        return numberGenerateServiceURL;
    }
}
