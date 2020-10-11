package org.example.app.wizard;

public interface EventHandler<T> {
    void handle (T event);
}
