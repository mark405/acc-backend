package com.traffgun.acc.service;

import com.traffgun.acc.entity.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotpService {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecret(User user) {
        var key = gAuth.createCredentials();
        user.setTotpSecret(key.getKey());
        return key.getKey();
    }

    public boolean verifyTotp(User user, int code) {
        if (!user.getTotpEnabled()) {
            return true; // skip if not enabled
        }
        return gAuth.authorize(user.getTotpSecret(), code);
    }

    public String getTotpUri(User user) {
        return "otpauth://totp/TraffGun:" + user.getUsername() +
                "?secret=" + user.getTotpSecret() +
                "&issuer=TraffGun";
    }
}