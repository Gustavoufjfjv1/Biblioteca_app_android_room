package com.gustavo.biblioteca.repository

import com.gustavo.biblioteca.database.LivroDao
import com.gustavo.biblioteca.database.LivroEntity
import com.gustavo.biblioteca.model.Livro
import com.gustavo.biblioteca.network.BookDoc
import com.gustavo.biblioteca.network.OpenLibraryService
import com.gustavo.biblioteca.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LivroRepository(
    private val dao: LivroDao,
    private val api: OpenLibraryService = RetrofitClient.api
) {
    // Observar: a tela se inscreve aqui uma única vez
    fun observeLivros(): Flow<List<Livro>> =
        dao.observeAll().map { entidades -> entidades.map { it.toDomain() } }

    // Buscar e Armazenar: só alimenta o Room, não devolve nada direto pra tela
    suspend fun buscarEArmazenar(query: String) {
        val resposta = api.buscarLivros(query)
        dao.insertAll(resposta.docs.map { it.toEntity() })
    }
}

private fun LivroEntity.toDomain() = Livro(id = id, titulo = titulo, autor = autor, ano = ano)

private fun BookDoc.toEntity() = LivroEntity(
    titulo = title,
    autor = authorName?.firstOrNull() ?: "Autor desconhecido",
    ano = firstPublishYear ?: 0
)