package com.example.myapplicationkotlin

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class AccessAdapter(val accessList: List<AccessData>) : RecyclerView.Adapter<AccessAdapter.ViewHolder> (){
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.title)
        val content : TextView = view.findViewById(R.id.content)
        val time :TextView = view.findViewById(R.id.time)
        val lockImage : ImageView = view.findViewById(R.id.lock_image)

        fun bind(title: String, content:String, time:String, lockImage:Int){
            this.title.text = title
            this.content.text = content
            this.time.text = time
            this.lockImage.setImageResource(lockImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.access_item, parent, false)
        val viewHolder = ViewHolder(view)
        //为view添加点击响应事件
        viewHolder.itemView.setOnClickListener{
            val positon = viewHolder.adapterPosition
            val item = accessList[positon]
            Toast.makeText(parent.context, "clicked ${item.title}", Toast.LENGTH_SHORT).show()
            Log.i("TAG", "clicked ${item.title}")
        }
        //为image添加点击响应事件
        viewHolder.lockImage.setOnClickListener {
            val positon = viewHolder.adapterPosition
            val item = accessList[positon]
            Toast.makeText(parent.context, "image", Toast.LENGTH_SHORT).show()
            Log.i("TAG", "clicked image")
            //跳转到某个页面
            //val intent = Intent(parent.context, BaseWidgetActivity::class.java)
            //startActivity(parent.context, intent, null)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = accessList[position]
        holder.bind(item.title, item.content, item.time, item.lockImage)
    }

    override fun getItemCount(): Int {
        return accessList.size
    }
}