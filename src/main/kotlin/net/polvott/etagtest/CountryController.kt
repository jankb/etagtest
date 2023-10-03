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

    data class CountryResponse(
        val id: Int,
        val name: String,
        val population: Int
    )

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

    data class AllCountriesResponse(
        val countries: List<CountryResponse>
    )

    @PostMapping
    fun create(@RequestBody form: CountryCreateForm): ResponseEntity<CountryCreateResponse> {
        val countryId = countryService.create(
            CountryCreateRequest(
                name = form.name,
                population = form.population
            )
        )

        return ResponseEntity.ok(CountryCreateResponse(countryId))
    }

    data class CountryCreateForm(
        val name: String,
        val population: Int
    )

    data class CountryCreateResponse(
        val id: Int
    )
}