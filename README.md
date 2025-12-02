# SCIRP – Smart City Incident Response Platform

Prototype for the “Smart City Incident Response Platform” hackathon challenge.  
Deze repo bevat:

- `frontend/`: React + TypeScript + Tailwind dashboard. Toont incidenten, KPI’s en sensor feed en valt terug op mock data wanneer de backend niet draait.
- `backend/`: Spring Boot 3 API met mock opslag (in-memory) voor incidenten, sensorevents en eenvoudige auth.

## Lokale development

### Backend (Java + Spring Boot)
```powershell
cd backend
./mvnw.cmd spring-boot:run
# API draait nu op http://localhost:8080
```

### Frontend (React + Vite)
```powershell
cd frontend
npm install   # eerste keer
npm run dev
# Dashboard staat op http://localhost:5173
```

Vite proxy’t niet automatisch naar de backend; de frontend leest via `VITE_API_BASE_URL` (standaard `http://localhost:8080/api`).  
Voor productie kun je bijv. in Vercel `VITE_API_BASE_URL=https://jouw-backend-url/api` zetten.

## Deploy op Vercel

1. Push deze repo naar GitHub.
2. Maak een nieuw Vercel project en wijs naar de `frontend/` map:
   - **Framework preset**: Vite
   - **Build command**: `npm run build`
   - **Output dir**: `dist`
   - **Root dir**: `frontend`
   - Voeg env `VITE_API_BASE_URL` toe naar je gehoste backend.
3. Voor de backend kun je Azure App Service, Render of Railway gebruiken. Deploy de Spring Boot jar (`mvn clean package`) en zorg dat `server.port` openstaat.

## Hoe dit past bij de opdracht

- **Software Development**: React dashboard + REST API, volledige mock data zodat demo altijd werkt.
- **Business IT & Management**: Dashboard illustreert KPI’s zoals open incidenten, doorlooptijd en workload per incidenttype; workflow copy beschrijft BPMN-stappen.
- **Technische Informatica**: Sensor simulatie & incident-aanmaak vanuit events modelleert IoT-integratie.
- **Cyber Security & Cloud**: Auth endpoint met hash-based validatie en STRIDE-ready threat surface (API gateway, RBAC rollen).
- **Test & Validatie**: API en frontend zijn modulair opgezet; je kunt eenvoudig unit/integration tests toevoegen (bijv. Spring MockMvc en React Testing Library).

Gebruik de modellen (class diagram, BPMN, ERD, threat model) om deze prototype code te ondersteunen in jullie overdracht en review. Vraag gerust feedback via Pull Requests om iteraties te documenteren.
