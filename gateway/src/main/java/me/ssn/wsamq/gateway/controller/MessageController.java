package me.ssn.wsamq.gateway.controller;

import lombok.AllArgsConstructor;
import me.ssn.wsamq.common.CustomHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.concurrent.Executor;

/**
 * @author s.nechkin
 */
@Controller
public class MessageController {

    private final Executor executor;

    public MessageController() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int threadCount = Runtime.getRuntime().availableProcessors() * 2;
        executor.setCorePoolSize(threadCount);
        executor.setMaxPoolSize(threadCount);
        executor.setQueueCapacity(0);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();

        this.executor = executor;
    }

    @MessageMapping("/message")
    public void messageHandler(String message,
                               @Header(CustomHeaders.HEADER_SCOKET_SESSION_ID) String socketSessionId,
                               Principal principal) {
        Assert.notNull(principal);

        executor.execute(new SendTask("http://localhost:8002/echo", principal.getName(), socketSessionId, message));
        executor.execute(new SendTask("http://localhost:8003/echo", principal.getName(), socketSessionId, message));
        executor.execute(new SendTask("http://localhost:8004/echo", principal.getName(), socketSessionId, message));
    }

    @AllArgsConstructor
    private static class SendTask implements Runnable {
        final String url;
        final String username;
        final String socketSessionId;
        final String message;

        @Override
        public void run() {
            try {
                URL url = new URL(this.url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                con.setRequestProperty(CustomHeaders.HEADER_USERNAME, username);
                con.setRequestProperty(CustomHeaders.HEADER_SCOKET_SESSION_ID, socketSessionId);

                byte[] bytes = message.substring(0, Math.min(message.length(), 2000)).getBytes(StandardCharsets.UTF_8);

                con.setFixedLengthStreamingMode(bytes.length);
                con.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
                con.connect();
                try(OutputStream out = con.getOutputStream()) {
                    out.write(bytes);
                    out.flush();
                }

                // soutResponse(con);

                con.disconnect();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        private void soutResponse(HttpURLConnection con) throws IOException {
            int status = con.getResponseCode();

            Reader streamReader;
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println(content);
        }
    }
}
