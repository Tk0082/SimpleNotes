package com.gbferking.thenotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gbferking.thenotes.Adapters.NotesListAdapter;
import com.gbferking.thenotes.Database.RoomDB;
import com.gbferking.thenotes.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

                                                //último passo: vamos implementar para o PopupMenu
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    //1º passo:
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;

    //1º----------------------------------------para selecionar notas
    Notes selectedNote;
    //1º----------------------------------------para selecionar notas

    //1º=========================adicionando logica da barra de busca #search
    SearchView searchView_home;
    //1º=========================adicionando logica da barra de busca #search

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);

        //2º=========================adicionando logica da barra de busca #search
        searchView_home = findViewById(R.id.searchView_home);
        //2º=========================adicionando logica da barra de busca #search


        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();

        updateRecycler(notes);

        //6º adicionar o bottao flutuante e criar uma activity, depois adicionamos.
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent, 101);

            }
        });

        //3º=========================adicionando logica da barra de busca #search
        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        //3º=========================adicionando logica da barra de busca #search


    }
    //4º=========================adicionando logica da barra de busca #search
    private void filter(String newText) {
        List<Notes> filterEdList = new ArrayList<>();
        for(Notes singleNotes : notes){
            if(singleNotes.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNotes.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterEdList.add(singleNotes);
            }
        }
        //pegando da classe adapter
        notesListAdapter.filterList(filterEdList);
    }
    //4º=========================adicionando logica da barra de busca #search


    //apos implementar a classe NOTESTAKERACTIVITY, PASSO 7º. ADD ESSES PARAMENTROS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if(resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
            //--------------------------------------------função para entrar dentro da nota
        } else if(requestCode == 102){ //ele altera as info dentro da nota já criada
            if(resultCode==Activity.RESULT_OK){ //mostra as novas info add ou alteradas
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
            //---------------------------------------------------função para entrar dentro da nota
        }

    }


    //2ºPasso
    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);

        //5ºpasso add diferentes tamanhos de notas
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, LinearLayoutManager.VERTICAL));

        //add notesClickListener após o 3ºPasso
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);

        //4ºpasso
        recyclerView.setAdapter(notesListAdapter);
    }
    //3ªpasso
    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {

            //-------------------------------------função para entrar dentro da nota
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }
            //-------------------------------------------função para entrar dentro da nota

        public void onLongClick(Notes notes, CardView cardView) {
            //2º----------------------------------------para selecionar notas
            selectedNote = new Notes();
            selectedNote = notes;
            showPopUp(cardView);
            //2º----------------------------------------para selecionar notas
        }
    };

    //3º----------------------------------------para selecionar notas
    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }
    //3º----------------------------------------para selecionar notas


    //4º----------------------------------------para selecionar notas
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedNote.isPinned()){
                    database.mainDAO().pin(selectedNote.getID(), false);
                    Toast.makeText(MainActivity.this, "Removido!", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.mainDAO().pin(selectedNote.getID(), true);
                    Toast.makeText(MainActivity.this, "Fixado!", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                database.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Nota Excluída", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
   }
   //4º----------------------------------------para selecionar notas

}