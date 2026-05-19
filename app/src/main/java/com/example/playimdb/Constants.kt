package com.example.playimdb

object Constants {
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val TMDB_POSTER_SIZE = "w500"
    const val TMDB_BACKDROP_SIZE = "w1280"
    val TMDB_READ_ACCESS_TOKEN: String get() = BuildConfig.TMDB_TOKEN
    const val PLAYIMDB_BASE_URL = "https://playimdb.com/title/"
}
