package net.polvott.etagtest

import org.jetbrains.exposed.dao.id.IntIdTable

object CountryEntity : IntIdTable() {
    val name = varchar("name", length = 50)
    val population = integer("population")
}