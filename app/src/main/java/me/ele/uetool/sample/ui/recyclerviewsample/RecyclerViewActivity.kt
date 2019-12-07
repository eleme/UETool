package me.ele.uetool.sample.ui.recyclerviewsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.ele.uetool.sample.R

class RecyclerViewActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val list: ArrayList<Item> = ArrayList()
        for (i in 0..999) {
            val item = when (i % 3) {
                0 -> Item1()
                1 -> Item2()
                else -> Item3()
            }
            list.add(item)
        }
        recyclerView = findViewById(R.id.recycler)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = SimpleAdapter(list)
    }
}


class SimpleAdapter(private val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE1: Int = 1
        const val TYPE2: Int = 2
        const val TYPE3: Int = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_simple_textview, parent, false)
        return when (viewType) {
            TYPE2 -> ViewHolder2(view)
            TYPE3 -> ViewHolder3(view)
            else -> ViewHolder1(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view: TextView = holder.itemView as TextView
        view.text = position.toString()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item1 -> TYPE1
            is Item2 -> TYPE2
            is Item3 -> TYPE3
            else -> 0
        }
    }
}

interface Item

class Item1 : Item
class Item2 : Item
class Item3 : Item

class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView)
class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView)
class ViewHolder3(itemView: View) : RecyclerView.ViewHolder(itemView)