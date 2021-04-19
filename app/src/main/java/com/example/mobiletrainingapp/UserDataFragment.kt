package com.example.mobiletrainingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_data.*
import kotlin.String as Ed


class UserDataFragment : Fragment() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_GOOGLE_SIGN_IN = 1001
    }


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        getDocument()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        btnPostUserData.setOnClickListener() {

            val userValues = hashMapOf(
                "name" to editTextPersonName.text.toString(),
                "age" to editTextPersonAge.text.toString(),
            )

            db.collection("user").document(auth.currentUser.uid.toString())
                .set(userValues)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    private fun getDocument() {
        // [START get_document]
        val db = Firebase.firestore
        val docRef = db.collection("user").document(auth.currentUser.uid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("name")
                    val userAge = document.getString("age")
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    if (userName != null && userAge != null) {
                        editTextPersonAge.setText(userAge)
                        editTextPersonName.setText(userName)
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        // [END get_document]
    }


}