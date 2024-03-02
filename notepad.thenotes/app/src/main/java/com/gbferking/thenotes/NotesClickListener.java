package com.gbferking.thenotes;

import androidx.cardview.widget.CardView;

import com.gbferking.thenotes.Models.Notes;

public interface NotesClickListener {

    //1ºmetodo de captura
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
