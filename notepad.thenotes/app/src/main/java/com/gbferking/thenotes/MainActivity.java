package com.gbferking.thenotes;

import static com.gbferking.thenotes.R.id.pin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    ImageView info;
    Intent i;
    PopupMenu popupMenu;
    LinearLayout backcard;

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
        info = findViewById(R.id.icinfo);

        //2º=========================adicionando logica da barra de busca #search
        searchView_home = findViewById(R.id.searchView_home);
        searchView_home.clearFocus();
        //2º=========================adicionando logica da barra de busca #search

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();

        updateRecycler(notes);

        //6º adicionar o bottao flutuante e criar uma activity, depois adicionamos.
        fab_add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            startActivityForResult(intent, 101);
        });

        //3º=========================adicionando logica da barra de busca #search
        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
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

        /* ACTIVITY DE INFORMAÇÃO */
        info.setOnClickListener(view->{
            i = new Intent(this, InfoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        /* AÇÃO DE SEMPRE APRESENTAR A RECYCLERVIEW NO TOPO */
        if (recyclerView.getY() != 0.0){
            recyclerView.scrollToPosition(0);
        }
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

        if(filterEdList.isEmpty()){
            showMessage("Nenhuma nota encontrada!");
        } else {
            //pegando da classe adapter
            notesListAdapter.filterList(filterEdList);
        }

        notesListAdapter.notifyDataSetChanged();
    }
    //4º=========================adicionando logica da barra de busca #search


    //apos implementar a classe NOTESTAKERACTIVITY, PASSO 7º. ADD ESSES PARAMENTROS
    @SuppressLint("NotifyDataSetChanged")
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
        //---------------------------função para entrar dentro da nota
        } else if (requestCode == 102){ //ele altera as info dentro da nota já criada
            if(resultCode==Activity.RESULT_OK){ //mostra as novas info add ou alteradas
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        //---------------------------função para entrar dentro da nota
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
            //noinspection deprecation
            startActivityForResult(intent, 102);
        }
            //-------------------------------------função para entrar dentro da nota

        public void onLongClick(Notes notes, CardView cardView) {
            //2º----------------------------------------para selecionar notas
            //selectedNote = new Notes();
            selectedNote = notes;
            showPopUp(cardView);
            //2º----------------------------------------para selecionar notas
        }
    };

    //3º----------------------------------------para selecionar notas
    private void showPopUp(CardView cardView) {
        popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
    //3º----------------------------------------para selecionar notas


    //4º----------------------------------------para selecionar notas
    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedNote.isPinned()){
                    database.mainDAO().pin(selectedNote.getID(), false);
                    showMessage("Removido!");
                }
                else{
                    database.mainDAO().pin(selectedNote.getID(), true);
                    showMessage("Fixado!");
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
            case R.id.share:
                String name = "TheNotes\nhttps://play.google.com/store/apps/details?id=com.gbferking.thenotes\n\n";
                String note = name+ selectedNote.getTitle() +"\n\n"+ selectedNote.getNotes();
                String title = "Compartilhar: "+ selectedNote.getTitle();
                i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TITLE, title);
                i.putExtra(Intent.EXTRA_TEXT, note );
                startActivity(Intent.createChooser(i, null));
                return true;
            default:
                return false;
        }
   }
   //4º----------------------------------------para selecionar notas

   private void showMessage(String s) {
       Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
   }

   @Override
   protected void onRestart() {
       recyclerView.scrollToPosition(0);
       super.onRestart();
   }

   @Override
   protected void onResume() {
       recyclerView.scrollToPosition(0);  // Sempre retornar visualização do topo da RecyclerView
       super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}