# 📌 Plug-and-Test

🛠 **Plug-and-Test** est un framework de tests automatisés conçu pour valider les interfaces web et les API, en mettant l'accent sur la robustesse et la sécurité des applications.

## 🚀 **Prérequis**

Avant de commencer, assurez-vous d’avoir installé :
- **Java 21** (ou une version compatible)
- **Gradle**
- **Firefox** (pour les tests UI)
- **Geckodriver** (si Firefox est utilisé pour Selenium)

## 📂 **Structure du projet**

```
plug-and-test/
│── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   ├── pages/      # 📌 Page Object Models pour UI tests (pour l'instant il est dans tests) 
│   ├── test/
│   │   ├── kotlin/
│   │   │   ├── tests/      # 📌 Tests automatisés API & UI
│── build.gradle.kts        # 📌 Configuration Gradle
│── README.md               # 📌 Documentation du projet
```

## ⚙️ **Installation & Configuration**

1️⃣ **Cloner le projet**
```sh
git clone https://github.com/pabiosoft/plug-and-test.git
cd plug-and-test
```

2️⃣ **Configurer les dépendances**
```sh
./gradlew build
```

3️⃣ **Vérifier la configuration**
```sh
./gradlew dependencies
```

---

## 🖥️ **Exécution des Tests**

### ✅ **Tests UI (Selenium + Selenide)**
Pour tester l’interface utilisateur :
```sh
./gradlew test --tests "org.pabiosoft.tests.LoginTest" # pour l'instant org.example.tests.someTest
```

### 🔍 **Tests API**
Pour tester les endpoints API :
```sh
./gradlew test --tests "org.pabiosoft.tests.ProductApiTest"
```

### 🔥 **Tests de Charge**
Simuler 100 connexions simultanées :
```sh
./gradlew test --tests "org.pabiosoft.tests.LoginLoadTest"
```

### 🛡 **Tests de Sécurité**
Exécuter les tests contre les attaques SQL et Brute Force :
```sh
./gradlew test --tests "org.pabiosoft.tests.SecurityTests"
```

---

## 📊 **Génération des Rapports**

### 🏆 **Rapports Allure**
Générer un rapport détaillé après l’exécution des tests :
```sh
./gradlew allureReport
```
Ouvrir le rapport dans un navigateur :
```sh
./gradlew allureServe
```

---

## 🛠 **Personnalisation**

### Modifier le navigateur utilisé par défaut
Dans `LoginTest.kt`, changez :
```kotlin
Configuration.browser = "firefox"  // 🔥 Par défaut
```
Par :
```kotlin
Configuration.browser = "chrome"   // Pour utiliser Chrome
```

### Modifier l’URL des APIs
Dans les fichiers `ProductApiTest.kt` et `LoginLoadTest.kt`, mettez à jour les valeurs :
```kotlin
private val apiUrl = "http://localhost:8082/api/products"
```

---

## 🤝 **Contribution**
- **Fork & Pull Request** : Toute amélioration est la bienvenue !
- **Issues** : Signalez les problèmes ou proposez des améliorations.

📌 **Auteur** : [Pabiosoft](https://pabiosoft.com)

🚀 **Bon test à toute l’équipe !** 🎯

