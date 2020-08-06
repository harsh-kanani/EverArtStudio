package com.example.everartstudio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.fragment__custom__order.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Custom_Order.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Custom_Order : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK
            && data != null && data.getData() != null )
        {
            filePath=data.data
            var bitmap= MediaStore.Images.Media.getBitmap(context!!.contentResolver,filePath)
            img_custom.setImageBitmap(bitmap)

//



        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var arlst = arrayListOf<String>("Drawing", "Paintings", "Poster")
        var adapter = ArrayAdapter<String>(context!!,android.R.layout.simple_spinner_dropdown_item,arlst)
        sp_custom_type.adapter = adapter

        var arlst2 = arrayListOf<String>("A4","A3","A2")
        var adapter2 = ArrayAdapter<String>(context!!,android.R.layout.simple_spinner_dropdown_item,arlst2)
        sp_custom_size.adapter = adapter2

        img_custom.setOnClickListener {
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        btn_custom_cart.setOnClickListener {
            var prc= 0
            if(sp_custom_type.selectedItem.toString() == "Drawing"){
                prc=2000

            }else if(sp_custom_type.selectedItem == "Paintings"){
                prc=2500
            }else{
                prc = 3000
            }
            if(sp_custom_size.selectedItem =="A4"){
                prc+=500
            }else if(sp_custom_size.selectedItem == "A3"){
                prc+=700
            }else{
                prc +=900
            }
            var sp = context!!.getSharedPreferences("MySp",Activity.MODE_PRIVATE)
            var user = sp.getString("uid","null")
            upload(prc,user.toString())

        }
    }

    private fun upload(prc:Int,user:String){

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
                        val myRef = database.getReference("Cart").child(user).push()
                        var adprd=CartDataClass(myRef.key.toString(),prc,sp_custom_size.selectedItem.toString(),sp_custom_type.selectedItem.toString(),imgdata.toString())
                        myRef.setValue(adprd)
                        Toast.makeText(context,"Successfully Added in Cart..!!", Toast.LENGTH_LONG).show()


                    } else {
                        // Handle failures
                    }
                }
                uploadTask.addOnFailureListener{
                    //progressBar.visibility= View.GONE

                    Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__custom__order, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Custom_Order.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Custom_Order().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}