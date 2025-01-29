## chapitre 1

🛠 Détaillons les types de tests que tu as écrit :
1️⃣ Tests d’interface (UI Tests)

- 📌 Exemple : login button should be visible
- 🔎 Ce que ça teste : Vérifie que les éléments de la page sont présents et visibles (ex. le bouton de connexion).
- 2️⃣ Tests fonctionnels

- 📌 Exemple : login with invalid credentials should show error message
- 🔎 Ce que ça teste : Vérifie que l'application réagit correctement à une mauvaise saisie (ex. mauvais identifiants affichent un message d’erreur).

## chapitre 2

1️⃣ Tests API (End-to-End)

- ✅ Tester les requêtes HTTP (POST /login, GET /user)
📌 Exemple : Vérifier que l'API retourne un 200 OK pour une requête valide.
- 2️⃣ Tests de Performance (Load Testing)

- ✅ Vérifier combien d'utilisateurs peuvent se connecter en même temps.
- 📌 Exemple : Simuler 100 connexions simultanées et vérifier la stabilité.
- 3️⃣ Tests de Sécurité

- ✅ Vérifier si des failles de sécurité sont exploitables (SQL Injection, XSS, etc.).
- 📌 Exemple : Tenter d’injecter du code malveillant dans le formulaire de login.