package com.mikhailgrigorev.mts_home.actorsRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.R
import com.mikhailgrigorev.mts_home.api.ActorResponse

class ActorAdapter(
    context: Context,
    private var actors: List<ActorResponse>):
    RecyclerView.Adapter<ActorViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(inflater.inflate(R.layout.item_actor, parent, false))
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        getActorAt(position)?.let { holder.bind(it) }
    }

    override fun getItemCount() = actors.size

    private fun getActorAt(position: Int): ActorResponse? {
        return when {
            actors.isEmpty() -> null
            position >= actors.size -> null
            else -> actors[position]
        }
    }
}