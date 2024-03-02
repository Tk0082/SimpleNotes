package com.gbferking.thenotes.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
//import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gbferking.thenotes.Models.Notes;
import com.gbferking.thenotes.NotesClickListener;
import com.gbferking.thenotes.R; //importar classe R.id

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//----------------------------------------------4º IMPLEMENTANDO METODOS-------------------------
public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    //5º criando esses objetos;
    Context context;
    List<Notes> list;
    //-------------------------
    //6º após criar a interface NotesClickListener add esses objetos
    NotesClickListener listener;

    //7ºcriar os construtores desses objetos -----------------------------------------------
    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    //----------------------------------------------------------------------------------

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //8º remover o 'null' e add novos parametros;
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list,
                parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        //10º adicionar esses parametros;
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_notes.setText(list.get(position).getNotes());

        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);

        //11º add a logica de fixar a nota;
        if(list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.thenotes);
        }else{
            holder.imageView_pin.setImageResource(0);
        }
        //13º add e tentar entender esse trecho e o de baixo
        int color_code = getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));
        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        //14º add e tentar entender esse trecho e o de cima
        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });
    }

    //12ºadicionar logica de cores diferentes(aleatorias) ao fixar a nota
    //obs. pegar cores de colors.xml
    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();

        //importando cores
        colorCode.add(R.color.white);


        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        //9º remover 0 e add outro parametro
        return list.size();
    }

    //1º=========================adicionando logica da barra de busca #search
    public void filterList(List<Notes> filterEdList){
        list = filterEdList;
        notifyDataSetChanged();

    }
    //1º=========================adicionando logica da barra de busca #search


}



//----------------------------------------------4º IMPLEMENTANDO METODOS-------------------------

//1º criar uma classe
class NotesViewHolder extends RecyclerView.ViewHolder {

    //3º criar um cardview add notes_container de notes_list.xml
    CardView notes_container;//<===pegar esse componente

    //4º criar o textview e pegar informações em notes_list.xml e trazer.
    TextView textView_title, textView_notes, textView_date;//<===pegar esses componentes

    //5º criar o ImageView e pegar informações em notes_list.xml e trazer.
    ImageView imageView_pin; //<===pegar esse componente

//2º criar um construtor
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        //3º criar o findViewByID para referenciar os objetos
        notes_container = itemView.findViewById(R.id.notes_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_date = itemView.findViewById(R.id.textView_date);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);



    }
}
