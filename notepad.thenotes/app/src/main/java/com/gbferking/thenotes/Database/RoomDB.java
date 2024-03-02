package com.gbferking.thenotes.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gbferking.thenotes.Models.Notes;

//1º @Database, pesquisar mais sobre version =1, exportSchema
@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    //2º declarar classe como static, como se database fosse uma variavel do tipo RoomDB
    private static RoomDB database;
    //3º nova variavel que recebe o nome do projeto
    private static String DATABASE_NAME = "TheNotes";

    //4º criar uma Istância que sincronize o DATABASE ------------------------------------------
    public synchronized static RoomDB getInstance(Context context) {
        if(database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    //---------------------------------------------------------------------ENTENDER ESSA PARTE


    public abstract MainDAO mainDAO();
}
