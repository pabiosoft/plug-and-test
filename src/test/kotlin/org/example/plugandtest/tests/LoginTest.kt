package org.example.plugandtest.tests


import com.codeborne.selenide.Selenide.closeWebDriver
import io.qameta.allure.Description
import org.example.plugandtest.pages.LoginPage
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide.closeWebDriver
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide

import org.testng.Assert.assertTrue

/**
 * 📌 Type de test : Tests d'interface utilisateur (UI tests)
 *
 * 🎯 Objectif : Vérifier que la fonctionnalité de login fonctionne comme prévu pour un utilisateur :
 * - Vérifier la présence des éléments (champ email, champ mot de passe, bouton login).
 * - Tester la gestion des erreurs avec des identifiants invalides.
 * - Assurer que l'interface réagit correctement aux actions de l'utilisateur.
 *
 * 🛠️ Outils :
 * - Selenium avec Selenide pour une interaction fluide avec l'UI.
 * - TestNG pour l'organisation et l'exécution des tests.
 * - Allure pour générer des rapports de tests détaillés.
 */
class LoginTest {
    private lateinit var loginPage: LoginPage

    @BeforeClass
    fun setup() {
        Configuration.browser = "firefox"  // 🔥 Définit Firefox comme navigateur
        Configuration.browserSize = "1280x800" // Optionnel : Définit la taille de la fenêtre
        SelenideLogger.addListener("allure", AllureSelenide())

        loginPage = LoginPage()
    }

    @Test
    @Description("Vérifier que le bouton de connexion est visible")
    fun `login button should be visible`() {
        loginPage.openLoginPage()
        assertTrue(loginPage.clickLogin() != null, "Le bouton de connexion n'est pas visible")
    }

    @Test
    @Description("Vérifier l'erreur avec des identifiants invalides")
    fun `login with invalid credentials should show error message`() {
        loginPage.openLoginPage()
            .enterUsername("invalid_user")
            .enterPassword("wrong_password")
            .clickLogin()

        assertTrue(loginPage.getErrorMessage().contains("Your username is invalid!"), "Le message d'erreur n'est pas affiché")
    }

    @AfterClass
    fun teardown() {
        closeWebDriver()
    }
}















//Ces tests UI automatisés sont essentiels pour garantir que les utilisateurs
//finaux ne rencontreront pas de problèmes dans les fonctionnalités de base, comme
//la connexion. Ils forment une première ligne de défense pour la qualité de l'application.