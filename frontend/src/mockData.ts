import type { DashboardSnapshot, Incident, SensorEvent } from './types';

export const mockIncidents: Incident[] = [
  {
    id: 'INC-1001',
    type: 'TRAFFIC',
    status: 'OPEN',
    priority: 4,
    description: 'Realtime congestion detected at Erasmusbrug inbound lanes.',
    location: 'Rotterdam - Erasmusbrug',
    source: 'Camera RDM-23',
    assignedTo: 'traffic-ops-1',
    createdAt: new Date(Date.now() - 15 * 60 * 1000).toISOString(),
    updatedAt: new Date(Date.now() - 5 * 60 * 1000).toISOString(),
  },
  {
    id: 'INC-1002',
    type: 'IOT_FAILURE',
    status: 'IN_PROGRESS',
    priority: 3,
    description: 'Air quality sensor AQ-44 stopped sending payloads.',
    location: 'Utrecht - Jaarbeursplein',
    source: 'Sensor AQ-44',
    assignedTo: 'iot-team',
    createdAt: new Date(Date.now() - 60 * 60 * 1000).toISOString(),
    updatedAt: new Date(Date.now() - 12 * 60 * 1000).toISOString(),
  },
  {
    id: 'INC-1003',
    type: 'CYBER_ATTACK',
    status: 'RESOLVED',
    priority: 5,
    description: 'Anomalous login pattern detected on mobility API gateway.',
    location: 'Cloud Region EU-west',
    source: 'SIEM',
    assignedTo: 'secops',
    createdAt: new Date(Date.now() - 5 * 60 * 60 * 1000).toISOString(),
    updatedAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
  },
];

export const mockSensorEvents: SensorEvent[] = [
  {
    id: 'EVT-2001',
    sensorId: 'CAM-RDM-23',
    payload: 'vehicle_count=125, avg_speed=18kmh',
    eventType: 'TRAFFIC',
    receivedAt: new Date().toISOString(),
  },
  {
    id: 'EVT-2002',
    sensorId: 'AQ-44',
    payload: 'signal_lost',
    eventType: 'IOT_FAILURE',
    receivedAt: new Date(Date.now() - 4 * 60 * 1000).toISOString(),
  },
  {
    id: 'EVT-2003',
    sensorId: 'IDS-GW-3',
    payload: 'blocked_ip=185.23.11.4',
    eventType: 'CYBER_ATTACK',
    receivedAt: new Date(Date.now() - 9 * 60 * 1000).toISOString(),
  },
];

export const mockSnapshot: DashboardSnapshot = {
  totalIncidents: mockIncidents.length,
  openIncidents: mockIncidents.filter((i) => i.status !== 'RESOLVED' && i.status !== 'CLOSED').length,
  avgResolutionMins: 42,
  openByType: mockIncidents.reduce(
    (acc, incident) => {
      acc[incident.type] = (acc[incident.type] ?? 0) + (incident.status === 'RESOLVED' ? 0 : 1);
      return acc;
    },
    {
      TRAFFIC: 0,
      IOT_FAILURE: 0,
      CYBER_ATTACK: 0,
      OTHER: 0,
    } as Record<Incident['type'], number>,
  ),
  lastUpdated: new Date().toISOString(),
};


