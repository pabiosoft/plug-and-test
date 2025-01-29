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
 *   - Création de nouveaux produits.
 *
 * 🛠️ Méthodologie :
 * - Utiliser des requêtes HTTP directes (GET, POST).
 * - Vérifier les codes de réponse HTTP (200, 404, 201).
 * - Assurer que les comportements de l'API sont conformes aux attentes.
 */
class ProductApiTest {

    private val baseUrl = "http://localhost:8082/api/products"

    @BeforeClass
    fun setup() {
        println("📌 Tests API démarrés sur $baseUrl")
    }

    @Test(description = "Vérifie que l'API retourne bien la liste des produits")
    fun `GET products should return 200 OK`() {
        val url = URL(baseUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        val responseCode = connection.responseCode

        println("🔍 GET /products → HTTP $responseCode")
        assertEquals(responseCode, 200, "Le serveur doit répondre avec HTTP 200 OK")
    }

    @Test(description = "Vérifie qu'un produit inexistant retourne une erreur 404")
    fun `GET non-existing product should return 404`() {
        val url = URL("$baseUrl/999999")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
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
        connection.setRequestProperty("Content-Type", "application/json")

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
}
