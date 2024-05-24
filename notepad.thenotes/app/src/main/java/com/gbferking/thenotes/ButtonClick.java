package com.gbferking.thenotes;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class ButtonClick {

    /*
           Criar efeito de Click em Botão em Views
           Use:
               ButtonClick btnclck = new ButtonClick();
           Atribua o evento à view desjeada:
               btnclick.setClick("LinearLayout, RelativeLayout Desejada" linear_button_save, "Activity em uso" LoginActivity.this);

     */

    private Timer timer = new Timer();
    private TimerTask task;
    private int c = 0;

    private void click(View view, Activity activity){
        c++;
        if (c == 1){
            GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFD9D55B, 0xFFFFEB3B});
            bg.setCornerRadius(80);
            bg.setStroke(3, 0xFFFFF59D);
            view.setElevation(8);
            view.setBackground(bg);
        }
        task = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFFFEB3B, 0xFFD9D55B});
                        bg.setCornerRadius(80);
                        bg.setStroke(3, 0xFFFFF59D);
                        view.setElevation(2);
                        view.setBackground(bg);
                    }
                });
            };
        };
        timer.schedule(task, 200);
    }

    public void setClick(View v, Activity act){
        click(v,act);
    }
}
