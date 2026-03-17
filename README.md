# HackHub

Piattaforma web per la gestione di hackathon, sviluppata con **Spring Boot 3.4.3** e **Java 21**.

## Funzionalità

- **Gestione hackathon** — creazione, staff, iscrizione team, sottomissioni, valutazioni, proclamazione vincitore
- **Ciclo di vita** — `IN_ISCRIZIONE → IN_CORSO → IN_VALUTAZIONE → CONCLUSO` (State Pattern)
- **Creazione utenti** — organizzatori, giudici, mentori, membri team (Factory Pattern)
- **Servizi esterni** — integrazione con sistema Calendar e sistema di Pagamento
- **Persistenza** — MySQL con Spring Data JPA

## Requisiti

| Software | Versione |
| -------- | -------- |
| Java JDK | 21+      |
| Maven    | 3.6.3+   |
| MySQL    | 8.0+     |

## Setup

### 1. Configura MySQL

Il server di prova è configurato con:

```
Porta: 3306
Utente: Prova
Password: Password123!
```

Queste credenziali sono già impostate in `src/main/resources/application.properties`. Se usi credenziali diverse, modificale:

```properties
spring.datasource.username=TUO_USERNAME
spring.datasource.password=TUA_PASSWORD
```

### 2. Compila

```bash
mvn clean compile
```

### 3. Avvia

```bash
mvn spring-boot:run
```

L'applicazione sarà disponibile su `http://localhost:8080`. Il database `hackhub` viene creato automaticamente.

### 4. Esegui i test

```bash
mvn test
```

## API REST

| Endpoint                                 | Metodo   | Descrizione                        |
| ---------------------------------------- | -------- | ---------------------------------- |
| `/api/hackathon`                         | GET/POST | Lista/Crea hackathon               |
| `/api/hackathon/{id}/avanza-stato`       | PUT      | Avanza stato nel ciclo di vita     |
| `/api/hackathon/{id}/proclama-vincitore` | POST     | Proclama vincitore                 |
| `/api/team`                              | GET/POST | Lista/Crea team                    |
| `/api/team/{id}/iscrivi/{hackathonId}`   | POST     | Iscrivi team a hackathon           |
| `/api/sottomissione`                     | POST     | Invia sottomissione                |
| `/api/valutazione`                       | POST     | Valuta sottomissione (0-10)        |
| `/api/utente`                            | GET/POST | Lista/Registra utente              |
| `/api/staff`                             | GET/POST | Lista/Crea staff (Factory Pattern) |

## Popolare il database

Importa in Postman il file `docs/HackHub_Postman_Collection.json` ed esegui le richieste in ordine numerico.

## Design Pattern

- **State Pattern** — gestione del ciclo di vita dell'hackathon tramite l'interfaccia `StatoHackathon` e le classi `StatoInIscrizione`, `StatoInCorso`, `StatoInValutazione`, `StatoConcluso`
- **Factory Pattern** — creazione centralizzata di utenti e staff tramite `UtenteFactory`

## Struttura progetto

```
src/main/java/unicam/hackhub/
├── controller/     ← REST API (6 controller)
├── handler/        ← Logica business (5 handler)
├── model/          ← Entità JPA e design pattern (20 classi)
├── repository/     ← Accesso dati (5 repository)
└── service/        ← Servizi esterni (Calendar, Payment)
```
