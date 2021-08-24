package com.mikhailgrigorev.mts_home.actorsRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.R

class ActorAdapter(
    context: Context,
    private var actorsNames: List<String>,
    private var actorsPaths: List<String>):
    RecyclerView.Adapter<ActorViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(inflater.inflate(R.layout.item_actor, parent, false))
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        getActorAt(position)?.let { holder.bind(it) }
    }

    override fun getItemCount() = actorsNames.size

    private fun getActorAt(position: Int): Pair<String, String>?{
        return when {
            actorsNames.isEmpty() -> null
            position >= actorsNames.size -> null
            else -> Pair(actorsNames[position], actorsPaths[position])
        }
    }
}