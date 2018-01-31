package com.khanmurad.weather

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.chart.view.*


class ChartsAdapter : RecyclerView.Adapter<ChartsAdapter.Holder>() {

    var charts = emptyList<Chart>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent.inflate(R.layout.chart))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.chart.chart = charts[position]
    }

    override fun getItemCount() = charts.size

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}