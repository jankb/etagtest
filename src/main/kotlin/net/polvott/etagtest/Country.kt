package net.polvott.etagtest

data class Country(
    val id: Int,
    val name: String,
    val population: Int
)

data class CountriesUpdateRequest(
    val countries: List<Country>
)

data class AllCountriesResponse(
    val countries: List<Country>
)