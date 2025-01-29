package org.example.plugandtest.tests

import io.qameta.allure.Description
import org.testng.annotations.Test
import org.testng.annotations.BeforeClass
import org.testng.annotations.AfterClass
import org.testng.Assert.assertEquals
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * 📌 Type de test : Test de charge (Load Test)
 *
 * 🎯 Objectif :
 * - Simuler 100 utilisateurs se connectant simultanément à l'API `/api/login`.
 * - Mesurer la stabilité et les performances de l'API sous charge élevée.
 * - Vérifier le taux de succès des connexions et analyser les échecs éventuels.
 * - Calculer le temps de réponse moyen pour évaluer les performances.
 *
 * 🛠️ Méthodologie :
 * - Utilisation d'un pool de threads pour simuler 100 requêtes simultanées.
 * - Envoi de requêtes `POST` avec des identifiants valides.
 * - Collecte des temps de réponse et des codes de réponse HTTP pour analyse.
 *
 * ✅ Critères de succès :
 * - 100% des connexions doivent réussir (`HTTP 200`).
 * - Temps de réponse moyen raisonnable.
 * - Aucun plantage de l'API ou dépassement de délai.
 */
class LoginLoadTest {

    private val apiUrl = "http://localhost:8081/api/login"
    private val username = "dev4@mo.com"
    private val password = "dev4"

    private val threadPool = Executors.newFixedThreadPool(100) // 100 Threads
    private val responses = mutableListOf<Int>()
    private val responseTimes = mutableListOf<Long>()

    @BeforeClass
    fun setup() {
        println("🔥 Début du test de charge : 100 connexions simultanées")
    }

    @Test
    @Description("Simuler 100 connexions simultanées")
    fun `simulate 100 concurrent logins`() {
        val startTime = System.currentTimeMillis()

        repeat(100) { // 100 utilisateurs
            threadPool.execute {
                val responseTime = sendLoginRequest()
                synchronized(responses) {
                    responses.add(responseTime.first)
                    responseTimes.add(responseTime.second)
                }
            }
        }

        threadPool.shutdown()
        threadPool.awaitTermination(30, TimeUnit.SECONDS) // Attendre la fin de tous les threads

        val totalTime = System.currentTimeMillis() - startTime
        val successCount = responses.count { it == 200 }
        val failureCount = responses.count { it != 200 }
        val avgResponseTime = responseTimes.average()

        println("✅ Test terminé en ${totalTime}ms")
        println("✔️ Succès: $successCount / 100")
        println("❌ Échecs: $failureCount / 100")
        println("⏳ Temps de réponse moyen: ${"%.2f".format(avgResponseTime)}ms")

        assertEquals(successCount, 100, "Tous les utilisateurs doivent pouvoir se connecter")
    }

    private fun sendLoginRequest(): Pair<Int, Long> {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val jsonInputString = """{"email": "$username", "password": "$password"}"""
        connection.outputStream.use { os ->
            os.write(jsonInputString.toByteArray())
            os.flush()
        }

        val start = System.currentTimeMillis()
        val responseCode = connection.responseCode
        val responseTime = System.currentTimeMillis() - start

        return Pair(responseCode, responseTime)
    }

    @AfterClass
    fun teardown() {
        println("🛑 Fin du test de charge")
    }
}


















//🔍 📌 Explication du Test
//On crée un pool de 100 threads (Executors.newFixedThreadPool(100)).
//Chaque thread exécute sendLoginRequest() pour envoyer une requête POST /api/login.
//On stocke les résultats :
//Codes de réponse HTTP (200, 500, etc.).
//Temps de réponse en millisecondes.
//On affiche les résultats :
//Combien d’utilisateurs ont réussi à se connecter ?
//Combien ont échoué ?
//Temps de réponse moyen.

//✅ 📌 Ce que ce test permet de vérifier
//✔️ Si 100 utilisateurs peuvent se connecter en même temps sans problème.
//✔️ Si le temps de réponse reste stable ou augmente sous charge.
//✔️ Si l’API retourne des erreurs (500, 429 - Rate Limit) sous forte charge.