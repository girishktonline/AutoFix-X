import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { bookingAPI } from '../services/api';
import './Auth.css';

function BookingPage() {
  const [formData, setFormData] = useState({
    vehicleId: '',
    serviceTypes: 'PUNCTURE',
    symptoms: '',
    customerLatitude: '0',
    customerLongitude: '0',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await bookingAPI.createBooking(formData);
      if (response.data.bookingId) {
        navigate('/dashboard');
      } else {
        setError(response.data.message || 'Booking failed');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card" style={{ maxWidth: '600px' }}>
        <h2>Book a Service</h2>
        {error && <div className="error">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Service Type</label>
            <select
              name="serviceTypes"
              value={formData.serviceTypes}
              onChange={handleChange}
              required
            >
              <option value="PUNCTURE">Puncture Repair</option>
              <option value="WASH">Car Wash</option>
              <option value="BRAKE">Brake Service</option>
              <option value="BATTERY">Battery Replacement</option>
              <option value="AC">AC Service</option>
              <option value="ENGINE">Engine Service</option>
            </select>
          </div>

          <div className="form-group">
            <label>Describe Your Issue</label>
            <textarea
              name="symptoms"
              value={formData.symptoms}
              onChange={handleChange}
              placeholder="Describe the problem with your vehicle..."
              rows="4"
            ></textarea>
          </div>

          <div className="form-group">
            <label>Vehicle ID</label>
            <input
              type="text"
              name="vehicleId"
              value={formData.vehicleId}
              onChange={handleChange}
              placeholder="Enter your vehicle ID"
              required
            />
          </div>

          <button type="submit" className="btn-submit" disabled={loading}>
            {loading ? 'Booking...' : 'Book Service'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default BookingPage;
