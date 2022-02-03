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
            var topics = clients.get(req.name());
            if (topics == null) {
                return new Resp("No topics found. Data sent ignored", 400);
            }
            for(var topic : topics.values()) {
                topic.add(req.text());
            }
            return new Resp("Request successful", 200);
        } else if (method.equals("GET")) {

            clients.putIfAbsent(req.name(), new ConcurrentHashMap<>());
            var clientQueue = clients.get(req.name()).putIfAbsent(req.id(), new ConcurrentLinkedQueue<>());
            if (clientQueue == null) {
                return new Resp("New consumer for this topic created", 200);
            }

            String text = clientQueue.poll();

            if (text == null) {
                return new Resp("No messages for this topic queue", 400);
            }
            return new Resp(text, 200);
        }
        return new Resp("Invalid request", 400);
    }
}
