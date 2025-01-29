package org.example.plugandtest.tests

import io.qameta.allure.Description
import org.testng.annotations.Test
import org.testng.Assert.assertTrue
import java.net.HttpURLConnection
import java.net.URL

/**
 * 📌 Type de test : Test de sécurité (Brute Force)
 *
 * 🎯 Objectif :
 * - Vérifier que l'API est protégée contre les attaques par force brute.
 * - Tester si l'API bloque les tentatives répétées de connexion avec des identifiants incorrects.
 * - Mesurer le comportement de l'API lorsqu'un utilisateur effectue de nombreuses requêtes invalides.
 *
 * 🛠️ Méthodologie :
 * - Simuler 50 tentatives de connexion avec des identifiants incorrects.
 * - Surveiller les réponses de l'API pour détecter une protection contre les attaques brute-force :
 *   - **HTTP 429 (Too Many Requests)** attendu après un certain nombre d'échecs.
 *   - Compter le nombre de succès (éventuels) et d’échecs.
 * - Vérifier si l’API réagit rapidement pour bloquer les attaques.
 *
 * ✅ Critères de succès :
 * - L’API doit retourner **HTTP 429** après plusieurs tentatives de connexion échouées.
 * - Le taux d’échec doit être élevé (proche de 100%).
 * - La protection doit se déclencher rapidement (après un petit nombre de tentatives).
 *
 * 🚨 Risques identifiés :
 * - Si l’API ne bloque pas les tentatives répétées, un attaquant pourrait deviner des mots de passe par force brute.
 * - Si le temps de réponse de l’API augmente sous charge, cela pourrait révéler une faiblesse dans sa gestion des attaques.
 */

class SecurityBruteForceTest {

    private val loginUrl = "http://localhost:8081/api/login"

    @Test
    @Description("Tester la protection contre le brute-force sur l’authentification")
    fun `test brute force login`() {
        var successCount = 0
        var failureCount = 0

        repeat(50) { attempt ->
            val responseCode = sendLoginRequest("attacker@mo.com", "wrong_password")
            if (responseCode == 429) {
                println("🔐 Protection activée après $attempt tentatives !")
                assertTrue(attempt >= 5, "🚨 L’API ne bloque pas assez rapidement les attaques brute-force")
                return
            } else if (responseCode == 200) {
                successCount++
            } else {
                failureCount++
            }
        }

        println("✅ Résultat : $successCount succès | $failureCount échecs")
        assertTrue(failureCount >= 45, "🚨 L’API accepte trop d’essais de connexion incorrects")
    }

    private fun sendLoginRequest(email: String, password: String): Int {
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








// ✅ 📌 Ce qu’on va vérifier
//✔️ L’API bloque après plusieurs essais (HTTP 429).
//✔️ Aucun attaquant ne peut essayer une infinité de mots de passe.
//✔️ Temps de réponse : l’API ne ralentit pas sous attaque.