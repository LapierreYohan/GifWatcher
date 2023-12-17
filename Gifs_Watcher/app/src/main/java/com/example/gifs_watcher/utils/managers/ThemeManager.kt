package com.example.gifs_watcher.utils.managers

import kotlin.random.Random

object ThemeManager {

    private val SEARCH_TERMS = listOf(
        "Cute animals",
        "Humor",
        "Science fiction",
        "Cult films",
        "Nature",
        "Technology",
        "Sports",
        "Travel",
        "Developper Meme",
        "What the fuck",
        "Cooking",
        "Danse",
        "Music",
        "Bleach",
        "Abstract art",
        "Emotions",
        "Holidays",
        "Celebrity reactions",
        "Historical events",
        "Superheroes",
        "Jokes",
        "Motivation",
        "Wildlife",
        "Office jokes",
        "Science",
        "Rocket league",
        "Anime",
        "Cars",
        "Cartoons",
        "Dance",
        "Fitness",
        "Triumphs and failures",
        "Love",
        "Naruto",
        "Facial expressions",
        "Video games",
        "Funny kids",
        "Learning",
        "Sheldon Cooper",
        "Cultural events",
        "Celebrations",
        "How to",
        "Futuristic technology",
        "Crafts and DIY",
        "Social events",
        "Fashion",
        "One piece",
        "Vintage vehicles",
        "Contradictory emotions",
        "Famous film sequences",
        "Panoramic views",
        "Luffy",
        "Celebrity reactions",
        "Strange moves",
        "Dark humor",
        "Dragon ball",
        "Epic moments",
        "Natural phenomena",
        "Japanese animation",
        "Manga",
        "Humorous TV series",
        "Animated characters",
        "Virtual reality",
        "Travel adventures",
        "Fairy tail",
        "Animes",
        "Angry",
        "Pandas",
        "Developpers",
        "Loving"
    )

    fun getRandomTheme(excludes : ArrayList<String>? = null): String {

        if (excludes == null || excludes?.size == 0) {
            val randomIndex = Random.nextInt(SEARCH_TERMS.size)
            return SEARCH_TERMS[randomIndex]
        } else {
            val filteredSearchTerms = SEARCH_TERMS.filterNot  { it in excludes!! }
            val randomIndex = Random.nextInt(filteredSearchTerms.size)
            return filteredSearchTerms[randomIndex]
        }
    }
}