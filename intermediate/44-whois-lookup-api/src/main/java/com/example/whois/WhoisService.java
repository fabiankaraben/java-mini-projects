package com.example.whois;

import org.apache.commons.net.whois.WhoisClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WhoisService {

    private static final String DEFAULT_WHOIS_SERVER = "whois.iana.org";
    private static final int WHOIS_PORT = 43;

    public String getWhoisInfo(String domain) throws IOException {
        return queryWhois(domain, DEFAULT_WHOIS_SERVER, WHOIS_PORT);
    }

    public String getWhoisInfo(String domain, String whoisServer) throws IOException {
        return queryWhois(domain, whoisServer, WHOIS_PORT);
    }

    public String getWhoisInfo(String domain, String whoisServer, int port) throws IOException {
        return queryWhois(domain, whoisServer, port);
    }

    private String queryWhois(String domain, String server, int port) throws IOException {
        WhoisClient whois = new WhoisClient();
        try {
            whois.connect(server, port);
            return whois.query(domain);
        } finally {
            try {
                whois.disconnect();
            } catch (IOException e) {
                // Ignore disconnection errors
            }
        }
    }
}
