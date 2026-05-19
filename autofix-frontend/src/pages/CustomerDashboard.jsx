import React, { useState, useEffect } from 'react';
import { bookingAPI } from '../services/api';
import './Dashboard.css';

function CustomerDashboard() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      const response = await bookingAPI.getMyBookings();
      setBookings(response.data);
    } catch (err) {
      setError('Failed to load bookings');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading"><div className="spinner"></div></div>;
  }

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Customer Dashboard</h1>
        <p>Manage your service bookings</p>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-label">Active Bookings</div>
          <div className="stat-value">{bookings.filter(b => b.status === 'PENDING' || b.status === 'IN_PROGRESS').length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Completed</div>
          <div className="stat-value">{bookings.filter(b => b.status === 'COMPLETED').length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Spent</div>
          <div className="stat-value">$0</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Average Rating</div>
          <div className="stat-value">5.0</div>
        </div>
      </div>

      <div className="bookings-section">
        <h2>Your Bookings</h2>
        {bookings.length === 0 ? (
          <p className="empty-state">No bookings yet. <a href="/book-service">Book a service now!</a></p>
        ) : (
          <div className="bookings-list">
            {bookings.map(booking => (
              <div key={booking.id} className="booking-card">
                <div className="booking-header">
                  <h3>Booking #{booking.id}</h3>
                  <span className={`status status-${booking.status.toLowerCase()}`}>{booking.status}</span>
                </div>
                <p><strong>Service Types:</strong> {booking.serviceTypes}</p>
                <p><strong>Booking Time:</strong> {new Date(booking.bookingTime).toLocaleString()}</p>
                <p><strong>Estimated Cost:</strong> ${booking.estimatedCost}</p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default CustomerDashboard;
