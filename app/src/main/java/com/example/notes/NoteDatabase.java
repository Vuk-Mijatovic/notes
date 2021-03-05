package com.example.notes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public static NoteDatabase instance;

    public abstract NoteDao noteDao();

    private static final int NUMBER_OF_THREADS = 1;

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .addCallback(roomCallback).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }
    }

//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
//                NoteDao noteDao;
//                @Override
//                public void run() {
//
//                    noteDao = instance.noteDao();
//                    if (noteDao != null)
//                        noteDao.insert(new Note("note1", "description1", 1));
//                        noteDao.insert(new Note("note2", "description2", 2));
//                        noteDao.insert(new Note("note3", "description3", 3));
//                                    }
//            });
//        }
//    };
}



