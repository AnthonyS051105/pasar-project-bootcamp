package com.example.pasar_project_bootcamp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecommendationAdapter(
    private val recommendations: List<String>,
    private val onRecommendationClick: (String) -> Unit
) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recommendationText: TextView = itemView.findViewById(R.id.tv_recommendation)
        val subtitleText: TextView = itemView.findViewById(R.id.tv_subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]
        
        holder.recommendationText.text = recommendation
        holder.subtitleText.text = "satu"
        
        holder.itemView.setOnClickListener {
            onRecommendationClick(recommendation)
        }
    }

    override fun getItemCount(): Int = recommendations.size
}