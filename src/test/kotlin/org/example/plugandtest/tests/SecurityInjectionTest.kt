package org.example.plugandtest.tests

import io.qameta.allure.Description
import org.testng.annotations.Test
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.net.HttpURLConnection
import java.net.URL

/**
 * 📌 Type de test : Test de sécurité (Injection SQL/NoSQL)
 *
 * 🎯 Objectif :
 * - Vérifier que l'API est protégée contre les attaques par injection SQL ou NoSQL.
 * - Tester si des charges malveillantes permettent d'exécuter des commandes ou contourner les sécurités.
 *
 * ✅ Succès :
 * - Toutes les tentatives d'injection doivent échouer.
 * - Aucun retour HTTP 201 ne doit être obtenu avec des payloads d'injection.
 */

class SecurityInjectionTest {

    private val loginUrl = "http://localhost:8081/sign_up"

    @Test
    @Description("Tester l’injection SQL / NoSQL dans la connexion")
    fun `test SQL injection on signup`() {
        val payloads = listOf(
            "\" OR 1=1; --",   // Injection SQL classique
            "'; DROP TABLE users; --", // Tentative de suppression de table :) la premiere fois cela avais tout casser
            "{ \"\$ne\": null }", // Injection NoSQL
            "admin' --",  // Contourner l’authentification
            "1' OR '1'='1" // Authentification forcée
        )

        for (payload in payloads) {
            val responseCode = sendSignUpRequest(payload, "password123")
            assertFalse(responseCode == 201, "⚠️ L’injection $payload a réussi, ce qui est une faille critique!")
        }
    }

    private fun sendSignUpRequest(email: String, password: String): Int {
        val url = URL(loginUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val jsonInputString = """{"email": "$email", "password": "$password"}"""
        connection.outputStream.use { os ->
            os.write(jsonInputString.toByteArray())
            os.flush()
        }

        return connection.responseCode
    }
}











//✅ 📌 Ce qu’on va vérifier
//✔️ L’API refuse les entrées malveillantes (400 ou 403).
//✔️ Aucune donnée ne fuit dans la réponse (ex: "SQL error" = FAIL).
//✔️ Aucune connexion n'est autorisée avec ces payloads.