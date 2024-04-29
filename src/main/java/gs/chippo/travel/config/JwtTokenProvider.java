package gs.chippo.travel.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import gs.chippo.travel.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY =SecureKeyGenerator.generateKey(); // 이거 하드코딩하지말고 변경

    // userId로 토큰 생성 => userEntity로 변경
    public String create(UserEntity user){

        try {
            JWSSigner signer = new MACSigner(SECRET_KEY);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getId())  // id 로 토큰 생성
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1시간 유효
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("JWT 토큰 생성 실패", e);
        }
    }
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY);

            return signedJWT.verify(verifier) && new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (JOSEException e) {
            return false;
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("토큰에서 사용자 이름을 추출하는 데 실패했습니다.", e);
        }
    }
}
