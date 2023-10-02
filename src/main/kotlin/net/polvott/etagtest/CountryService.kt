package net.polvott.etagtest

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class CountryService {

    fun findCountry(name: String): Country? {
        return CountryEntity.select { CountryEntity.name eq name }.firstOrNull()?.let {
            Country(
                id = it[CountryEntity.id].value,
                name = it[CountryEntity.name],
                population = it[CountryEntity.population],)
        }
    }

    fun create(request: CountryCreateRequest): Int
    {
        val id =  CountryEntity.insertAndGetId {
            it[name] = request.name
            it[population] = request.population
        }

        return id.value
    }
}

data class CountryCreateRequest(
    val name: String,
    val population: Int
)