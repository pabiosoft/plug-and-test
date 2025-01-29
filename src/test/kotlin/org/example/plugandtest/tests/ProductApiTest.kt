package org.example.plugandtest.tests

import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.net.HttpURLConnection
import java.net.URL
import org.testng.Assert.assertEquals

/**
 * 📌 Type de test : Tests API
 *
 * 🎯 Objectif :
 * - Vérifier le bon fonctionnement des routes de l'API `/api/products`.
 * - Tester les réponses pour différentes actions :
 *   - Récupération de la liste des produits.
 *   - Gestion des erreurs pour des produits inexistants.
 *   - Création de nouveaux produits en respectant le format JSON-LD.
 *
 * 🛠️ Méthodologie :
 * - Utiliser des requêtes HTTP directes (GET, POST).
 * - Vérifier les codes de réponse HTTP (200, 404, 201).
 * - Assurer que les comportements de l'API sont conformes aux attentes.
 * - **IMPORTANT** : Toutes les requêtes d'ajout (`POST`) doivent être envoyées avec
 *   `Content-Type: application/ld+json` pour respecter le format attendu par API Platform.
 *
 * ✅ Améliorations :
 * - Ajout de l'authentification avec un **JWT token** pour tester les accès sécurisés.
 * - Correction du format d'envoi des données pour éviter les erreurs 415 (Unsupported Media Type).
 */
class ProductApiTest {

    private val loginUrl = "http://localhost:8081/api/login"
    private val baseUrl = "http://localhost:8082/api/products"
    private var authToken: String? = null

    @BeforeClass
    fun setup() {
        println("📌 Tests API démarrés sur $baseUrl")
        authToken = getAuthToken()
        assert(authToken != null) { "⚠️ Impossible de récupérer le token JWT" }
    }

    @Test(description = "Vérifie que l'API retourne bien la liste des produits avec un token valide")
    fun `GET products should return 200 OK`() {
        val url = URL(baseUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer $authToken")

        val responseCode = connection.responseCode
        println("🔍 GET /products → HTTP $responseCode")

        assertEquals(responseCode, 200, "Le serveur doit répondre avec HTTP 200 OK")
    }

    @Test(description = "Vérifie qu'un produit inexistant retourne une erreur 404")
    fun `GET non-existing product should return 404`() {
        val url = URL("$baseUrl/999999")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer $authToken")

        val responseCode = connection.responseCode
        println("🔍 GET /products/999999 → HTTP $responseCode")

        assertEquals(responseCode, 404, "Un produit inexistant doit retourner HTTP 404 Not Found")
    }

    @Test(description = "Ajoute un nouveau produit et vérifie que la réponse est 201 Created")
    fun `POST product should return 201 Created`() {
        val url = URL(baseUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/ld+json") // ✅ Corrigé
        connection.setRequestProperty("Authorization", "Bearer $authToken")

        val jsonInputString = """
        {
            "name": "Test Product",
            "description": "Produit de test",
            "price": 99.99
        }
    """.trimIndent()

        connection.outputStream.use { os -> os.write(jsonInputString.toByteArray()) }
        val responseCode = connection.responseCode

        println("📝 POST /products → HTTP $responseCode")
        assertEquals(responseCode, 201, "L'ajout d'un produit doit retourner HTTP 201 Created")
    }


    /**
     * 🔐 Récupère un token JWT en appelant l'endpoint /api/login
     */
    private fun getAuthToken(): String? {
        val url = URL(loginUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        val jsonInputString = """
            {
                "email": "dev4@mo.com",
                "password": "Dev4"
            }
        """.trimIndent()

        connection.outputStream.use { os -> os.write(jsonInputString.toByteArray()) }

        return if (connection.responseCode == 200) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            println("🔑 JWT récupéré: $response")
            extractToken(response)
        } else {
            println("⚠️ Échec de l'authentification: HTTP ${connection.responseCode}")
            null
        }
    }

    /**
     * 🎯 Extrait le token JWT de la réponse JSON
     */
    private fun extractToken(response: String): String? {
        val regex = """"token"\s*:\s*"(.+?)"""".toRegex()
        return regex.find(response)?.groupValues?.get(1)
    }
}

