package com.gbferking.thenotes.Models;

//usando a biblioteca Room

//IMPORTAÇÕES:
//Estudar informações de todas as importações, pontos fortes e fracos
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;


/*PRIMEIRA PARTE DO PROJETO:
* IMPORTAR O Serializable
* ADD @Entity
* ADD @PrimaryKey
* */

//verificar essa linha de codigo: @entity
@Entity(tableName = "notes") //TABELA QUE @Query busca;
//verificar significado de implements novamente.
public class Notes implements Serializable {

    @PrimaryKey(autoGenerate = true) //2ªpasso criar
    int ID = 0;  //1ªpasso criarw

    /*
    Primeiro vamos criar uma String: title para o Titulo do bloco de notas.
    Segundo criamos uma String: notes para receber as informações dentro da nota.
    Terceiro criamos uma String: Date para mostrar a data da nota adicionada.
    Quarto vamos criar um Boolean: pinned para fixar as notas que eu quiser fixar no app
    * */

    @ColumnInfo(name = "title") //2ªpasso criar
    String title = ""; //1ªpasso criar

    @ColumnInfo(name = "notes") //2ªpasso criar
    String notes = ""; //1ªpasso criar

    @ColumnInfo(name = "date")//2ªpasso criar
    String date = "";//1ªpasso criar

    @ColumnInfo(name = "pinned")
    boolean pinned = false;

    //---------------------------------------------------------------------

    //Agora vamos criar os Getters e Setters dessas variaveis;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
