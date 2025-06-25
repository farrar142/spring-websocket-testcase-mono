package com.example.fluxwebsocket;

public class Greeting {
    private String content;

    public Greeting(){

    }

    public Greeting(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Greeting greeting = (Greeting) o;

        return content != null ? content.equals(greeting.content) : greeting.content == null;
    }
}
