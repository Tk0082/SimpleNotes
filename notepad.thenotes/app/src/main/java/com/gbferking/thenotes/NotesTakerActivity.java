package com.gbferking.thenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gbferking.thenotes.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {
    //1º passo, pegar os objetos;
    EditText editText_title, editText_notes;
    ImageView imageView_save;
    RelativeLayout lbtn_save;
    Notes notes;
    //--------------------------função para entrar dentro da nota
    boolean isOldNote = false;
    //--------------------------função para entrar dentro da nota
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        //2º passo, pegar os id
        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        lbtn_save = findViewById(R.id.bck_btn);


        //------------------------------------------------função para entrar dentro da nota
        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            isOldNote = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        //-----------------------------------função para entrar dentro da nota


        //3ºpasso
        lbtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Evento de click no Botão Salvar(RelativeLayout)
                ButtonClick click = new ButtonClick();
                click.setClick(lbtn_save, NotesTakerActivity.this);

                String title = editText_title.getText().toString();
                String description = editText_notes.getText().toString();

                //4º passo
                if(description.isEmpty()){
                    Toast.makeText(NotesTakerActivity.this, "Por favor, add uma nota!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //5ªpasso
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                //=================================função para entrar dentro da nota

                if(!isOldNote){
                    notes = new Notes();
                }

                //==================================função para entrar dentro da nota


                //6ªpasso
                //notes = new Notes(); coloquei dentro do if acima;
                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));

                //7ª passo
                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    };
}