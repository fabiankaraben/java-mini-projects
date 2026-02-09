package com.example.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SessionController {

    @GetMapping("/")
    public Map<String, Object> home(HttpSession session) {
        Integer visits = (Integer) session.getAttribute("visits");
        if (visits == null) {
            visits = 0;
        }
        session.setAttribute("visits", ++visits);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("visits", visits);
        return response;
    }

    @GetMapping("/set")
    public String setSession(@RequestParam String key, @RequestParam String value, HttpSession session) {
        session.setAttribute(key, value);
        return "Saved " + key + "=" + value;
    }

    @GetMapping("/get")
    public String getSession(@RequestParam String key, HttpSession session) {
        return (String) session.getAttribute(key);
    }
}
