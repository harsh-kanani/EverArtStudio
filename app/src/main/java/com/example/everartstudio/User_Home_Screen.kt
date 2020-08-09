package com.example.everartstudio

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_user__home__screen.*
import java.util.*


class User_Home_Screen : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener{

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawer_layout)
    }
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null )
        {
            val navView  =  findViewById<NavigationView>(R.id.nav_view)
            navView.setNavigationItemSelectedListener(this@User_Home_Screen)
            var vw: View =navView.getHeaderView(0)
            var img = vw.findViewById<ImageView>(R.id.img_profile)

            filePath=data.data
            var bitmap= MediaStore.Images.Media.getBitmap(contentResolver,filePath)
            img.setImageBitmap(bitmap)
            var gsp  = getSharedPreferences("MySp",Activity.MODE_PRIVATE)
            var uid = gsp.getString("uid","null")
            uploadImage(uid.toString())
        }
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

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
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

        var myRef2 = database.getReference("Image").child(user.toString())
        var vw: View =navView.getHeaderView(0)
        var img = vw.findViewById<ImageView>(R.id.img_profile)
        img.setOnClickListener {

                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)




        }
        myRef2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var value = dataSnapshot.child("img").value

                if(value != null) {
                    val storage = FirebaseStorage.getInstance()
                    val storageReference = storage.getReferenceFromUrl(value.toString())
                    storageReference.downloadUrl.addOnSuccessListener {
                        val imgurl = it.toString()
                        Glide.with(this@User_Home_Screen).load(imgurl).into(img).view
                    }
                }
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
    private fun logout(){
        var mAuth: FirebaseAuth? = null
        mAuth = FirebaseAuth.getInstance()
        mAuth!!.signOut()


        var sp = getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var edt = sp.edit()
        edt.putString("uid","null")
        edt.apply()
        edt.commit()
        startActivity(Intent(this@User_Home_Screen,Login::class.java))
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_home->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Fragment_Category()).commit()

            R.id.nav_profile->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Fragment_Profile()).commit()
            R.id.nav_myorder->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Fragment_MyOrder()).commit()
            R.id.nav_wishlist->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Fragment_Wishlist()).commit()
            R.id.nav_customOrder->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Fragment_Custom_Order()).commit()
            R.id.nav_gallary->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Fragment_Gallary()).commit()
            R.id.nav_contactUs->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Fragment_ContactUs()).commit()
            R.id.nav_logout ->
                logout()

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun uploadImage(user:String){

        storageReference = FirebaseStorage.getInstance().reference

        if(filePath != null){
            //progressBar.visibility= View.VISIBLE
            val ref = storageReference?.child("uploads/" +  UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            if (uploadTask != null) {
                var imgdata=  ref
                uploadTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = ref.downloadUrl

                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("Image").child(user)

                        myRef.child("img").setValue(imgdata.toString())






                    } else {
                        // Handle failures
                    }
                }
                uploadTask.addOnFailureListener{
                    //progressBar.visibility= View.GONE

                    Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }

    }
}
