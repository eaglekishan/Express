package com.example.express.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.express.R
import com.example.express.data.SuggAccount

class SuggViewAdapter(private val m:List<SuggAccount>,
                      private val context:Context,
                      private val click:ClickListner):RecyclerView.Adapter<SuggViewAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val profileSugg=itemView.findViewById<ImageView>(R.id.suggProfile)
        val emailSugg:TextView=itemView.findViewById(R.id.suggEmail)
        val btnSugg:Button=itemView.findViewById(R.id.suggBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.suggestion_view,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return m.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=m[position]
        if(item.sugProfile.isNotEmpty()){
            Glide.with(context).load(item.sugProfile).into(holder.profileSugg)
        }

        holder.emailSugg.text=item.sugEmail

        holder.btnSugg.setOnClickListener {
            click.onFollowClick(item.uid)
            holder.btnSugg.text="Following"


        }
    }

    interface ClickListner{
        fun onFollowClick(uid:String)
    }
}