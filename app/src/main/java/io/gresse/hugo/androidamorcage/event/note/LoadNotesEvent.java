package io.gresse.hugo.androidamorcage.event.note;

import io.gresse.hugo.androidamorcage.event.base.RetryEvent;

/**
 * Called to load a new set of note
 *
 * Created by Hugo Gresse on 02/10/15.
 */
public class LoadNotesEvent extends RetryEvent {

    public int mStart;

    public int mCount;

    public LoadNotesEvent(int start, int count) {
        mStart = start;
        mCount = count;
    }

}
