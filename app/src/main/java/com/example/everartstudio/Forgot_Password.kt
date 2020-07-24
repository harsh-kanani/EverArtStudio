package com.example.everartstudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot__password.*

class Forgot_Password : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot__password)

        mAuth = FirebaseAuth.getInstance()

        btnsub.setOnClickListener {
            mAuth!!.sendPasswordResetEmail(txtfemail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@Forgot_Password,"Email sended On Your Register Email...", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@Forgot_Password,Login::class.java))
                        finish()
                    }
                }

        }
    }
}