package com.example.trello.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.example.trello.R

class IntroActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btn_sign_up_intro : Button= findViewById(R.id.btn_sign_up_intro)
        val btn_sign_in_intro : Button= findViewById(R.id.btn_sign_in_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        btn_sign_up_intro.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        })
        btn_sign_in_intro.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        })
    }
}