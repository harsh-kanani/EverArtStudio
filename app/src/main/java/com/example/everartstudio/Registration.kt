package com.example.everartstudio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*


class Registration : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Users")



        lbllg.setOnClickListener {
            startActivity(Intent(this@Registration,Login::class.java))
            finish()
        }
        btnreg.setOnClickListener {
            if(txtmail.text.toString() == "" || txtpwd.text.toString() == "" || txtcpwd.text.toString()=="" || txtmono.text.toString() == "" || txtunm.text.toString() == ""){
                Toast.makeText(this@Registration,"Blank Input Is Not Allow !!",Toast.LENGTH_LONG).show()

            }else{
                if (txtpwd.text.toString().equals(txtcpwd.text.toString())) {
                    mAuth!!.createUserWithEmailAndPassword(
                        txtmail.text.toString(),
                        txtpwd.text.toString()
                    )
                        .addOnCompleteListener(
                            this
                        ) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information

                                val user = mAuth!!.currentUser
                                user!!.sendEmailVerification().addOnCompleteListener {
                                    Toast.makeText(
                                        this@Registration,
                                        "Verification Email Sended To Your Mail !!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                var userdata = UserDataClass(
                                    user!!.uid,
                                    txtunm.text.toString(),
                                    txtmail.text.toString(),
                                    txtpwd.text.toString(),
                                    txtmono.text.toString()
                                )
                                myRef.child(userdata.uid).setValue(userdata).addOnCompleteListener {
                                    Toast.makeText(
                                        this@Registration,
                                        "Successfully Register",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(Intent(this@Registration, Login::class.java))
                                    finish()
                                }

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(
                                    this@Registration, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            // ...
                        }
                } else {
                    Toast.makeText(this@Registration, "Password is not match", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}