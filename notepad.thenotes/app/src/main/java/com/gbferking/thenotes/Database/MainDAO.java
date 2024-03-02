package com.gbferking.thenotes.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.gbferking.thenotes.Adapters.NotesListAdapter;
import com.gbferking.thenotes.MainActivity;
import com.gbferking.thenotes.Models.Notes;

import java.util.List;

//1º IMPORTAR DAO colocando @Dao
@Dao

public interface MainDAO {

    //3º add @Insert
    @Insert(onConflict = REPLACE) //ALT + ENTER para ver opções

    //2º fazer o insert da classe Notes
    void insert(Notes notes);
    //--------------------------------------------------------------------------------DATABASE
    //3ºCRIAR UMA LISTA para buscar tudo
    //4ºadicionar @Query e dentro add um SELECT para buscar na TABELA; dentro da classe Notes
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();

    //ESTAMOS BUSCANDO ESSAS INFORMAÇÕES NA VARIAVEL ID, DENTRO DA CLASSE NOTES
    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    //5ºMETODO DE UPDATE E ADD @QUERY ACIMA
    void update(int id, String title, String notes);
    //------------------------------------------------------DATABASE

    @Delete
    //CRIAR METODO PARA DELETAR NOTES
    void delete(Notes notes);

    //CRIAR METODO PARA FIXAR NOTA
    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);

}
