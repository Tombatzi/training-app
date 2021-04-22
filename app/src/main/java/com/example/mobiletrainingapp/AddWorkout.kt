package com.example.mobiletrainingapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.FileProvider
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_add_workout.*
import kotlinx.android.synthetic.main.fragment_user_data.*
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File
private lateinit var auth: FirebaseAuth


class AddWorkout : Fragment() {

    companion object {
        private const val TAG = "AddworkoutFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraBtn.setOnClickListener{
            dispatchTakePictureIntent()
        }
        submitBtn.setOnClickListener {
            postData()
        }
    }
    private fun postData(){
        val db = Firebase.firestore
        var resistance = ""

        if(bodyweightRB.isChecked){
            resistance = bodyweightRB.text.toString()
        }else if(rubberRB.isChecked){
            resistance = rubberRB.text.toString()
        }
        else if(dumbbellRB.isChecked){
            resistance = dumbbellRB.text.toString()
        }
        else if(freeweightRB.isChecked){
            resistance = freeweightRB.text.toString()
        }
        else if(otherRB.isChecked){
            resistance = otherRB.text.toString()
        }


        val workoutValues = hashMapOf(
            "name" to addNameText.text.toString(),
            "instruction" to addInsturctionsText.text.toString(),
            "resistance" to resistance
        )
        db.collection("user").document(auth.currentUser.uid).collection("program").document(addNameText.text.toString())
            .set(workoutValues)
            .addOnSuccessListener { Log.d(AddWorkout.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(AddWorkout.TAG, "Error writing document", e) }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        // This DOESN'T work for API >= 24 (starting 2016)
        // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)

        val fileProvider = context?.let { FileProvider.getUriForFile(it, "com.example.mobiletrainingapp.fileprovider", photoFile) }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        startActivityForResult(takePictureIntent, REQUEST_CODE)
    }

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            iVMovePhoto.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}