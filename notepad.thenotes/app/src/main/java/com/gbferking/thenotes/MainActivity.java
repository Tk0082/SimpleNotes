package com.gbferking.thenotes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gbferking.thenotes.Adapters.NotesListAdapter;
import com.gbferking.thenotes.Adapters.PinAdapter;
import com.gbferking.thenotes.Database.RoomDB;
import com.gbferking.thenotes.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

                    //último passo: vamos implementar para o PopupMenu
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    //1º passo:
    RecyclerView recyclerView;
    RecyclerView recyclerPin;
    NotesListAdapter notesListAdapter;
    PinAdapter pinAdapter;                    // Adapter das notas pinadas
    List<Notes> notes = new ArrayList<>();
    List<Notes> notesPin = new ArrayList<>(); // Notas pinadas
    RoomDB database;
    FloatingActionButton fab_add;
    ImageView info;
    Intent i;
    PopupMenu popupMenu;
    TextView tx_cont;      // Contador das notas pinadas
    String num_pined;      // Numero passado ao contador

    //1º----------------------------------------para selecionar notas
    Notes selectedNote;
    //1º----------------------------------------para selecionar notas

    //1º=========================adicionando logica da barra de busca #search
    SearchView searchView_home;
    //1º=========================adicionando logica da barra de busca #search
    
    @Deprecated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        recyclerPin = findViewById(R.id.recyclerPin);
        fab_add = findViewById(R.id.fab_add);
        info = findViewById(R.id.icinfo);
        tx_cont = findViewById(R.id.tx_pin_cont);

        //2º=========================adicionando logica da barra de busca #search
        searchView_home = findViewById(R.id.searchView_home);
        searchView_home.clearFocus();
        //2º=========================adicionando logica da barra de busca #search

        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();
        
        notesPin = database.mainDAO().getPinned();
        
        if (notesPin.isEmpty()) {
            recyclerPin.setVisibility(View.GONE);
            tx_cont.setVisibility(View.GONE);
        } else {
            recyclerPin.setVisibility(View.VISIBLE);
            tx_cont.setVisibility(View.VISIBLE);
            updateRecycler(notesPin);
            num_pined = String.valueOf(database.mainDAO().getPinned().size());
            tx_cont.setText(num_pined);
        }
        
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
                hideKeyboard(searchView_home);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        
        searchView_home.setOnCloseListener(() -> {
            hideKeyboard(searchView_home);
            return false;
        });
        
        searchView_home.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
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
            recyclerPin.scrollToPosition(0);
        }
    }

    //4º=========================adicionando logica da barra de busca #search
    private void filter(String newText) {
        // LISTAS SEPARADAS PARA FILTRAGEM DIRETA EM CADA ADAPTER
        List<Notes> filterEdList = new ArrayList<>();
        List<Notes> filterEdPinList = new ArrayList<>();
        
        // FILTRO EM NOTAS NORMAIS
        for(Notes singleNotes : notes){
            if(singleNotes.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNotes.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterEdList.add(singleNotes);
            }
            //pegando da classe adapter
            notesListAdapter.filterList(filterEdList);
            notesListAdapter.notifyDataSetChanged();
        }
        
        // FILTRO EM NOTAS PINADAS
        for(Notes singleNotes : notesPin){
            if(singleNotes.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNotes.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterEdPinList.add(singleNotes);
            }
            if (filterEdPinList.isEmpty()) {
                recyclerPin.setVisibility(View.GONE);
                tx_cont.setVisibility(View.GONE);
            } else {
                recyclerPin.setVisibility(View.VISIBLE);
                tx_cont.setVisibility(View.VISIBLE);
            }
            // Pegando o Adapter PinAdapter
            pinAdapter.filterPinList(filterEdPinList);
            pinAdapter.notifyDataSetChanged();
        }
        
        // Caso nenhuma lista tenha correspondência, mostra mensagem
        if(filterEdList.isEmpty() && filterEdPinList.isEmpty()){
            hideKeyboard(searchView_home);
            showMessage("Nenhuma nota encontrada!");
        }
    }
    //4º=========================adicionando logica da barra de busca #search
    public void  hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
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
                notesPin.clear();
                notes.addAll(database.mainDAO().getAll());
                notesPin.addAll(database.mainDAO().getPinned());
                
                notesListAdapter.notifyDataSetChanged();
                pinAdapter.notifyDataSetChanged();
                
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
        
        // Atualização das notas pinadas
        recyclerPin.setHasFixedSize(true);
        recyclerPin.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        pinAdapter = new PinAdapter(this, notesPin, notesClickListener);
        recyclerPin.setAdapter(pinAdapter);
        
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
        try {
            popupMenu = new PopupMenu(this, cardView);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.setGravity(Gravity.CENTER);
            popupMenu.inflate(R.menu.popup_menu);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            popupMenu.show();
        }
    }
    //3º----------------------------------------para selecionar notas


    //4º----------------------------------------para selecionar notas
    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if (selectedNote.isPinned()) {
                    database.mainDAO().pin(selectedNote.getID(), false);
                    showMessage(getString(R.string.tx_pin_removido));
                } else {
                    database.mainDAO().pin(selectedNote.getID(), true);
                    showMessage(getString(R.string.tx_pin_fixado));
                }
                // Limpa e atualiza os dois Querys(Notas simples e Pinadas)
                notes.clear();
                notesPin.clear();
                notes.addAll(database.mainDAO().getAll());
                notesPin.addAll(database.mainDAO().getPinned());
                num_pined = String.valueOf(database.mainDAO().getPinned().size()); // Recebe o valor atualizado de notas pinadas
                
                if (notesPin.isEmpty()){  // Se estiver vazio, SOME!
                    tx_cont.setVisibility(View.GONE);
                    recyclerPin.setVisibility(View.GONE);
                } else {
                    tx_cont.setVisibility(View.VISIBLE);
                    tx_cont.setText(num_pined);
                    recyclerPin.setVisibility(View.VISIBLE);
                    pinAdapter.notifyDataSetChanged();
                    updateRecycler(notesPin);
                    notesListAdapter.notifyDataSetChanged();
                    updateRecycler(notes);
                }
                pinAdapter.notifyDataSetChanged();
                updateRecycler(notesPin);
                notesListAdapter.notifyDataSetChanged();
                updateRecycler(notes);
                return true;
            case R.id.delete:
                database.mainDAO().delete(selectedNote);
                showMessage(getString(R.string.tx_nota_excluida));
                notes.clear();
                notesPin.clear();
                notes.addAll(database.mainDAO().getAll());
                notesPin.addAll(database.mainDAO().getPinned());
                num_pined = String.valueOf(database.mainDAO().getPinned().size());
                tx_cont.setText(num_pined);
                if(selectedNote.isPinned()) {
                    if(notesPin.isEmpty()){
                        tx_cont.setVisibility(View.GONE);
                        recyclerPin.setVisibility(View.GONE);
                        pinAdapter.notifyDataSetChanged();
                        updateRecycler(notesPin);
                        notesListAdapter.notifyDataSetChanged();
                        updateRecycler(notes);
                    }
                }
                pinAdapter.notifyDataSetChanged();
                updateRecycler(notesPin);
                notesListAdapter.notifyDataSetChanged();
                updateRecycler(notes);
                return true;
            case R.id.share:
                String name = "TheNotes\nhttps://play.google.com/store/apps/details?id=com.gbferking.thenotes\n\n";
                String note = name.concat(selectedNote.getTitle()).concat("\n\n").concat(selectedNote.getNotes());
                String title = getString(R.string.tx_compartilhar).concat(selectedNote.getTitle());
                i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TITLE, title);
                i.putExtra(Intent.EXTRA_TEXT, note);
                startActivity(Intent.createChooser(i, null));
                return true;
            default:
                return false;
        }
   }
   //4º----------------------------------------para selecionar notas
   
   // Chamar passando o texto desejado no parâmetro
   private void showMessage(String s) {
       Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
   }

   @Override
   protected void onRestart() {
       notesListAdapter.notifyDataSetChanged();
       pinAdapter.notifyDataSetChanged();
       num_pined = String.valueOf(database.mainDAO().getPinned().size());
       // Sempre retornar visualização do topo da RecyclerView
       recyclerView.scrollToPosition(0);
       recyclerPin.scrollToPosition(0);
       super.onRestart();
   }
   
   @Override
   protected void onResume() {
        notesListAdapter.notifyDataSetChanged();pinAdapter.notifyDataSetChanged();
        num_pined = String.valueOf(database.mainDAO().getPinned().size());
        recyclerView.scrollToPosition(0);
        recyclerPin.scrollToPosition(0);
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