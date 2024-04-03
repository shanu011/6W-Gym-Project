package com.example.adminapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminapp.databinding.ImageItemBinding

class ImageAdapter(var imageList : ArrayList<Uri>): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    class ViewHolder(var binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(uri: Uri) {
            binding.ivImageShow.setImageURI(uri)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindData(imageList[position])
    }
}