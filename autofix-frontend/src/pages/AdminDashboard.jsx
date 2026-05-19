import React, { useState, useEffect } from 'react';
import { adminAPI } from '../services/api';
import './Dashboard.css';

function AdminDashboard() {
  const [dashboard, setDashboard] = useState(null);
  const [mechanics, setMechanics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchDashboard();
    fetchMechanics();
  }, []);

  const fetchDashboard = async () => {
    try {
      const response = await adminAPI.getDashboard();
      setDashboard(response.data);
    } catch (err) {
      setError('Failed to load dashboard');
    } finally {
      setLoading(false);
    }
  };

  const fetchMechanics = async () => {
    try {
      const response = await adminAPI.getMechanics();
      setMechanics(response.data);
    } catch (err) {
      console.error('Failed to load mechanics');
    }
  };

  if (loading) {
    return <div className="loading"><div className="spinner"></div></div>;
  }

  const dashboardData = dashboard || {
    totalBookings: 0,
    completedBookings: 0,
    totalRevenue: 0,
    activeMechanics: 0,
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Admin Dashboard</h1>
        <p>Platform management and analytics</p>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Total Bookings</div>
          <div className="stat-value">{dashboardData.totalBookings}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Completed</div>
          <div className="stat-value">{dashboardData.completedBookings}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Revenue</div>
          <div className="stat-value">${dashboardData.totalRevenue?.toFixed(2) || '0.00'}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Active Mechanics</div>
          <div className="stat-value">{dashboardData.activeMechanics}</div>
        </div>
      </div>

      <div className="bookings-section">
        <h2>Mechanics Management</h2>
        {mechanics.length === 0 ? (
          <p className="empty-state">No mechanics registered yet</p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid #ddd' }}>
                  <th style={{ padding: '12px', textAlign: 'left', color: '#333' }}>Name</th>
                  <th style={{ padding: '12px', textAlign: 'left', color: '#333' }}>Email</th>
                  <th style={{ padding: '12px', textAlign: 'left', color: '#333' }}>Status</th>
                  <th style={{ padding: '12px', textAlign: 'left', color: '#333' }}>Jobs Completed</th>
                  <th style={{ padding: '12px', textAlign: 'left', color: '#333' }}>Rating</th>
                </tr>
              </thead>
              <tbody>
                {mechanics.map(mechanic => (
                  <tr key={mechanic.id} style={{ borderBottom: '1px solid #eee' }}>
                    <td style={{ padding: '12px' }}>{mechanic.name}</td>
                    <td style={{ padding: '12px' }}>{mechanic.email}</td>
                    <td style={{ padding: '12px' }}>
                      <span className={`status status-${mechanic.currentStatus?.toLowerCase() || 'offline'}`}>
                        {mechanic.currentStatus || 'OFFLINE'}
                      </span>
                    </td>
                    <td style={{ padding: '12px' }}>{mechanic.totalCompleted || 0}</td>
                    <td style={{ padding: '12px' }}>{mechanic.averageRating?.toFixed(1) || 'N/A'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="bookings-section" style={{ marginTop: '40px' }}>
        <h2>Platform Statistics</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px' }}>
          <div style={{ padding: '20px', background: '#f9f9f9', borderRadius: '4px' }}>
            <div style={{ color: '#999', fontSize: '12px', textTransform: 'uppercase' }}>Commission Revenue</div>
            <div style={{ color: '#667eea', fontSize: '24px', fontWeight: 'bold', marginTop: '8px' }}>
              ${(dashboardData.totalRevenue * 0.3)?.toFixed(2) || '0.00'}
            </div>
          </div>
          <div style={{ padding: '20px', background: '#f9f9f9', borderRadius: '4px' }}>
            <div style={{ color: '#999', fontSize: '12px', textTransform: 'uppercase' }}>Success Rate</div>
            <div style={{ color: '#667eea', fontSize: '24px', fontWeight: 'bold', marginTop: '8px' }}>
              {dashboardData.totalBookings > 0 
                ? ((dashboardData.completedBookings / dashboardData.totalBookings) * 100).toFixed(1)
                : 0}%
            </div>
          </div>
          <div style={{ padding: '20px', background: '#f9f9f9', borderRadius: '4px' }}>
            <div style={{ color: '#999', fontSize: '12px', textTransform: 'uppercase' }}>Avg Job Value</div>
            <div style={{ color: '#667eea', fontSize: '24px', fontWeight: 'bold', marginTop: '8px' }}>
              ${dashboardData.totalBookings > 0 
                ? (dashboardData.totalRevenue / dashboardData.totalBookings).toFixed(2)
                : '0.00'}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;
