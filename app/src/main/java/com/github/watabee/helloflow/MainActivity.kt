package com.github.watabee.helloflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.watabee.helloflow.api.Repository

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()
    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.repositories.observe(this, adapter::update)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
    }

    private class Adapter : RecyclerView.Adapter<MyViewHolder>() {

        private val repositories = mutableListOf<Repository>()

        fun update(newRepositories: List<Repository>) {
            repositories.clear()
            repositories.addAll(newRepositories)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(parent)

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(repositories[position])
        }

        override fun getItemCount(): Int = repositories.size
    }

    private class MyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_repository,
            parent,
            false
        )
    ) {
        private val textView: TextView = itemView.findViewById(R.id.text_name)

        fun bind(repository: Repository) {
            textView.text = repository.name
        }
    }
}
