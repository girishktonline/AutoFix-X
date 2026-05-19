import React, { useState } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import { authAPI } from '../services/api';
import './Auth.css';

function Register({ onLogin }) {
  const [searchParams] = useSearchParams();
  const [role, setRole] = useState(searchParams.get('role') || 'CUSTOMER');
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    phone: '',
    name: '',
    address: '',
    city: '',
    latitude: '',
    longitude: '',
    experienceYears: '',
    licenseNumber: '',
    specializations: 'PUNCTURE,WASH',
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
      const payload = { ...formData, role };
      const response = await authAPI.register(payload);
      if (response.data.token) {
        onLogin(response.data.token, response.data.role);
        navigate('/dashboard');
      } else {
        setError(response.data.message || 'Registration failed');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Register for AutoFix X</h2>
        
        <div className="form-group role-buttons">
          <button
            type="button"
            className={`role-button ${role === 'CUSTOMER' ? 'active' : ''}`}
            onClick={() => setRole('CUSTOMER')}
          >
            Customer
          </button>
          <button
            type="button"
            className={`role-button ${role === 'MECHANIC' ? 'active' : ''}`}
            onClick={() => setRole('MECHANIC')}
          >
            Mechanic
          </button>
        </div>

        {error && <div className="error">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Phone</label>
            <input
              type="tel"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          {role === 'CUSTOMER' && (
            <>
              <div className="form-group">
                <label>Address</label>
                <input
                  type="text"
                  name="address"
                  value={formData.address}
                  onChange={handleChange}
                />
              </div>
              <div className="form-group">
                <label>City</label>
                <input
                  type="text"
                  name="city"
                  value={formData.city}
                  onChange={handleChange}
                />
              </div>
            </>
          )}

          {role === 'MECHANIC' && (
            <>
              <div className="form-group">
                <label>Experience (Years)</label>
                <input
                  type="number"
                  name="experienceYears"
                  value={formData.experienceYears}
                  onChange={handleChange}
                />
              </div>
              <div className="form-group">
                <label>License Number</label>
                <input
                  type="text"
                  name="licenseNumber"
                  value={formData.licenseNumber}
                  onChange={handleChange}
                />
              </div>
            </>
          )}

          <button type="submit" className="btn-submit" disabled={loading}>
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>

        <p className="auth-link">
          Already have an account? <Link to="/login">Login here</Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
