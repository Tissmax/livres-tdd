package com.mathis.livres.domain.port

import com.mathis.livres.domain.models.Livre

interface LivreRepository {
    fun fetchAllLivres(): List<Livre>
    fun addLivre(livre: Livre)
}