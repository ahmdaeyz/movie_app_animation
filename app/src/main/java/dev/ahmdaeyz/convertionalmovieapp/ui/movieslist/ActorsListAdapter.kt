package dev.ahmdaeyz.convertionalmovieapp.ui.movieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import dev.ahmdaeyz.convertionalmovieapp.R
import dev.ahmdaeyz.convertionalmovieapp.model.Actor


class ActorsListAdapter : RecyclerView.Adapter<ActorsListAdapter.ViewHolder>() {

    private var items: List<Actor> = emptyList()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actorName: TextView = itemView.findViewById(R.id.movie_actor_name)
        private val actorPic: ImageView = itemView.findViewById(R.id.movie_actor_pic)

        fun bind(actor: Actor) {
            actorName.text = actor.name
            actorPic.load(actor.pictureResId) {
                transformations(RoundedCornersTransformation(radius = 18f))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_actors_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return items[position].pictureResId.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<Actor>) {
        items = list
    }

}