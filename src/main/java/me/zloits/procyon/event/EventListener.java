package me.zloits.procyon.event;

public interface EventListener<T extends Event> {

    void onEvent(T event);
}
