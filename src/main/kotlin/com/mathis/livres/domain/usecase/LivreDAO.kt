package com.mathis.livres.domain.usecase

import com.mathis.livres.domain.models.Livre
import com.mathis.livres.domain.port.LivreRepository

class LivreDAO: LivreRepository {
    val livres = mutableListOf<Livre>()

    override fun addLivre(livre: Livre) {
        if (livre.title.isBlank() || livre.author.isBlank()) {
            throw IllegalArgumentException("Livre author or title missing")
        }
        livres.add(livre)
    }

    override fun fetchAllLivres(): List<Livre> {
        // C'est ici que le test de propriété échouait probablement !
        return livres.sortedBy { it.title.lowercase() }
    }

    fun clear() {
        livres.clear()
    }
}