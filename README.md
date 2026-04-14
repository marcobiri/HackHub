# HackHub

Piattaforma web per la gestione di hackathon, sviluppata con **Spring Boot 3.4.3** e **Java 21**.

## Funzionalità

- **Gestione hackathon** — creazione, configurazione staff, iscrizione team, sottomissioni, valutazioni, proclamazione vincitore
- **Ciclo di vita** — `IN_ISCRIZIONE → IN_CORSO → IN_VALUTAZIONE → CONCLUSO` (State Pattern)
- **Creazione utenti** — registrazione, login e creazione staff con ruoli Organizzatore, Giudice, Mentore (Factory Pattern)
- **Gestione team** — creazione team, sistema di inviti con accettazione/rifiuto, iscrizione ad hackathon
- **Sottomissioni e valutazioni** — invio e aggiornamento sottomissioni, valutazione con punteggio da parte dei giudici
- **Supporto e segnalazioni** — richieste di supporto (call con mentore via Calendar), segnalazioni violazione con workflow di gestione (`APERTA → IN_REVISIONE → CONFERMATA/RIGETTATA`)
- **Servizi esterni** — integrazione con sistema Calendar e sistema di Pagamento
- **Persistenza** — MySQL con Spring Data JPA (H2 per i test)

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
Utente: prova
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

I test utilizzano un database H2 in-memory e non richiedono MySQL.

## API REST

### Hackathon

| Endpoint                                 | Metodo | Descrizione                       |
| ---------------------------------------- | ------ | --------------------------------- |
| `/api/hackathon`                         | GET    | Lista tutti gli hackathon         |
| `/api/hackathon/{id}`                    | GET    | Dettaglio hackathon               |
| `/api/hackathon`                         | POST   | Crea un nuovo hackathon           |
| `/api/hackathon/{id}/avanza-stato`       | PUT    | Avanza stato nel ciclo di vita    |
| `/api/hackathon/{id}/mentore`            | POST   | Aggiunge un mentore all'hackathon |
| `/api/hackathon/{id}/proclama-vincitore` | POST   | Proclama il vincitore             |
| `/api/hackathon/stato/{stato}`           | GET    | Filtra hackathon per stato        |

### Utente

| Endpoint                         | Metodo | Descrizione               |
| -------------------------------- | ------ | ------------------------- |
| `/api/utente`                    | GET    | Lista tutti gli utenti    |
| `/api/utente/{id}`               | GET    | Dettaglio utente          |
| `/api/utente`                    | POST   | Registra un nuovo utente  |
| `/api/utente/cerca?username=...` | GET    | Cerca utente per username |
| `/api/utente/login`              | POST   | Effettua il login         |

### Staff

| Endpoint     | Metodo | Descrizione                         |
| ------------ | ------ | ----------------------------------- |
| `/api/staff` | GET    | Lista tutti i membri dello staff    |
| `/api/staff` | POST   | Crea membro staff (Factory Pattern) |

### Team

| Endpoint                                   | Metodo | Descrizione                        |
| ------------------------------------------ | ------ | ---------------------------------- |
| `/api/team`                                | GET    | Lista tutti i team                 |
| `/api/team/{id}`                           | GET    | Dettaglio team                     |
| `/api/team`                                | POST   | Crea un nuovo team                 |
| `/api/team/{teamId}/iscrivi/{hackathonId}` | POST   | Iscrivi team a hackathon           |
| `/api/team/{teamId}/invita`                | POST   | Invia invito a un utente           |
| `/api/team/inviti/{utenteId}`              | GET    | Lista inviti ricevuti da un utente |
| `/api/team/inviti/{invitoId}/accetta`      | PUT    | Accetta un invito                  |
| `/api/team/inviti/{invitoId}/rifiuta`      | PUT    | Rifiuta un invito                  |

### Sottomissione

| Endpoint                                     | Metodo | Descrizione                 |
| -------------------------------------------- | ------ | --------------------------- |
| `/api/sottomissione`                         | POST   | Invia una sottomissione     |
| `/api/sottomissione/{id}`                    | PUT    | Aggiorna una sottomissione  |
| `/api/sottomissione/hackathon/{hackathonId}` | GET    | Sottomissioni per hackathon |
| `/api/sottomissione/team/{teamId}`           | GET    | Sottomissioni per team      |

### Valutazione

| Endpoint                                   | Metodo | Descrizione               |
| ------------------------------------------ | ------ | ------------------------- |
| `/api/valutazione`                         | POST   | Valuta una sottomissione  |
| `/api/valutazione/hackathon/{hackathonId}` | GET    | Valutazioni per hackathon |
| `/api/valutazione/giudice/{giudiceId}`     | GET    | Valutazioni per giudice   |

### Supporto

| Endpoint                                           | Metodo | Descrizione                        |
| -------------------------------------------------- | ------ | ---------------------------------- |
| `/api/supporto/richiesta`                          | POST   | Crea richiesta di supporto         |
| `/api/supporto/richieste/{hackathonId}`            | GET    | Lista richieste per hackathon      |
| `/api/supporto/richiesta/{id}/call`                | POST   | Mentore propone una call           |
| `/api/supporto/segnalazione`                       | POST   | Segnala violazione del regolamento |
| `/api/supporto/segnalazioni/{hackathonId}`         | GET    | Lista segnalazioni per hackathon   |
| `/api/supporto/segnalazione/{id}/prendi-in-carico` | PUT    | Prende in carico la segnalazione   |
| `/api/supporto/segnalazione/{id}/conferma`         | PUT    | Conferma la violazione             |
| `/api/supporto/segnalazione/{id}/rigetta`          | PUT    | Rigetta la segnalazione            |

## Design Pattern

- **State Pattern** — gestione del ciclo di vita dell'hackathon tramite l'interfaccia `StatoHackathon` e le classi `StatoInIscrizione`, `StatoInCorso`, `StatoInValutazione`, `StatoConcluso`
- **Factory Pattern** — creazione centralizzata dello staff tramite `UtenteFactory` con ruoli `ORGANIZZATORE`, `GIUDICE`, `MENTORE`

## Struttura progetto

```
src/main/java/unicam/hackhub/
├── controller/     ← REST API (7 controller)
├── handler/        ← Logica business (5 handler)
├── model/
│   ├── factory/    ← UtenteFactory (Factory Pattern)
│   ├── hackathon/  ← Hackathon, StatoHackathon e stati concreti (State Pattern)
│   ├── payment/    ← Premio
│   ├── staff/      ← MembroStaff, Organizzatore, Giudice, Mentore, RuoloStaff
│   ├── submission/ ← Sottomissione, Valutazione
│   ├── support/    ← RichiestaSupporto, SegnalazioneViolazione
│   ├── team/       ← Team, MembroTeam, InvitoTeam
│   └── user/       ← Utente, Visitatore
├── repository/     ← Accesso dati (6 repository)
└── service/        ← Servizi esterni (Calendar, Payment)
```

## Test

Il progetto include 4 classi di test:

| Classe                 | Descrizione                                                |
| ---------------------- | ---------------------------------------------------------- |
| `HackathonStatoTest`   | Verifica le transizioni di stato dell'hackathon            |
| `HandlerHackathonTest` | Test della logica business per la gestione degli hackathon |
| `HandlerSupportoTest`  | Test del supporto e gestione segnalazioni                  |
| `UtenteFactoryTest`    | Verifica la creazione di staff tramite Factory Pattern     |
