package com.mikhailgrigorev.mts_home.actorsRecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mikhailgrigorev.mts_home.PATH_HEADER
import com.mikhailgrigorev.mts_home.R
import com.mikhailgrigorev.mts_home.api.ActorResponse

class ActorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var actorName = itemView.findViewById<TextView>(R.id.actor_name)
    private var actorCardImage = itemView.findViewById<ImageView>(R.id.actor_card_image)

    fun bind(actor: ActorResponse) {

        actorCardImage.load(PATH_HEADER + actor.profile_path)
        actorName.text = actor.name

    }
}