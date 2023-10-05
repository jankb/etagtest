package net.polvott.etagtest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/country")
class CountryController(
    private val countryService: CountryService
) {
    @GetMapping("/{name}")
    fun getCountryByName(@PathVariable name: String) : ResponseEntity<CountryResponse>
    {
        val country = countryService.findCountry(name)

        return if (country != null) {
            ResponseEntity.ok(
                CountryResponse(
                id = country.id,
                name = country.name,
                population = country.population
            ))
        } else {
            ResponseEntity.notFound().build()
        }
    }



    @GetMapping
    fun getAllCountries() : ResponseEntity<AllCountriesResponse>
    {
        val countries = countryService.getAllCountries()

        val cResponse = countries.map {
            country -> CountryResponse (
                id = country.id,
                name = country.name,
                population = country.population
            )
        }
        return ResponseEntity.ok(
            AllCountriesResponse(
                countries = cResponse
            ))
    }

    @PostMapping
    fun createCountries(@RequestBody form: CountriesCreateForm): ResponseEntity<AllCountriesResponse> {

        val countriesCreateRequest = CountriesCreateRequest (
            countries = form.countries.map {
                CountryCreateRequest(it.name, it.population)
            }
        )
        val createdCountries = countryService.createCountries(countriesCreateRequest)

        val countryResponse = createdCountries.map {
            CountryResponse(it.id, it.name, it.population)
        }

        val response = AllCountriesResponse(countryResponse)

        return ResponseEntity.ok(response)
    }

    data class CountryCreateForm(
        val name: String,
        val population: Int
    )

    data class CountriesCreateForm(
        val countries: List<CountryCreateForm>
    )

    data class CountryResponse(
        val id: Int,
        val name: String,
        val population: Int
    )

    data class AllCountriesResponse(
        val countries: List<CountryResponse>
    )

}