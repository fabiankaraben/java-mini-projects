package com.fabiankaraben.http;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class HelloHandler extends HttpServlet {

    private static final Logger logger = Logger.getLogger(HelloHandler.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info(String.format("Received GET request for %s", req.getRequestURI()));
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().write("Hello World");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info(String.format("Received POST request for %s", req.getRequestURI()));
        resp.setStatus(405);
        resp.setContentType("text/plain; charset=UTF-8");
        resp.getWriter().write("Method Not Allowed");
    }
}
