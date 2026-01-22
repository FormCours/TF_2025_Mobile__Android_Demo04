package be.tftic.devmobile.demo04_async_request_gps.services

import be.tftic.devmobile.demo04_async_request_gps.dto.PersonResponseDto
import be.tftic.devmobile.demo04_async_request_gps.models.Person
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

suspend fun getPersonInfo(): Person {

    val url = URL("https://randomuser.me/api/?inc=gender,name,nat,picture")

    val connexion = url.openConnection() as HttpURLConnection
    try {
        var responseJSON = ""

        // Ajout d'un latence (pour la démo, ne fait pas ça en prod)
        delay(500)

        // Execution de la requete
        withContext(Dispatchers.IO) {
            connexion.connect() // ← Etablissement de la connexion

            // Exception si requete invalide
            if (connexion.responseCode != HttpURLConnection.HTTP_OK) {
                throw RuntimeException("Erreur lors de l'execution de la requete !")
            }

            // Récuperation des données sous la forme d'une string (JSON)
            connexion.getInputStream().bufferedReader().use {
                responseJSON = it.readText()
            }
        }

        // Renvoyer les données sous la forme d'un objet "model"
        var data: PersonResponseDto;
        withContext(Dispatchers.Default) {
            // Extrait les données via "GSON" dans le dto
            val gson = Gson()
            val dataType = object : TypeToken<PersonResponseDto>() {}.type
            data = gson.fromJson<PersonResponseDto>(responseJSON, dataType)
        }

        // Mapping vers le format "model"
        return Person(
            data.results[0].name.first,
            data.results[0].name.last,
            data.results[0].picture.large,
            data.results[0].nat
        );

    } finally {
        connexion.disconnect()
    }
}