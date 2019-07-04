package me.ssn.wsamq.sender.service;

public interface EchoMessageHandler {
    void handle(String username, String socketSessionId, String message);

    void handleGlobal(String message);
}
