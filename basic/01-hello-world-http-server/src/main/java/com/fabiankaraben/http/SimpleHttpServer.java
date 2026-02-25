package com.fabiankaraben.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;

public class SimpleHttpServer {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SimpleHttpServer.class.getName());
    private static final int PORT = 8080;
    private Server server;
    private ServerConnector connector;

    public void start() throws Exception {
        server = new Server();
        connector = new ServerConnector(server);
        connector.setPort(PORT);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new HelloHandler()), "/*");
        server.setHandler(context);

        server.start();
        logger.info("Server started on port " + PORT);
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
            logger.info("Server stopped");
        }
    }

    public static void main(String[] args) {
        SimpleHttpServer httpServer = new SimpleHttpServer();
        try {
            httpServer.start();
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Stopping server...");
                try {
                    httpServer.stop();
                } catch (Exception e) {
                    logger.severe("Error stopping server: " + e.getMessage());
                }
            }));
            
        } catch (Exception e) {
            logger.severe("Failed to start server: " + e.getMessage());
        }
    }
    
    public int getPort() {
        if (connector != null) {
            return connector.getLocalPort();
        }
        return PORT;
    }
}
