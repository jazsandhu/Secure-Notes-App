package com.example.securenotesapp;

import java.util.ArrayList;
import java.util.List;

public class NotesListManager {

    private List<Notes> items;

    public NotesListManager() {
        items = new ArrayList<Notes>();

        items.add(new Notes("School Notes"));
        items.add(new Notes("Grocery List"));
        items.add(new Notes("D&D Notes"));
    }

    public List<Notes> getNotes() {
        return items;
    }

    public void addNote(Notes item) {
        items.add(item);
    }
}