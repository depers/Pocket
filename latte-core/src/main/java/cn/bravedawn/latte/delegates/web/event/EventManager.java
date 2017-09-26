package cn.bravedawn.latte.delegates.web.event;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class EventManager {

    private static final HashMap<String, Event> EVENTS = new HashMap<>();

    private EventManager(){}

    private static class Holder {
        private static final EventManager INSTANCE = new EventManager();
    }

    public static EventManager getInstance(){
        return Holder.INSTANCE;
    }

    public EventManager addEvent(@NonNull String name, @NonNull Event event){
        EVENTS.put(name, event);
        return this;
    }

    public Event createEvent(@NonNull String action){
        Event event = EVENTS.get(action);
        if (event == null){
            return new UnDefinedEvent();
        }
        return event;
    }

}

