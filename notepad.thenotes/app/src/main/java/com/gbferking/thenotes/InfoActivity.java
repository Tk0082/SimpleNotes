package com.gbferking.thenotes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView mail = findViewById(R.id.txvmail);
        TextView git = findViewById(R.id.txvgit);
        TextView play = findViewById(R.id.txvplay);

        mail.setOnClickListener(view ->{
            i = new Intent(Intent.ACTION_SENDTO);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setType("text/plain");
            i.setData(Uri.parse("mailto:"+mail.getText()));
            i.putExtra(Intent.EXTRA_TEXT,"Envie seu Feedback..");
            startActivity(Intent.createChooser(i, "Feedback TheNotes"));
            finish();
        });

        git.setOnClickListener(view ->{
            String url = "https://github.com/bygabrielferreira/SimpleNotes";
            i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });

        play.setOnClickListener(view ->{
            String url = "https://play.google.com/store/apps/details?id=com.gbferking.thenotes";
            i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });
    }
}