package com.blog.back.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JwtFactory {

    static final String alias="private";
    static final char[] password="pass123".toCharArray();

    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicKey;

    //私钥生成指令
    //keytool -genkey -alias private -keyalg RSA -storepass pass123 -keysize 1024 -keystore private.jks -validity 3655

    //初始化操作
    static {
        try(InputStream resource = new ClassPathResource("private.jks").getInputStream()){

            KeyStore keyStore=KeyStore.getInstance("jks");
            keyStore.load(resource,password);

            privateKey=(RSAPrivateKey)keyStore.getKey(alias,password);
            publicKey=(RSAPublicKey) keyStore.getCertificate(alias).getPublicKey();
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    //产生token
    public static String create(Integer id,int expire){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, expire);

        return JWT.create()
                .withHeader(new HashMap<>())
                .withClaim("id",id)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.RSA256(privateKey));
    }

    //验证token是否正确
    public static Boolean verify(String token){
        JWTVerifier jwtVerifier=JWT.require(Algorithm.RSA256(publicKey)).build();

        try {
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }

    //返回载荷信息
    public static Map<String,Claim> getMes(String token){
        JWTVerifier jwtVerifier=JWT.require(Algorithm.RSA256(publicKey)).build();
        Map<String,Claim> claims = null;
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            claims = decodedJWT.getClaims();
        }catch (TokenExpiredException e){
            e.printStackTrace();
        }
        return claims;
    }
}