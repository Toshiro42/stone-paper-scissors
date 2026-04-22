# 🪨📄✂️ Stone Paper Scissors Arena

A full-stack 2-player Stone Paper Scissors game with 6 rounds per match, player history, and a REST API.

LIVE LINK :- http://13.60.244.103:8080

---

## Tech Stack

| Layer    | Technology                            |
|----------|---------------------------------------|
| Backend  | Java 17 · Spring Boot 3 · Spring Data JPA |
| Database | H2 (dev) · MySQL (production)        |
| Frontend | HTML5 · CSS3 · Vanilla JavaScript     |
| Build    | Maven                                 |
| Deploy   | AWS Elastic Beanstalk / GCP App Engine |

---

## Project Structure

```
stone-paper-scissors/
└── backend/
    ├── pom.xml
    └── src/main/
        ├── java/com/sps/
        │   ├── StonePaperScissorsApplication.java
        │   ├── config/CorsConfig.java
        │   ├── controller/GameController.java
        │   ├── dto/GameDto.java
        │   ├── model/
        │   │   ├── Game.java
        │   │   └── GameRound.java
        │   ├── repository/
        │   │   ├── GameRepository.java
        │   │   └── GameRoundRepository.java
        │   └── service/GameService.java
        └── resources/
            ├── application.properties
            └── static/
                ├── index.html      ← Game page
                └── history.html    ← History page
```

---

## Prerequisites

- Java 17+
- Maven 3.8+
- (Optional for production) MySQL 8+

---

## Run Locally

```bash
# 1. Clone the repo
git clone https://github.com/YOUR_USERNAME/stone-paper-scissors.git
cd stone-paper-scissors/backend

# 2. Build
mvn clean package -DskipTests

# 3. Run
java -jar target/stone-paper-scissors-1.0.0.jar
```

Open in browser:
- **Game** → http://localhost:8080
- **History** → http://localhost:8080/history.html
- **H2 Console** → http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:spsdb`)

---

## API Reference

### POST `/api/games`
Create a new game.
```json
// Request
{ "player1Name": "Alice", "player2Name": "Bob" }

// Response
{ "id": 1, "player1Name": "Alice", "player2Name": "Bob",
  "player1Score": 0, "player2Score": 0, "completedRounds": 0,
  "totalRounds": 6, "status": "IN_PROGRESS", ... }
```

### POST `/api/games/{id}/rounds`
Play a single round.
```json
// Request
{ "player1Choice": "STONE", "player2Choice": "PAPER" }

// Response
{
  "round": { "roundNumber": 1, "player1Choice": "STONE",
             "player2Choice": "PAPER", "result": "PLAYER2_WINS",
             "resultLabel": "Bob wins!" },
  "game":  { ...updated game state... }
}
```
Choices: `STONE` | `PAPER` | `SCISSORS`

### GET `/api/games/{id}`
Get a game with all rounds.

### GET `/api/games`
Get all games (newest first).

---

## Production Database (MySQL)

In `src/main/resources/application.properties`, comment out the H2 block and uncomment MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spsdb?useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

Also add MySQL dependency in `pom.xml` (already present, just uncomment it).

Create database:
```sql
CREATE DATABASE spsdb;
```

---

## Deploy to AWS (Elastic Beanstalk)

1. Build the JAR:
   ```bash
   mvn clean package -DskipTests
   ```

2. Go to [AWS Elastic Beanstalk Console](https://console.aws.amazon.com/elasticbeanstalk)

3. Create Application → Web server environment
   - Platform: **Java**
   - Upload `target/stone-paper-scissors-1.0.0.jar`

4. Add environment variable:
   ```
   SERVER_PORT = 5000
   ```
   *(Elastic Beanstalk routes port 80 → 5000)*

5. For RDS MySQL, add these environment variables:
   ```
   SPRING_DATASOURCE_URL      = jdbc:mysql://your-rds-endpoint:3306/spsdb
   SPRING_DATASOURCE_USERNAME = admin
   SPRING_DATASOURCE_PASSWORD = yourpassword
   ```

6. Deploy — your app will be live at `http://your-env.elasticbeanstalk.com`

---

## Deploy to GCP (App Engine)

1. Create `src/main/appengine/app.yaml`:
   ```yaml
   runtime: java17
   env: standard
   instance_class: F2
   ```

2. Build:
   ```bash
   mvn clean package -DskipTests
   ```

3. Deploy:
   ```bash
   gcloud app deploy target/stone-paper-scissors-1.0.0.jar
   ```

4. Open:
   ```bash
   gcloud app browse
   ```

For Cloud SQL (MySQL), set environment variables via `app.yaml` or GCP Secret Manager.

---

## Game Rules

- **Stone beats Scissors**
- **Scissors beat Paper**
- **Paper beats Stone**
- Same choice = **Tie**
- 6 rounds per game
- Player with most round wins is the overall winner

---

## Contact

Built for the WonderWhy technical assessment.
Questions? → thinker@wonderwhy.in
