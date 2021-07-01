package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String method = req.method();
        if (method.equals("POST")) {
            queue.putIfAbsent(req.name(), new ConcurrentLinkedQueue<>());
            queue.get(req.name()).add(req.text());
            return new Resp("Request successful", 200);
        } else if (method.equals("GET")) {
            String response = queue.get(req.name()).poll();
            if (response == null) {
                return new Resp("Invalid request", 400);
            } else {
                return new Resp(response, 200);
            }
        }
        return new Resp("Invalid request", 400);
    }
}
