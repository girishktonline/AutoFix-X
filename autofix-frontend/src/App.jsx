import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navigation from './components/Navigation';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import CustomerDashboard from './pages/CustomerDashboard';
import MechanicDashboard from './pages/MechanicDashboard';
import AdminDashboard from './pages/AdminDashboard';
import BookingPage from './pages/BookingPage';
import './App.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));
  const [userRole, setUserRole] = useState(localStorage.getItem('userRole'));

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    const role = localStorage.getItem('userRole');
    setIsAuthenticated(!!token);
    setUserRole(role);
  }, []);

  const handleLogin = (token, role) => {
    localStorage.setItem('authToken', token);
    localStorage.setItem('userRole', role);
    setIsAuthenticated(true);
    setUserRole(role);
  };

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    setIsAuthenticated(false);
    setUserRole(null);
  };

  return (
    <Router>
      <Navigation isAuthenticated={isAuthenticated} userRole={userRole} onLogout={handleLogout} />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/register" element={<Register onLogin={handleLogin} />} />
        
        {isAuthenticated && userRole === 'CUSTOMER' && (
          <>
            <Route path="/dashboard" element={<CustomerDashboard />} />
            <Route path="/book-service" element={<BookingPage />} />
          </>
        )}
        
        {isAuthenticated && userRole === 'MECHANIC' && (
          <Route path="/jobs" element={<MechanicDashboard />} />
        )}
        
        {isAuthenticated && userRole === 'ADMIN' && (
          <Route path="/admin" element={<AdminDashboard />} />
        )}
        
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;
