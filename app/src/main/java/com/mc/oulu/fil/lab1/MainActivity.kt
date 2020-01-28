package com.mc.oulu.fil.lab1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fabOpened = false

        ButtonMain.setOnClickListener{
            if(!fabOpened) {
                fabOpened = true
                ButtonMap.animate().translationY(-resources.getDimension(R.dimen.standard_66))
                ButtonTime.animate().translationY(-resources.getDimension(R.dimen.standard_116))
            }
            else{
                ButtonMap.animate().translationY(0.0f)
                ButtonTime.animate().translationY(0.0f)
                fabOpened = false
            }
        }

        ButtonMap.setOnClickListener{
            startActivity(Intent(applicationContext, MapActivity::class.java))
        }
        ButtonTime.setOnClickListener{
            startActivity(Intent(applicationContext, TimeActivity::class.java))
        }
    }
}
