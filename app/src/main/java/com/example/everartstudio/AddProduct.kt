package com.example.everartstudio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_product.*
import java.util.*
import kotlin.collections.ArrayList


class AddProduct : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var d:ByteArray?=null
    private var username:String?=null
    private var flg:String="false"
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null )
        {
            filePath=data.data
            var bitmap=MediaStore.Images.Media.getBitmap(contentResolver,filePath)
            proimg.setImageBitmap(bitmap)

//            imgupload.isDrawingCacheEnabled = true
//            imgupload.buildDrawingCache()
//            val bitmap = (imgupload.drawable as BitmapDrawable).bitmap
//            val baos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//             d= baos.toByteArray()



        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_add_product)


            /*var arr= arrayOf("ABC","Xyz","DVD") */
            var arlst = arrayListOf<String>("Drawing", "Handicraft", "Paintings", "Poster")
            var adapter = ArrayAdapter<String>(
                this@AddProduct,
                android.R.layout.simple_spinner_dropdown_item,
                arlst
            )
            spcategory.adapter = adapter
            proimg.setOnClickListener {

                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

            }

        btnadd.setOnClickListener {
                uploadImage()

                //Toast.makeText(this,"hel",Toast.LENGTH_LONG).show()
            }

        }

    private fun uploadImage(){

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



                        //progressBar.visibility= View.GONE
                        //Toast.makeText(this, imgdata.toString(), Toast.LENGTH_SHORT).show()

                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("Product")
                        var adprd=AddProductDataClass(txtpnm.text.toString(),txtprice.text.toString().toInt(),txtdetail.text.toString(),spcategory.selectedItem.toString(),imgdata.toString(),"on")
                        myRef.child(txtpnm.text.toString()).setValue(adprd)
                        Toast.makeText(this@AddProduct,"Successfully Add",Toast.LENGTH_LONG).show()





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