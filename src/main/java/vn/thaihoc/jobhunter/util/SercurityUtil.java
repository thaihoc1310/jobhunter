package vn.thaihoc.jobhunter.util;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

@Service
public class SercurityUtil {
    @Value("${thaihoc.jwt.base64secret}")
    private String jwtKey;

    @Value("${thaihoc.jwt.token-validity-in-seconds}")
    private String jwtKeyExpiration;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public void createToken(Authentication authentication) {

    }
}
