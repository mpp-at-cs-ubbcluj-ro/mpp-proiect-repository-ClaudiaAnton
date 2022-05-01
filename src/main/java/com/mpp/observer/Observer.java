package com.mpp.observer;


import com.mpp.events.EventInterface;

public interface Observer<E extends EventInterface> {
    void update(E e);
}