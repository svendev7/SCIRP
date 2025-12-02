export type IncidentType = 'TRAFFIC' | 'IOT_FAILURE' | 'CYBER_ATTACK' | 'OTHER';

export type IncidentStatus = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';

export type IncidentPriority = 1 | 2 | 3 | 4 | 5;

export interface Incident {
  id: string;
  type: IncidentType;
  status: IncidentStatus;
  priority: IncidentPriority;
  description: string;
  location: string;
  source: string;
  assignedTo?: string;
  createdAt: string;
  updatedAt: string;
}

export interface SensorEvent {
  id: string;
  sensorId: string;
  payload: string;
  eventType: string;
  receivedAt: string;
}

export interface DashboardSnapshot {
  totalIncidents: number;
  openIncidents: number;
  avgResolutionMins: number;
  openByType: Record<IncidentType, number>;
  lastUpdated: string;
}


