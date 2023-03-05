package com.server.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Data
public class JwtUtils {
    /*
设置过期时间为1天
 */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    /*
    token私钥
     */
    private static final String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0e618";

    /***
     * 验证token是否正确
     */
    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);//解密算法
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 生成签名，1天后过期
     * @return 加密好的token
     */
    public static String sign(String username,int userId){

        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            //私钥加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>(2);
            header.put("typ:","JWT");
            header.put("alg","HS256");
            //附带username,UserId信息,生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("loginName",username)
                    .withClaim("userId",userId)
                    .withExpiresAt(date)
                    .sign(algorithm);

        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
