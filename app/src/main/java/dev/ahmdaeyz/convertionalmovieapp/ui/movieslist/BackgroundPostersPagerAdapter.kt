package dev.ahmdaeyz.convertionalmovieapp.ui.movieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.ahmdaeyz.convertionalmovieapp.R

class BackgroundPostersPagerAdapter :
    RecyclerView.Adapter<BackgroundPostersPagerAdapter.ViewHolder>() {
    private var items: List<Int> = emptyList()

    class ViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        private val posterImageView: ImageView = viewItem.findViewById(R.id.poster)

        fun bind(posterResId: Int) {
            posterImageView.setImageResource(posterResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.poster_bg_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<Int>) {
        items = list
    }

    override fun getItemId(position: Int): Long {
        return items[position].toLong()
    }
}