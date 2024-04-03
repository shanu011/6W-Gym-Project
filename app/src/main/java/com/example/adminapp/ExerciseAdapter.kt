package com.example.adminapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ExerciseAdapter(var context: Context,
                      private val categoryList: ArrayList<ExerciseModel>,
    var clickInterface: ClickInterface

                      ) :
    RecyclerView.Adapter<ExerciseAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentitem = categoryList[position]
        holder.tvExerciseName.text = currentitem.exerciseName
        holder.tvDescription.text = currentitem.exerciseDescription
        Glide.with(context)
            .load(currentitem.image).placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(holder.ivExerciseImage)
        holder.tvUpdate.setOnClickListener {
            clickInterface.onExerciseClick(currentitem)
        }
        holder.itemView.setOnClickListener {
            clickInterface
        }


    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvExerciseName : TextView = itemView.findViewById(R.id.tvExerciseName)
        val tvDescription :TextView = itemView.findViewById(R.id.tvDescription)
        val ivExerciseImage :ImageView = itemView.findViewById(R.id.image)
        val tvUpdate :TextView = itemView.findViewById(R.id.tvUpdate)

    }
}