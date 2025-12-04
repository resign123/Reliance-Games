# Reliance Games API

A Spring Boot application for player management, leaderboards, and game events.

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Gradle 7.0 or higher

## Database Setup

1. Create database and tables using the provided SQL file:
   ```sql
   mysql -u root -p < schema.sql
   ```
   Or manually execute the SQL commands in `schema.sql`.
   Note- 4 tables will be created in reliance_games db

2. Update `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

## Building the Project

```bash
./gradlew build
```

The JAR file will be generated at: `build/libs/reliance-games-1.0.0.jar`

## Running the Application

```bash
java -jar build/libs/reliance-games-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Player Registration

**POST** `/api/player/register`

Register a new player.

Request Body:
```json
{
  "deviceId": "device123",
  "userName": "Player1",
  "platform": "iOS"
}
```

Response:
```json
{
  "success": true,
  "playerId": 1,
  "message": "Player registered successfully"
}
```

### 2. Save Player Progression

**POST** `/api/player/progression`

Save player progression data.

Request Body:
```json
{
  "playerId": 1,
  "level": 10,
  "rank": 5,
  "gold": 1000,
  "cash": 500,
  "gem": 100,
  "rewardsCollected": "reward1,reward2",
  "lastActiveTime": "2024-01-15T10:30:00",
  "country": "USA"
}
```

Response:
```json
{
  "success": true,
  "message": "Progression saved successfully"
}
```

### 3. Submit Score

**POST** `/api/leaderboard/submit`

Submit a player's score.

Request Body:
```json
{
  "playerId": 1,
  "gameId": 1,
  "score": 1500,
  "timestamp": "2024-01-15T10:30:00"
}
```

Response:
```json
{
  "success": true,
  "message": "Score submitted successfully"
}
```

### 4. Get Top Players (Global)

**GET** `/api/leaderboard/top/{limit}`

Get top X players globally.

Example: `GET /api/leaderboard/top/10`

Response:
```json
{
  "success": true,
  "topPlayers": [
    {
      "playerId": 1,
      "userName": "Player1",
      "score": 1500,
      "gameId": 1,
      "timestamp": "2024-01-15T10:30:00"
    }
  ]
}
```

### 5. Get Top Players (Per Game)

**GET** `/api/leaderboard/top/{limit}?gameId={gameId}`

Get top X players for a specific game.

Example: `GET /api/leaderboard/top/10?gameId=1`

Response:
```json
{
  "success": true,
  "topPlayers": [
    {
      "playerId": 1,
      "userName": "Player1",
      "score": 1500,
      "gameId": 1,
      "timestamp": "2024-01-15T10:30:00"
    }
  ]
}
```

### 6. Get Top Players (By Country)

**GET** `/api/leaderboard/top/{limit}/country?gameId={gameId}&country={country}`

Get top X players for a specific game and country.

Example: `GET /api/leaderboard/top/10/country?gameId=1&country=USA`

Response:
```json
{
  "success": true,
  "topPlayers": [
    {
      "playerId": 1,
      "userName": "Player1",
      "score": 1500,
      "gameId": 1,
      "country": "USA",
      "timestamp": "2024-01-15T10:30:00"
    }
  ]
}
```

### 7. Schedule Game Event

**POST** `/api/events/schedule`

Schedule a new game event.

Request Body:
```json
{
  "name": "Tournament 1",
  "startTime": "2024-01-20T10:00:00",
  "endTime": "2024-01-25T18:00:00",
  "configuration": "{\"rewards\":[\"gold\",\"gem\"],\"eligibility\":\"level>5\"}"
}
```

Response:
```json
{
  "success": true,
  "eventId": 1,
  "message": "Event scheduled successfully"
}
```

### 8. Update Game Event

**PUT** `/api/events/{eventId}`

Update an existing game event.

Request Body (all fields optional):
```json
{
  "name": "Updated Tournament",
  "startTime": "2024-01-21T10:00:00",
  "endTime": "2024-01-26T18:00:00",
  "configuration": "{\"rewards\":[\"gold\"]}"
}
```

Response:
```json
{
  "success": true,
  "message": "Event updated successfully"
}
```

### 9. Get Available Events

**GET** `/api/events/available`

Get all currently active events (events where current time is between start and end time).

Response:
```json
{
  "success": true,
  "events": [
    {
      "eventId": 1,
      "name": "Tournament 1",
      "startTime": "2024-01-20T10:00:00",
      "endTime": "2024-01-25T18:00:00",
      "configuration": "{\"rewards\":[\"gold\",\"gem\"]}"
    }
  ]
}
```

## Testing the APIs

You can use tools like Postman, cURL, or any HTTP client to test the APIs.

### Example cURL commands for all 9 APIs:

**1. Register a player:**
```bash
curl -X POST http://localhost:8080/api/player/register \
  -H "Content-Type: application/json" \
  -d "{\"deviceId\":\"device123\",\"userName\":\"Player1\",\"platform\":\"iOS\"}"
```

**2. Save player progression:**
```bash
curl -X POST http://localhost:8080/api/player/progression \
  -H "Content-Type: application/json" \
  -d "{\"playerId\":1,\"level\":10,\"rank\":5,\"gold\":1000,\"cash\":500,\"gem\":100,\"rewardsCollected\":\"reward1,reward2\",\"country\":\"USA\"}"
```

**3. Submit score:**
```bash
curl -X POST http://localhost:8080/api/leaderboard/submit \
  -H "Content-Type: application/json" \
  -d "{\"playerId\":1,\"gameId\":1,\"score\":1500,\"timestamp\":\"2024-01-15T10:30:00\"}"
```

**4. Get top players (global):**
```bash
curl http://localhost:8080/api/leaderboard/top/10
```

**5. Get top players (per game):**
```bash
curl "http://localhost:8080/api/leaderboard/top/10?gameId=1"
```

**6. Get top players (by country):**
```bash
curl "http://localhost:8080/api/leaderboard/top/10/country?gameId=1&country=USA"
```

**7. Schedule game event:**
```bash
curl -X POST http://localhost:8080/api/events/schedule \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Tournament 1\",\"startTime\":\"2024-01-20T10:00:00\",\"endTime\":\"2024-12-31T18:00:00\",\"configuration\":\"{\\\"rewards\\\":[\\\"gold\\\",\\\"gem\\\"],\\\"eligibility\\\":\\\"level>5\\\"}\"}"
```

**8. Update game event:**
```bash
curl -X PUT http://localhost:8080/api/events/1 \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Updated Tournament\",\"startTime\":\"2024-01-21T10:00:00\",\"endTime\":\"2024-12-31T18:00:00\",\"configuration\":\"{\\\"rewards\\\":[\\\"gold\\\"]}\"}"
```

**9. Get available events:**
```bash
curl http://localhost:8080/api/events/available
```


## Database Name

The database name is: `reliance_games`

## Notes

- All timestamps should be in ISO-8601 format: `YYYY-MM-DDTHH:mm:ss`
- The application uses Spring Data JPA with Hibernate for database operations
- Caching is enabled for leaderboard queries to improve performance
- The application automatically creates/updates database tables on startup

