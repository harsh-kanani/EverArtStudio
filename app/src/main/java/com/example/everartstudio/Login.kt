package com.example.everartstudio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        lblrg.setOnClickListener {
            startActivity(Intent(this@Login,Registration::class.java))
        }

        lblforgot.setOnClickListener {
            startActivity(Intent(this@Login,Forgot_Password::class.java))
        }

        btnlogin.setOnClickListener {
            if (txtemail.text.toString().equals("admin@gmail.com") && txtpassword.text.toString().equals("admin123")){
                startActivity(Intent(this@Login,ViewOrder::class.java))
                finish()
            }else{
                mAuth!!.signInWithEmailAndPassword(txtemail.text.toString(),txtpassword.text.toString())
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = mAuth!!.currentUser
                            var sp=getSharedPreferences("MySp",Activity.MODE_PRIVATE)
                            var edt=sp.edit()
                            edt.putString("uid","${user!!.uid}")
                            edt.apply()
                            edt.commit()
                            Toast.makeText(this@Login,"Successfully Login",Toast.LENGTH_LONG).show()
                            //Toast.makeText(this@Login,user!!.uid,Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@Login,User_Home_Screen::class.java))
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.


                            Toast.makeText(
                                this@Login, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        // ...
                    }
            }
        }
    }
}