package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> clients =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String method = req.method();
        if (method.equals("POST")) {
            for(var topic : clients.values()) {
                topic.putIfAbsent(req.name(), new ConcurrentLinkedQueue<>());
                topic.get(req.name()).add(req.text());
            }
            return new Resp("Request successful", 200);
        } else if (method.equals("GET")) {

            boolean isClientPresent = clients.putIfAbsent(req.id(), new ConcurrentHashMap<>()) != null;
            if (!isClientPresent) {
                return new Resp("New client for this topic", 400);
            }

            String text = clients.get(req.id()).get(req.name()).poll();

            if (text == null) {
                return new Resp("No messages for this topic", 400);
            }
            return new Resp(text, 200);
        }
        return new Resp("Invalid request", 400);
    }
}
