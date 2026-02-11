package com.example.twofactorauth.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

@Service
public class TotpService {

    private final GoogleAuthenticator googleAuthenticator;

    public TotpService() {
        this.googleAuthenticator = new GoogleAuthenticator();
    }

    public GoogleAuthenticatorKey generateSecret() {
        return googleAuthenticator.createCredentials();
    }

    public String getQrCodeUrl(GoogleAuthenticatorKey key, String accountName) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("MyCompany", accountName, key);
    }

    public boolean validateCode(String secret, int code) {
        return googleAuthenticator.authorize(secret, code);
    }
}
