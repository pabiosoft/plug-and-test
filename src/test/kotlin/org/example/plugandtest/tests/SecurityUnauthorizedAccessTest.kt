package org.example.plugandtest.tests

import io.qameta.allure.Description
import org.testng.annotations.Test
import org.testng.Assert.assertEquals
import java.net.HttpURLConnection
import java.net.URL

/**
 * 📌 Type de test : Test de sécurité (Accès non autorisé)
 *
 * 🎯 Objectif :
 * - Vérifier que l'accès aux endpoints de l'API est correctement restreint aux utilisateurs authentifiés.
 * - Tester que les requêtes non authentifiées retournent une erreur HTTP 401.
 *
 * ✅ Succès :
 * - Toute tentative d'accès non authentifié doit être bloquée avec un HTTP 401 (Unauthorized).
 * - Aucune donnée ne doit être exposée sans authentification valide.
 */

class SecurityUnauthorizedAccessTest {

    private val apiUrl = "http://localhost:8082/api/products"

    @Test
    @Description("Vérifier que l'accès non authentifié est interdit")
    fun `test unauthorized access`() {
        val responseCode = sendRequestWithoutAuth()
        assertEquals(responseCode, 401, "⚠️ L’API laisse accéder aux données sans authentification !")
    }

    private fun sendRequestWithoutAuth(): Int {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        return connection.responseCode
    }
}
