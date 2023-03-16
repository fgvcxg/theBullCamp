package com.example.thebullcamp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.thebullcamp.MainActivity
import com.example.thebullcamp.R
import com.example.thebullcamp.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {



            val email = binding.emailArea.text.toString()
            val pass1 = binding.passArea1.text.toString()
            val pass2 = binding.passArea2.text.toString()

            //값 비어있는지 확인
            if(email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해라", Toast.LENGTH_SHORT).show()
            }
            else if(pass1.isEmpty()){
                Toast.makeText(this, "비밀번호를 입력해라", Toast.LENGTH_SHORT).show()
            }
            else if(pass2.isEmpty()){
                Toast.makeText(this, "비밀번호 확인을 입력해라", Toast.LENGTH_SHORT).show()
            }
            else if(pass1.length < 6){
                Toast.makeText(this, "비밀번호는 6자리 이상", Toast.LENGTH_SHORT).show()
            }
            else if(!pass1.equals(pass2)){
                Toast.makeText(this, "비밀번호를 똑같이 입력해라", Toast.LENGTH_SHORT).show()
            }
            else{

                auth.createUserWithEmailAndPassword(email, pass1)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   Toast.makeText(this, "성공",Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    //메인으로 갔을때 뒤로 가기를 누르면 꺼지도록 하는 것
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "실패",Toast.LENGTH_SHORT).show()

                }
            }
            }
            
            
        }



    }
}