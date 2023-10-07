package net.polvott.etagtest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayInputStream

@RestController
@RequestMapping("/country")
class CountryController(
    private val countryService: CountryService
) {
    @GetMapping("/{name}")
    fun getCountryByName(@PathVariable name: String) : ResponseEntity<Country>
    {
        val country = countryService.findCountry(name)

        return if (country != null) {
            ResponseEntity.ok(
                Country(
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
                country -> Country (
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
            Country(it.id, it.name, it.population)
        }

        val response = AllCountriesResponse(countryResponse)

        return ResponseEntity.ok(response)
    }

    @PutMapping("/update")
    fun updateCountries(@RequestBody form: Country): ResponseEntity<Any> {
        val resp = countryService.update(form)
        return ResponseEntity.ok(resp)
    }

 //   @PutMapping("/updateAll")
    @PutMapping
    fun updateAllCountries(@RequestBody form: CountriesUpdateRequest, @RequestHeader("If-None-Match", required = false) ifNoneMatchHeader: String?) : ResponseEntity<Any>
    {
         ifNoneMatchHeader?.let {
            val countriesUpdateRequest =
                CountriesUpdateRequest(countryService.getAllCountries())

            val stream = byteArrayInputStream(countriesUpdateRequest)
            val eTagForCurrentData = generateEtag(stream)

            if (eTagForCurrentData != ifNoneMatchHeader) {
                val currentCountriesMap = countriesUpdateRequest.countries.associateBy { it.id }
                val modifiedCountriesMap = form.countries.associateBy { it.id }

                val delta = currentCountriesMap.filter { (id, modifiedCountry) ->
                    modifiedCountriesMap[id]?.let { it != modifiedCountry } ?: true
                }.values.toList()

                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(CountriesUpdateRequest(delta))
            }
        } ?: run {
            countryService.updateAllCountries(form)
        }
        return ResponseEntity.noContent().build()
    }

    private fun generateEtag(stream: ByteArrayInputStream): String {
        val builder = StringBuilder(37)
        builder.append("\"0")
        DigestUtils.appendMd5DigestAsHex(stream, builder)
        builder.append('"')
        return builder.toString()
    }

    /**
     * Converts a given object into a [ByteArrayInputStream] by serializing the object to JSON
     * and then converting it to a byte array.
     *
     * @param data The object to be converted to a [ByteArrayInputStream].
     * @return A [ByteArrayInputStream] representation of the serialized object.
     */
    private fun byteArrayInputStream(data: Any): ByteArrayInputStream {
        val objectMapper = jacksonObjectMapper()
        val jsonString = objectMapper.writeValueAsString(data)
        val byteArray = jsonString.toByteArray(Charsets.UTF_8)
        return ByteArrayInputStream(byteArray)
    }

    data class CountryCreateForm(
        val name: String,
        val population: Int
    )

    data class CountriesCreateForm(
        val countries: List<CountryCreateForm>
    )

}