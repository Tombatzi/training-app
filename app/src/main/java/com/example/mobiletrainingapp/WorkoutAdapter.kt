package com.example.mobiletrainingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.row_workout.view.*

class WorkoutAdapter(options: FirestoreRecyclerOptions<WorkoutModel>) :
    FirestoreRecyclerAdapter<WorkoutModel, WorkoutAdapter.WorkoutAdapterVH>(options) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutAdapterVH {
        return WorkoutAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.row_workout, parent, false))
    }

    override fun onBindViewHolder(holder: WorkoutAdapterVH, position: Int, model: WorkoutModel) {

        holder.workoutName.text = model.name
        holder.instruction.text = model.instruction
        holder.resistance.text = model.resistance
    }

    class WorkoutAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var workoutName = itemView.tvWorkoutName
        var instruction = itemView.tvInstructions
        var resistance = itemView.tvResistance
    }
}