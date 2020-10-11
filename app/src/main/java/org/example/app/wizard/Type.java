package org.example.app.wizard;

public class Type<T> {
    public <S> S f () {
        return null;
    }

    public static void main(String[] args) {
        Type<?> type = new Type<>();
        String s = type.f();
    }
}
