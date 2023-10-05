package net.polvott.etagtest

import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
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

    fun findCountry(id: Int): Country? {
        return CountryEntity.select { CountryEntity.id eq id }.firstOrNull()?.let {
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

    fun update(request: Country): Country?
    {
        CountryEntity.update({CountryEntity.id eq request.id}) {
            it[name] = request.name
            it[population] = request.population
        }
        return findCountry(request.id)
    }

    fun createCountries(request: CountriesCreateRequest): List<Country>
    {
        return request.countries.map {
            val id = create(it)
            Country(id, it.name, it.population)
        }
    }

    fun updateAllCountries(request: CountriesUpdateRequest): AllCountriesResponse
    {
        val countries = request.countries.mapNotNull { update(it) }
        return AllCountriesResponse(countries)
    }
}

data class CountryCreateRequest(
    val name: String,
    val population: Int
)

data class CountriesCreateRequest(
  val countries: List<CountryCreateRequest>
)