package com.ghm.guesthousemanagementsystem.helper;

import java.util.UUID;

public class ReferenceGenerator {
    public static String generateReferenceId(){
        return "GHM-"+ UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
