package me.zloits.procyon.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    
    private final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners = new HashMap<>();

    /**
     * Registers an event listener for a specific event type.
     * @param eventClass The class type of the event.
     * @param listener The listener that handles the event.
     * @param <T> The event type.
     */
    public <T extends Event> void registerListener(Class<T> eventClass, EventListener<T> listener) {
        listeners.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Unregisters an event listener for a specific event type.
     * @param eventClass The class type of the event.
     * @param listener The listener to be removed.
     * @param <T> The event type.
     */
    public <T extends Event> void unregisterListener(Class<T> eventClass, EventListener<T> listener) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(eventClass);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventClass); // Remove the event class if no listeners remain.
            }
        }
    }

    /**
     * Calls an event, triggering all registered listeners for the given event type.
     * @param event The event to be called.
     * @param <T> The type of the event.
     */
    public <T extends Event> void callEvent(T event) {
        // Retrieve the listeners for the specific event type.
        List<EventListener<? extends Event>> eventListeners = listeners.get(event.getClass());

        if (eventListeners != null) {
            // Notify each listener of the event.
            for (EventListener<? extends Event> listener : eventListeners) {
                @SuppressWarnings("unchecked")
                EventListener<T> eventListener = (EventListener<T>) listener;

                eventListener.onEvent(event);
            }
        }
    }
}
