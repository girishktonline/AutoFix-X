import React, { useState, useEffect } from 'react';
import { mechanicAPI } from '../services/api';
import './Dashboard.css';

function MechanicDashboard() {
  const [jobs, setJobs] = useState([]);
  const [earnings, setEarnings] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchJobs();
    fetchEarnings();
  }, []);

  const fetchJobs = async () => {
    try {
      const response = await mechanicAPI.getAssignedJobs();
      setJobs(response.data);
    } catch (err) {
      setError('Failed to load jobs');
    } finally {
      setLoading(false);
    }
  };

  const fetchEarnings = async () => {
    try {
      const response = await mechanicAPI.getEarnings();
      setEarnings(response.data.totalEarnings || 0);
    } catch (err) {
      console.error('Failed to load earnings');
    }
  };

  const handleStartJob = async (jobId) => {
    try {
      await mechanicAPI.startJob(jobId);
      fetchJobs();
    } catch (err) {
      setError('Failed to start job');
    }
  };

  const handleCompleteJob = async (jobId) => {
    try {
      await mechanicAPI.completeJob(jobId, { actualCost: 0 });
      fetchJobs();
    } catch (err) {
      setError('Failed to complete job');
    }
  };

  if (loading) {
    return <div className="loading"><div className="spinner"></div></div>;
  }

  const activeJobs = jobs.filter(j => j.status === 'IN_PROGRESS' || j.status === 'ASSIGNED');
  const completedJobs = jobs.filter(j => j.status === 'COMPLETED');

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Mechanic Dashboard</h1>
        <p>Manage your service jobs and earnings</p>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="earning-summary">
        <h2>Your Earnings</h2>
        <div className="earning-grid">
          <div className="earning-item">
            <h4>Total Earnings</h4>
            <div className="earning-item-value">${earnings.toFixed(2)}</div>
          </div>
          <div className="earning-item">
            <h4>Active Jobs</h4>
            <div className="earning-item-value">{activeJobs.length}</div>
          </div>
          <div className="earning-item">
            <h4>Completed</h4>
            <div className="earning-item-value">{completedJobs.length}</div>
          </div>
          <div className="earning-item">
            <h4>Average Rating</h4>
            <div className="earning-item-value">4.8</div>
          </div>
        </div>
      </div>

      <div className="bookings-section">
        <h2>Active Jobs</h2>
        {activeJobs.length === 0 ? (
          <p className="empty-state">No active jobs right now</p>
        ) : (
          <div className="jobs-grid">
            {activeJobs.map(job => (
              <div key={job.id} className="job-card">
                <h3>Job #{job.id}</h3>
                <div className="job-info">
                  <span className="job-label">Service Type:</span>
                  <span className="job-value">{job.serviceTypes}</span>
                </div>
                <div className="job-info">
                  <span className="job-label">Status:</span>
                  <span className={`status status-${job.status.toLowerCase()}`}>{job.status}</span>
                </div>
                <div className="job-info">
                  <span className="job-label">Customer:</span>
                  <span className="job-value">{job.customerName}</span>
                </div>
                <div className="job-info">
                  <span className="job-label">Distance:</span>
                  <span className="job-value">{job.distanceKm} km</span>
                </div>
                <div className="action-buttons">
                  {job.status === 'ASSIGNED' && (
                    <button className="btn-accept" onClick={() => handleStartJob(job.id)}>
                      Start Job
                    </button>
                  )}
                  {job.status === 'IN_PROGRESS' && (
                    <button className="btn-complete" onClick={() => handleCompleteJob(job.id)}>
                      Complete Job
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {completedJobs.length > 0 && (
        <div className="bookings-section" style={{ marginTop: '40px' }}>
          <h2>Completed Jobs</h2>
          <div className="jobs-grid">
            {completedJobs.slice(0, 3).map(job => (
              <div key={job.id} className="job-card" style={{ opacity: 0.7 }}>
                <h3>Job #{job.id}</h3>
                <div className="job-info">
                  <span className="job-label">Completed:</span>
                  <span className="job-value">{new Date(job.completionTime).toLocaleDateString()}</span>
                </div>
                <div className="job-info">
                  <span className="job-label">Earned:</span>
                  <span className="job-value">${job.actualCost}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default MechanicDashboard;
