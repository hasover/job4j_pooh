package ru.job4j;

public class Req {
    private final String method;
    private final String mode;
    private final String name;
    private final String id;
    private final String text;

    private Req(String method, String mode, String name, String id,  String text) {
        this.method = method;
        this.mode = mode;
        this.name = name;
        this.id = id;
        this.text = text;
    }

    public static Req of(String content) {
        System.out.println(content);
        String[] subReq = content.split("[\\s\\n]+");

        String[] reqParts = subReq[1].split("/");

        String method = subReq[0];
        String mode = reqParts[1];
        String name = reqParts[2];
        String id = "";
        String text = "";
        if (method.equals("POST")) {
            text = subReq[subReq.length - 1];
        }
        if (method.equals("GET") && mode.equals("topic")) {
            id = reqParts[3];
        }
        return new Req(method, mode, name, id, text);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }

    public String name() {
        return name;
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return "Req{" +
                "method='" + method + '\'' +
                ", mode='" + mode + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}