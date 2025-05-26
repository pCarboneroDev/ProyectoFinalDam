package com.example.dam_proyecto_pablo_carbonero.lib.domain.model

class SongSearchDelegate(
    private val baseList: List<SongWithTuning>
) {

    private var searchResult: List<SongWithTuning> = emptyList()

    var currentQuery: String = ""

    fun search(query: String): List<SongWithTuning>{
        searchResult = baseList.filter { it.song.name.lowercase().contains(query) || it.song.bandName.lowercase().contains(query) }
        return searchResult
    }


    fun updateQuery(query: String): List<SongWithTuning> {
        currentQuery = query
        return filter()
    }

    fun filter(): List<SongWithTuning> {
        return if (currentQuery.isBlank()) {
            baseList
        } else {
            search(currentQuery)
        }
    }

    fun clear() {
        currentQuery = ""
    }
}