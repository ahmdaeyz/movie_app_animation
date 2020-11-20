package dev.ahmdaeyz.convertionalmovieapp.ui.movieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dev.ahmdaeyz.convertionalmovieapp.R
import dev.ahmdaeyz.convertionalmovieapp.model.Movie

class MoviesCarouselAdapter : RecyclerView.Adapter<MoviesCarouselAdapter.ViewHolder>() {

    private var items: List<Movie> = emptyList()


    interface OnItemClickListener {
        fun onClick(view: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)
        private val movieRating: TextView = itemView.findViewById(R.id.movie_rating)
        private val movieGenres: ChipGroup = itemView.findViewById(R.id.genres_chips)
        private val movieDirector: TextView = itemView.findViewById(R.id.movie_director)
        private val actorsList: RecyclerView = itemView.findViewById(R.id.actors_list)
        private val introductionText: TextView = itemView.findViewById(R.id.introduction_text)
        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            moviePoster.load(movie.posterResId) {
                transformations(RoundedCornersTransformation(radius = 40f))
            }
            movieRating.text = "9.0"
            movieGenres.children.forEachIndexed { index, view ->
                val chip = view as Chip
                chip.text = movie.chips[index]
            }
            movieDirector.text =
                itemView.context.getString(R.string.director_format, movie.director)
            val actorsListAdapter = ActorsListAdapter()
            actorsListAdapter.submitList(movie.actors)
            actorsList.adapter = actorsListAdapter
            introductionText.text = movie.introductionText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movies_carousel_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return items[position].posterResId.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<Movie>) {
        items = list
    }

}