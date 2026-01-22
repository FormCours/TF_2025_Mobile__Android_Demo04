package be.tftic.devmobile.demo04_async_request_gps.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getStringOrNull
import be.tftic.devmobile.demo04_async_request_gps.db.DbContract
import be.tftic.devmobile.demo04_async_request_gps.db.DbHelper
import be.tftic.devmobile.demo04_async_request_gps.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ProductDao(context: Context) {

    private val dbHelper = DbHelper(context)
    private var db : SQLiteDatabase? = null;

    //region Méthode d'acces à la base de donnée
    fun openWritable() {
        db = dbHelper.writableDatabase
    }

    fun openReadable() {
        db = dbHelper.readableDatabase
    }

    fun close() {
        db?.close()
        db = null
    }

    private fun requireDb() : SQLiteDatabase {
        return db ?:  throw RuntimeException("Database not open ! (╯°□°）╯︵ ┻━┻")
    }
    //endregion

    //region Méthode utilitaire
    private fun cursorToProduct(cursor: Cursor): Product {
        val product = Product(
            cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Product.ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Product.NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Product.EAN13)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.Product.PRICE)),
            cursor.getStringOrNull(cursor.getColumnIndexOrThrow(DbContract.Product.DESC)),
            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Product.RELEASE_DATE))),
            cursor.getDouble(cursor.getColumnIndexOrThrow(DbContract.Product.IN_STOCK)) == 1.0
        )

        return product
    }

    private fun productToContentValues(product: Product): ContentValues {
        // Premet de créer un objet qui lie le nom de la colonnes et la valeur de l'objet
        return ContentValues().apply {
            put(DbContract.Product.NAME, product.name)
            put(DbContract.Product.EAN13, product.ean13)
            put(DbContract.Product.DESC, product.desc)
            put(DbContract.Product.PRICE, product.price)
            put(DbContract.Product.RELEASE_DATE, product.releaseDate.toString())
            put(DbContract.Product.IN_STOCK, product.inStock)
        }
    }
    //endregion

    //region Méthode d'interaction avec les données (get, insert, delete, update)
    suspend fun getAll() : List<Product> = withContext(Dispatchers.IO) {
        val db = requireDb();

        // Requete pour un récuperer les données
        val cursor = db.query(
            DbContract.Product.TABLE_NAME,
            null, // Liste des colonnes (null → SELECT * FROM ...)
            null, // Clause WHERE
            null, // Argument de la clause WHERE
            null, // Clause GROUP BY
            null, // Clause HAVING
            "${DbContract.Product.NAME} ASC"
        )

        // Si aucun resultat dans le cursor, envoi d'une liste vide
        if(!cursor.moveToFirst()) {
            return@withContext emptyList()
        }

        // Parcours du cursor
        val result = mutableListOf<Product>()

        do {
            // Récuperer les données dans le format du modele (classe Product)
            val product: Product = cursorToProduct(cursor)

            // Ajout à la liste des resultats
            result.add(product)

        } while(cursor.moveToNext())

        // Cloture de la requete
        cursor.close()
        return@withContext result
    }

    suspend fun getById(productId: Long) : Product? = withContext(Dispatchers.IO) {
        val db = requireDb()

        // Utilisation du cursor via l'ecriture "use" de kotlin
        db.query(
            DbContract.Product.TABLE_NAME,
            null,
            "${DbContract.Product.ID} = ?",    // La clause WHERE pour obtenir un produit via l'id
            arrayOf(productId.toString()), // Les parametres necessaire de la clause WHERE
            null,
            null,
            null
        ).use { cursor ->

            if(!cursor.moveToFirst()) {
                return@withContext null
            }
            return@withContext cursorToProduct(cursor)
        }
    }

    suspend fun insert(product: Product) : Long = withContext(Dispatchers.IO) {
        val db = requireDb()

        return@withContext db.insertOrThrow(
            DbContract.Product.TABLE_NAME,
            null,
            productToContentValues(product)
        )
    }

    suspend fun delete(productId: Long) : Boolean = withContext(Dispatchers.IO) {
        val db = requireDb()

        val nbRowDeleted = db.delete(
            DbContract.Product.TABLE_NAME,
            "${DbContract.Product.ID} = ?",
            arrayOf(productId.toString())
        )

        return@withContext nbRowDeleted == 1
    }

    suspend fun update(product: Product) : Boolean= withContext(Dispatchers.IO)  {
        val db = requireDb()

        val nbRowUpdated = db.update(
            DbContract.Product.TABLE_NAME,
            productToContentValues(product),
            "${DbContract.Product.ID} = ?",
            arrayOf(product.id.toString())
        )

        return@withContext nbRowUpdated == 1
    }
    //endregion
}