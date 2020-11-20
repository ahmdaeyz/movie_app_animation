package dev.ahmdaeyz.convertionalmovieapp.model

import dev.ahmdaeyz.convertionalmovieapp.R

data class Movie(
    val title: String,
    val posterResId: Int,
    val bgResId: Int,
    val chips: List<String>,
    val director: String,
    val actors: List<Actor>,
    val introductionText: String
)

val movies = listOf(
    Movie(
        "Good Boys",
        R.drawable.good_boys_poster,
        R.drawable.good_boys_bg,
        listOf("Action", "Drama", "History"),
        "Gene Stupnitsky",
        listOf<Actor>(
            Actor("Jacob Tremblay", R.drawable.jacob_tremblay),
            Actor("Keith L. Williams", R.drawable.keith_l_williams),
            Actor("Brady Noon", R.drawable.brady_noon)
        ),
        "Max, a 12-year-old boy, has the chance to kiss his crush at a party and works out a plan with his best friends to learn how to kiss."
    ),
    Movie(
        "Joker",
        R.drawable.joker_poster,
        R.drawable.joker_bg,
        listOf("Action", "Drama", "History"),
        "Todd Phillips",
        listOf(
            Actor("Joaquin Phoenix", R.drawable.joaquin),
            Actor("Robert De Niro", R.drawable.robert_diro),
            Actor("Zazie Beetz", R.drawable.zazie)
        ),
        "Arthur Fleck, a party clown, leads an impoverished life with his ailing mother. However, when society shuns him and brands him as a freak, he decides to embrace the life of crime and chaos."
    ),
    Movie(
        "The Hustle",
        R.drawable.hustle_poster,
        R.drawable.hustle_bg,
        listOf("Action", "Drama", "History"),
        "Chris Addison",
        listOf(
            Actor("Rebel Wilson", R.drawable.rebel),
            Actor("Anne Hathaway", R.drawable.anne),
            Actor("Alex Sharp", R.drawable.alex)
        ),
        "Josephine Chesterfield is a glamorous, seductive British woman who has a penchant for defrauding gullible men out of their money. Into her well-ordered, meticulous world comes Penny Rust, a cunning and fun-loving Australian woman who lives to swindle unsuspecting marks."
    ),
)