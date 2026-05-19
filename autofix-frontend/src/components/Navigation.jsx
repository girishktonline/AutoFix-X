import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Navigation.css';

function Navigation({ isAuthenticated, userRole, onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    onLogout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          AutoFix X
        </Link>
        
        <ul className="navbar-menu">
          <li><Link to="/">Home</Link></li>
          
          {!isAuthenticated ? (
            <>
              <li><Link to="/login">Login</Link></li>
              <li><Link to="/register">Register</Link></li>
            </>
          ) : (
            <>
              {userRole === 'CUSTOMER' && (
                <>
                  <li><Link to="/dashboard">Dashboard</Link></li>
                  <li><Link to="/book-service">Book Service</Link></li>
                </>
              )}
              {userRole === 'MECHANIC' && (
                <li><Link to="/jobs">My Jobs</Link></li>
              )}
              {userRole === 'ADMIN' && (
                <li><Link to="/admin">Admin Panel</Link></li>
              )}
              <li>
                <button onClick={handleLogout} className="logout-btn">
                  Logout
                </button>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
}

export default Navigation;
