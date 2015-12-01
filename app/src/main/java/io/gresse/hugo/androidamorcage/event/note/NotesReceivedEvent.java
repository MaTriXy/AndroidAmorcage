package io.gresse.hugo.androidamorcage.event.note;

import io.gresse.hugo.androidamorcage.model.Note;
import io.realm.RealmList;

/**
 * When many note has been received
 *
 * Created by Hugo Gresse on 12/10/15.
 */
public class NotesReceivedEvent {

    public RealmList<Note> mNotes;

    public NotesReceivedEvent(RealmList<Note> notes) {
        mNotes = notes;
    }
}
