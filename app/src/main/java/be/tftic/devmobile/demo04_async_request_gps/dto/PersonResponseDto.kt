package be.tftic.devmobile.demo04_async_request_gps.dto

data class PersonResponseDto(
    val results: List<Result>,
    val info: Info,
) {

    data class Result(
        val gender: String,
        val name: Name,
        val picture: Picture,
        val nat: String,
    )

    data class Name(
        val title: String,
        val first: String,
        val last: String,
    )

    data class Picture(
        val large: String,
        val medium: String,
        val thumbnail: String,
    )

    data class Info(
        val seed: String,
        val results: Long,
        val page: Long,
        val version: String,
    )
}