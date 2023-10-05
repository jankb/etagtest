package net.polvott.etagtest

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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

    fun getAllCountries(): List<Country>
    {
        return CountryEntity.selectAll().map {
            Country(
                id = it[CountryEntity.id].value,
                name = it[CountryEntity.name],
                population = it[CountryEntity.population]
            )
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

    fun createCountries(request: CountriesCreateRequest): List<Country>
    {
        val countries = mutableListOf<Country>()
        request.countries.forEach {
            val id  = create(it)
            countries.add(Country(id, it.name, it.population))
        }
        return countries
    }
}

data class CountryCreateRequest(
    val name: String,
    val population: Int
)

data class CountriesCreateRequest(
  val countries: List<CountryCreateRequest>
)