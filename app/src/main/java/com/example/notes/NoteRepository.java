package com.example.notes;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        });

    }

    public void update(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });

    }

    public void delete(Note note) {
        NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
            }
        });

    }

    public void deleteAllNotes() {
    NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() {
            noteDao.deleteAllNotes();
        }
    });
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


}
