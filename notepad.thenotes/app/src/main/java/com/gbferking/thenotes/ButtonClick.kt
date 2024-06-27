package com.gbferking.thenotes

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.view.View
import java.util.Timer
import java.util.TimerTask

class ButtonClick {
    /*
           Criar efeito de Click em Botão em Views
           Use:
               ButtonClick btnclck = new ButtonClick();
           Atribua o evento à view desjeada:
               btnclick.setClick("LinearLayout, RelativeLayout Desejada" linear_button_save, "Activity em uso" LoginActivity.this);

     */
    private val timer = Timer()
    private var task: TimerTask? = null
    private var c = 0

    private fun click(view: View, activity: Activity) {
        c++
        if (c == 1) {
            val bg = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(-0x262aa5, -0x14c5)
            )
            bg.cornerRadius = 80f
            bg.setStroke(3, -0xa63)
            view.elevation = 8f
            view.background = bg
        }
        task = object : TimerTask() {
            override fun run() {
                activity.runOnUiThread {
                    val bg = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(-0x14c5, -0x262aa5)
                    )
                    bg.cornerRadius = 80f
                    bg.setStroke(3, -0xa63)
                    view.elevation = 2f
                    view.background = bg
                }
            }
        }
        timer.schedule(task, 200)
    }

    fun setClick(v: View, act: Activity) {
        click(v, act)
    }
}
