package com.example.everartstudio

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.view_order.*


class ViewOrder : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_order)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menuAddProduct->{
                startActivity(Intent(this@ViewOrder,AddProduct::class.java))
                return true
            }
            R.id.menuViewProduct->{
                return true
            }
            R.id.menuQuery->{
                return true
            }
            R.id.menuLogout->{
                startActivity(Intent(this@ViewOrder,Login::class.java))
                return true
            }
            else->super.onOptionsItemSelected(item)
        }

    }
}