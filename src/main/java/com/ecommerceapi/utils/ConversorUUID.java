package com.ecommerceapi.utils;

import java.util.UUID;

public class ConversorUUID {

    public static UUID converteUUID(String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
            return uuid;
        } catch (Exception e) {
            return null;
        }
    }

}
