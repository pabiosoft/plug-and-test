package org.example.plugandtest.pages

import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import org.openqa.selenium.By
import io.qameta.allure.Step

class LoginPage {

    private val usernameField = By.id("username")
    private val passwordField = By.id("password")
    private val loginButton = By.className("radius")
    private val errorMessage = By.id("flash")

    @Step("Ouvrir la page de connexion")
    fun openLoginPage(): LoginPage {
        open("https://the-internet.herokuapp.com/login")
        return this
    }

    @Step("Saisir le nom d'utilisateur: {username}")
    fun enterUsername(username: String): LoginPage {
        `$`(usernameField).sendKeys(username)
        return this
    }

    @Step("Saisir le mot de passe")
    fun enterPassword(password: String): LoginPage {
        `$`(passwordField).sendKeys(password)
        return this
    }

    @Step("Cliquer sur le bouton de connexion")
    fun clickLogin(): LoginPage {
        `$`(loginButton).click()
        return this
    }

    @Step("Récupérer le message d'erreur")
    fun getErrorMessage(): String {
        return `$`(errorMessage).text
    }
}
