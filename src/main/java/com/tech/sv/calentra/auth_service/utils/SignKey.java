package com.tech.sv.calentra.auth_service.utils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class SignKey {
    static String secretKey = "mTc6e71Wchdr65tZai8c0FECTHVjHSU7e3QIXbkgzgjncRmakG/J8eI/MF0I+h5zeq+GqbWIphts55ya2j3djw==";
    public static SecretKey getSecretKey(){
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
