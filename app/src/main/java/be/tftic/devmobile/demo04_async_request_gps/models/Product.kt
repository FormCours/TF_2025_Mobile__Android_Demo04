package be.tftic.devmobile.demo04_async_request_gps.models

import java.time.LocalDate

data class Product(
    val id: Long = 0,
    val name: String,
    val ean13: String,
    val price: Double,
    val desc: String?,
    val releaseDate: LocalDate,
    val inStock: Boolean
)
