package com.gbferking.thenotes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gbferking.thenotes.Models.Notes;
import com.gbferking.thenotes.NotesClickListener;
import com.gbferking.thenotes.R;

import java.util.List;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.ViewHolder> {
    Context context;
    List<Notes> listpin;
    NotesClickListener listener;
    
    public PinAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.listpin = list;
        this.listener = listener;
    }
    @NonNull
    @Override
    public PinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.pin_list, parent, false));
    }
    
    @Override
    public void onBindViewHolder(@NonNull PinAdapter.ViewHolder holder, int position) {
        holder.textView_title.setText(listpin.get(position).getTitle());
        holder.textView_title.setSelected(true);
        
        holder.textView_notes.setText(listpin.get(position).getNotes());
        holder.textView_date.setText(listpin.get(position).getDate());
        
        holder.lcard.setBackgroundResource(R.drawable.back_note_card_selected);
        
        if (listpin.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.thenotes);
            holder.itemView.setSelected(true);
        } else {
            holder.imageView_pin.setImageResource(0);
            holder.itemView.setSelected(false);
        }
        
        holder.notes_container.setOnClickListener(view -> listener.onClick(listpin.get(holder.getAdapterPosition())));
        
        //14º add e tentar entender esse trecho e o de cima
        holder.notes_container.setOnLongClickListener(view -> {
            listener.onLongClick(listpin.get(holder.getAdapterPosition()), holder.notes_container);
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return listpin.size();
    }
    
    public void filterPinList(List<Notes> filterEdPinList){
        this.listpin = filterEdPinList;
        notifyDataSetChanged();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView notes_container;
        TextView textView_title, textView_notes, textView_date;
        ImageView imageView_pin;
        LinearLayout lcard;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_container = itemView.findViewById(R.id.notespin_container_pin);
            textView_title = itemView.findViewById(R.id.textView_title_pin);
            textView_notes = itemView.findViewById(R.id.textView_notes_pin);
            textView_date = itemView.findViewById(R.id.textView_date_pin);
            imageView_pin = itemView.findViewById(R.id.imageview_pin);
            lcard = itemView.findViewById(R.id.card_lback_pin);
        }
    }
}
