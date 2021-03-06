package com.booklet.bookletandroid.presentation.fragments.markscard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.presentation.model.event.Result

class ResultListAdapter(
        private val mResults: List<Result>
) : RecyclerView.Adapter<ResultHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder =
            ResultHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_result,
                            parent,
                            false))

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        holder.bind(mResults[position])
    }

    override fun getItemCount(): Int = mResults.size
}