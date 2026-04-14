package com.mathis.livres.domain.usecase

import com.mathis.livres.domain.models.Livre
import com.mathis.livres.domain.port.LivreRepository

class LivreDAO: LivreRepository {
    var livres = mutableListOf<Livre>()
    override fun fetchAllLivres(): List<Livre> {
        return livres.sortedBy { it.title }
    }

    override fun addLivre(livre: Livre) {
        if (livre.author.isBlank() || livre.title.isBlank()) {
            throw IllegalArgumentException("Livre author or title missing")
        }
        livres.add(livre)
    }
}