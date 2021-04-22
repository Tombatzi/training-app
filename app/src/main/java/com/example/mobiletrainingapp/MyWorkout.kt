package com.example.mobiletrainingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_program.*

class MyWorkout : AppCompatActivity() {

    var workoutAdapter: WorkoutAdapter? = null


    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContentView(R.layout.activity_my_workout)

        setupRecyclerview()
    }


    fun setupRecyclerview(){

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference = db.collection("user").document(auth.currentUser.uid).collection("program")
        val query : Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<WorkoutModel> = FirestoreRecyclerOptions.Builder<WorkoutModel>()
            .setQuery(query, WorkoutModel::class.java)
            .build()

        workoutAdapter = WorkoutAdapter(firestoreRecyclerOptions)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = workoutAdapter
    }

    override fun onStart() {
        super.onStart()
        workoutAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        workoutAdapter!!.stopListening()
    }
}