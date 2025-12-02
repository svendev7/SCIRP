import { mockIncidents, mockSensorEvents, mockSnapshot } from './mockData';
import type { DashboardSnapshot, Incident, IncidentStatus, SensorEvent } from './types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

const fetchWithFallback = async <T>(path: string, fallback: T): Promise<T> => {
  try {
    const response = await fetch(`${API_BASE_URL}${path}`);
    if (!response.ok) {
      throw new Error(`Request failed: ${response.status}`);
    }
    return (await response.json()) as T;
  } catch {
    return fallback;
  }
};

export const ApiClient = {
  getIncidents: () => fetchWithFallback<Incident[]>('/incidents', mockIncidents),
  getSensorEvents: () => fetchWithFallback<SensorEvent[]>('/sensors/events', mockSensorEvents),
  getSnapshot: () => fetchWithFallback<DashboardSnapshot>('/metrics/snapshot', mockSnapshot),

  async createIncident(): Promise<Incident | null> {
    try {
      const response = await fetch(`${API_BASE_URL}/incidents`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          type: 'TRAFFIC',
          description: 'Demo-incident aangemaakt vanaf het dashboard',
          location: 'Rotterdam - Demo kruispunt',
          source: 'Dashboard-operator',
          priority: 3,
        }),
      });
      if (!response.ok) throw new Error('Failed to create incident');
      return (await response.json()) as Incident;
    } catch {
      return null;
    }
  },

  async updateIncidentStatus(id: string, status: IncidentStatus): Promise<Incident | null> {
    try {
      const response = await fetch(`${API_BASE_URL}/incidents/${id}/status`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status }),
      });
      if (!response.ok) throw new Error('Failed to update status');
      return (await response.json()) as Incident;
    } catch {
      return null;
    }
  },

  async simulateSensorEvent(): Promise<SensorEvent | null> {
    try {
      const response = await fetch(`${API_BASE_URL}/sensors/events`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          sensorId: 'SIM-TRAF-01',
          payload: 'vehicle_count=180,avg_speed=12kmh',
          eventType: 'TRAFFIC',
        }),
      });
      if (!response.ok) throw new Error('Failed to send sensor event');
      return (await response.json()) as SensorEvent;
    } catch {
      return null;
    }
  },
};


