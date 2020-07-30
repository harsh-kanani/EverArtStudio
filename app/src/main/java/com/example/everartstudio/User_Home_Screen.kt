package com.example.everartstudio

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_user__home__screen.*


class User_Home_Screen : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener{

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawer_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_main)

        var sp  = getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var user = sp.getString("uid","null")
        Toast.makeText(this@User_Home_Screen,user.toString(),Toast.LENGTH_LONG).show()


        val navView  =  findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Users").child(user.toString())

        // Read from the database

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //val value =dataSnapshot.getValue(String::class.java)!!
                var value = dataSnapshot.child("name").value
                var hv: View =navView.getHeaderView(0)
                var txt = hv.findViewById<TextView>(R.id.nav_top_name)
                txt.text = value.toString().toUpperCase()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })

        /*var hv:View =navView.getHeaderView(0)
        var txt = hv.findViewById<TextView>(R.id.nav_top_name)
        txt.text = "abc".toUpperCase()

         */

        val toogle = ActionBarDrawerToggle(this@User_Home_Screen,drawerLayout,toolbar,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Fragment_Category()).commit()

        }
        menuCartBtn.setOnClickListener {
            //supportFragmentManager.beginTransaction()
            //    .replace(R.id.fragment_container, Fragment_Cart()).commit()
            var fragmentManager: FragmentManager = this@User_Home_Screen.supportFragmentManager
            var fragmentTransaction: FragmentTransaction =  fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,Fragment_Cart())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_home->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Fragment_Category()).commit()

            R.id.nav_profile->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Fragment_Profile()).commit()


        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}