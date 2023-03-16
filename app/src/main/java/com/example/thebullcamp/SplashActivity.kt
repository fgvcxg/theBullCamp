package com.example.thebullcamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.thebullcamp.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        //로그인이 되어 있으면 자동으로 넘어가기
        if(auth.currentUser?.uid  == null){
            Log.d("SplashActivity", "null")

            Toast.makeText(this, "처음오셨습니다 ",Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }, 2000)

        }else{
            Log.d("SplashActivity", "not null")

            Toast.makeText(this, "회원님 환영합니다",Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2000)
        }

        //스플래쉬 화면이 나오고 2초후에 넘어감
//        Handler().postDelayed({
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }, 2000)

    }
}