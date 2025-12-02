import { useEffect, useMemo, useState } from 'react';
import { ApiClient } from './api';
import type { DashboardSnapshot, Incident, IncidentStatus, SensorEvent } from './types';

const statusStyles: Record<IncidentStatus, string> = {
  OPEN: 'bg-status-open/10 text-status-open ring-1 ring-status-open/30',
  IN_PROGRESS: 'bg-status-inProgress/10 text-status-inProgress ring-1 ring-status-inProgress/30',
  RESOLVED: 'bg-status-resolved/10 text-status-resolved ring-1 ring-status-resolved/30',
  CLOSED: 'bg-status-closed/10 text-status-closed ring-1 ring-status-closed/30',
};

const priorityCopy: Record<number, string> = {
  1: 'Info',
  2: 'Low',
  3: 'Medium',
  4: 'High',
  5: 'Critical',
};

const StatusBadge = ({ status }: { status: IncidentStatus }) => (
  <span className={`inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold uppercase tracking-wide ${statusStyles[status]}`}>
    {status.replace('_', ' ')}
  </span>
);

const SensorEventCard = ({ event }: { event: SensorEvent }) => (
  <div className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
    <div className="flex items-center justify-between text-sm text-slate-500">
      <span>{event.sensorId}</span>
      <span>{new Date(event.receivedAt).toLocaleTimeString()}</span>
    </div>
    <p className="mt-2 font-medium text-slate-900">{event.eventType}</p>
    <p className="text-sm text-slate-600">{event.payload}</p>
  </div>
);

function App() {
  const [incidents, setIncidents] = useState<Incident[]>([]);
  const [sensorEvents, setSensorEvents] = useState<SensorEvent[]>([]);
  const [snapshot, setSnapshot] = useState<DashboardSnapshot | null>(null);
  const [filter, setFilter] = useState<IncidentStatus | 'ALL'>('ALL');
  const [selectedIncident, setSelectedIncident] = useState<Incident | null>(null);

  useEffect(() => {
    ApiClient.getIncidents().then(setIncidents);
    ApiClient.getSensorEvents().then(setSensorEvents);
    ApiClient.getSnapshot().then(setSnapshot);
  }, []);

  const incidentsToShow = useMemo(() => {
    if (filter === 'ALL') return incidents;
    return incidents.filter((incident) => incident.status === filter);
  }, [incidents, filter]);

  const handleSelect = (incident: Incident) => {
    setSelectedIncident(incident);
  };

  const refreshData = () => {
    ApiClient.getIncidents().then(setIncidents);
    ApiClient.getSensorEvents().then(setSensorEvents);
    ApiClient.getSnapshot().then(setSnapshot);
  };

  const handleCreateDemoIncident = async () => {
    await ApiClient.createIncident();
    refreshData();
  };

  const handleSimulateSensor = async () => {
    await ApiClient.simulateSensorEvent();
    refreshData();
  };

  const handleResolveSelected = async () => {
    if (!selectedIncident) return;
    const updated = await ApiClient.updateIncidentStatus(selectedIncident.id, 'RESOLVED');
    if (updated) {
      setSelectedIncident(updated);
      refreshData();
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 pb-16">
      <div className="mx-auto max-w-6xl px-6 pt-12 text-white">
        <header className="rounded-3xl border border-slate-800 bg-gradient-to-br from-slate-900 via-slate-900 to-slate-950 p-8 shadow-2xl shadow-brand-500/10">
          <p className="text-sm uppercase tracking-[0.3em] text-brand-200">Smart City Ops Center</p>
          <div className="mt-4 flex flex-wrap items-center justify-between gap-4">
            <div>
              <h1 className="text-3xl font-semibold text-white sm:text-4xl">Incident Response Platform</h1>
              <p className="mt-2 max-w-2xl text-slate-300">
                Detect, triage en los incidenten op vanuit één command dashboard. Gedreven door sensordata, workflows en
                beveiligde API&apos;s.
              </p>
            </div>
            <div className="rounded-2xl border border-white/10 bg-white/5 px-5 py-4 text-right">
              <p className="text-sm text-slate-300">Laatste update</p>
              <p className="text-lg font-semibold text-white">
                {snapshot ? new Date(snapshot.lastUpdated).toLocaleTimeString() : 'Live'}
              </p>
            </div>
          </div>
        </header>

        <section className="mt-10 grid gap-6 md:grid-cols-3">
          <div className="rounded-2xl border border-slate-800 bg-white/5 p-6">
            <p className="text-sm text-slate-300">Openstaande incidenten</p>
            <p className="mt-3 text-4xl font-semibold text-white">{snapshot?.openIncidents ?? incidents.length}</p>
            <p className="text-sm text-brand-200">+2 in de laatste 24u</p>
          </div>
          <div className="rounded-2xl border border-slate-800 bg-white/5 p-6">
            <p className="text-sm text-slate-300">Gemiddelde doorlooptijd</p>
            <p className="mt-3 text-4xl font-semibold text-white">{snapshot?.avgResolutionMins ?? 38} min</p>
            <p className="text-sm text-brand-200">NOK doel: 45 min</p>
          </div>
          <div className="rounded-2xl border border-slate-800 bg-white/5 p-6">
            <p className="text-sm text-slate-300">Incident load</p>
            <div className="mt-4 flex gap-3 text-sm">
              {(['TRAFFIC', 'IOT_FAILURE', 'CYBER_ATTACK', 'OTHER'] as const).map((type) => (
                <div key={type} className="flex flex-1 flex-col rounded-xl border border-white/10 bg-white/5 px-3 py-2 text-center">
                  <span className="text-xs uppercase tracking-wide text-slate-400">{type.replace('_', ' ')}</span>
                  <span className="text-xl font-semibold text-white">{snapshot?.openByType[type] ?? 0}</span>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section className="mt-10 grid gap-6 lg:grid-cols-3">
          <div className="lg:col-span-2">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-lg font-semibold text-white">Incidenten</h2>
                <p className="text-sm text-slate-400">Live feed uit de backend API</p>
              </div>
              <div className="flex flex-wrap items-center justify-end gap-2">
                <div className="space-x-2">
                  {(['ALL', 'OPEN', 'IN_PROGRESS', 'RESOLVED'] as const).map((state) => (
                    <button
                      key={state}
                      className={`rounded-full px-4 py-2 text-xs font-medium ${
                        filter === state
                          ? 'bg-brand-500 text-white shadow-lg shadow-brand-500/30'
                          : 'border border-white/20 text-slate-200 hover:border-white/40'
                      }`}
                      onClick={() => setFilter(state as IncidentStatus | 'ALL')}
                    >
                      {state.replace('_', ' ')}
                    </button>
                  ))}
                </div>
                <div className="flex gap-2">
                  <button
                    className="rounded-full bg-white/10 px-3 py-1.5 text-xs font-medium text-brand-200 ring-1 ring-brand-400/40 hover:bg-white/20"
                    onClick={handleCreateDemoIncident}
                  >
                    + Demo-incident
                  </button>
                  <button
                    className="rounded-full bg-white/5 px-3 py-1.5 text-xs font-medium text-slate-200 ring-1 ring-white/20 hover:bg-white/10"
                    onClick={handleSimulateSensor}
                  >
                    Simuleer sensor
                  </button>
                </div>
              </div>
            </div>

            <div className="mt-4 overflow-hidden rounded-2xl border border-slate-800 bg-white">
              <table className="w-full table-auto text-left text-sm text-slate-600">
                <thead className="bg-slate-50 text-xs uppercase tracking-wider text-slate-500">
                  <tr>
                    <th className="px-5 py-3">Incident</th>
                    <th className="px-5 py-3">Status</th>
                    <th className="px-5 py-3">Prioriteit</th>
                    <th className="px-5 py-3">Locatie</th>
                    <th className="px-5 py-3">Bron</th>
                    <th className="px-5 py-3"></th>
                  </tr>
                </thead>
                <tbody>
                  {incidentsToShow.map((incident) => (
                    <tr key={incident.id} className="border-t border-slate-100 hover:bg-slate-50">
                      <td className="px-5 py-4">
                        <p className="font-medium text-slate-900">{incident.id}</p>
                        <p className="text-xs text-slate-500">{incident.description}</p>
                      </td>
                      <td className="px-5 py-4">
                        <StatusBadge status={incident.status} />
                      </td>
                      <td className="px-5 py-4">
                        <span
                          className={`rounded-full px-2 py-1 text-xs font-semibold ${
                            incident.priority >= 4 ? 'bg-red-100 text-red-700' : 'bg-slate-100 text-slate-700'
                          }`}
                        >
                          {priorityCopy[incident.priority]}
                        </span>
                      </td>
                      <td className="px-5 py-4">
                        <p className="text-sm font-medium text-slate-900">{incident.location}</p>
                        <p className="text-xs text-slate-500">{new Date(incident.createdAt).toLocaleString()}</p>
                      </td>
                      <td className="px-5 py-4">
                        <p className="text-sm text-slate-700">{incident.source}</p>
                        <p className="text-xs text-slate-500">{incident.assignedTo ?? 'Niet toegewezen'}</p>
                      </td>
                      <td className="px-5 py-4">
                        <button
                          className="text-sm font-semibold text-brand-600 hover:text-brand-700"
                          onClick={() => handleSelect(incident)}
                        >
                          Bekijk
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {selectedIncident && (
              <div className="mt-4 rounded-2xl border border-brand-200 bg-white p-6 shadow-lg">
                <div className="flex items-start justify-between">
                  <div>
                    <p className="text-sm text-slate-500">Geselecteerd incident</p>
                    <p className="text-xl font-semibold text-slate-900">{selectedIncident.id}</p>
                  </div>
                  <button className="text-sm text-slate-500" onClick={() => setSelectedIncident(null)}>
                    Sluit
                  </button>
                </div>
                <div className="mt-4 grid gap-4 text-sm text-slate-700 md:grid-cols-2">
                  <div>
                    <p className="font-medium">Beschrijving</p>
                    <p>{selectedIncident.description}</p>
                  </div>
      <div>
                    <p className="font-medium">Details</p>
                    <p>Locatie: {selectedIncident.location}</p>
                    <p>Bron: {selectedIncident.source}</p>
                    <p>Toegekend aan: {selectedIncident.assignedTo ?? 'Triage'}</p>
                  </div>
      </div>
                <div className="mt-4 flex flex-wrap gap-3">
                  <button
                    className="rounded-xl bg-brand-500 px-4 py-2 text-sm font-semibold text-white shadow-brand-500/40"
                    onClick={handleResolveSelected}
                  >
                    Markeer als resolved
                  </button>
                  <button
                    className="rounded-xl border border-slate-200 px-4 py-2 text-sm font-semibold text-slate-700"
                    onClick={handleSimulateSensor}
                  >
                    Genereer gerelateerd sensorevent
        </button>
                </div>
              </div>
            )}
          </div>

          <div className="space-y-6">
            <div className="rounded-2xl border border-slate-800 bg-white/5 p-6 text-white">
              <h3 className="text-lg font-semibold">Operations workflow</h3>
              <ol className="mt-4 space-y-4 text-sm text-slate-300">
                <li className="flex gap-3">
                  <span className="h-8 w-8 rounded-full border border-white/30 text-center leading-8">1</span>
                  Sensoren sturen events naar de ingest API.
                </li>
                <li className="flex gap-3">
                  <span className="h-8 w-8 rounded-full border border-white/30 text-center leading-8">2</span>
                  Business rules classificeren het incident en bepalen prioriteit.
                </li>
                <li className="flex gap-3">
                  <span className="h-8 w-8 rounded-full border border-white/30 text-center leading-8">3</span>
                  IncidentService wijst het ticket toe en triggert notificaties.
                </li>
                <li className="flex gap-3">
                  <span className="h-8 w-8 rounded-full border border-white/30 text-center leading-8">4</span>
                  Dashboard toont status, KPI&apos;s en audit trail voor overdracht.
                </li>
              </ol>
            </div>

            <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-xl">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold text-slate-900">Sensor feed</h3>
                <span className="text-xs font-medium uppercase tracking-wide text-brand-600">Live simulatie</span>
              </div>
              <div className="mt-4 space-y-3">
                {sensorEvents.slice(0, 4).map((event) => (
                  <SensorEventCard key={event.id} event={event} />
                ))}
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}

export default App;
