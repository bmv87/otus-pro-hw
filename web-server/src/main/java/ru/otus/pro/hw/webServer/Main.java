package ru.otus.pro.hw.webServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.helpers.ApplicationArgumentsHelper;
import ru.otus.pro.hw.webServer.helpers.ApplicationPropertiesHelper;
import ru.otus.pro.hw.webServer.server.HttpServer;

public class Main {
    private static final String PORT_ARG_NAME = "-port";
    private static final int DEFAULT_PORT = 8189;
    private static final int DEFAULT_RECEIVE_BUFFER_SIZE = 1048576;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        ApplicationArgumentsHelper.tryParse(args);
        ApplicationPropertiesHelper.load(Main.class);
        Integer port = null;
        int receiveBufferSize = DEFAULT_RECEIVE_BUFFER_SIZE;
        try {
            port = ApplicationArgumentsHelper.tryGet(PORT_ARG_NAME, Integer.class);
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            try {
                port = ApplicationPropertiesHelper.tryGet(ApplicationPropertiesHelper.SOCKET_PORT, Integer.class);
            } catch (RuntimeException ex) {
                logger.info(ex.getMessage());
            }
        }
        if (port == null) {
            port = DEFAULT_PORT;
        }

        try {
            receiveBufferSize = ApplicationPropertiesHelper.tryGet(ApplicationPropertiesHelper.SOCKET_RECEIVE_BUFFERSIZE, Integer.class);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        try (var server = new HttpServer(port, receiveBufferSize)) {
            server.start();
        }
    }
}