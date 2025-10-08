package com.ghm.guesthousemanagementsystem.helper;

import java.util.UUID;

public class TokenGenerator {
    public static String generateToken(){
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0,10).toUpperCase();
    }
}
