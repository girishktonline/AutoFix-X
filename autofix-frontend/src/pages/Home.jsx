import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

function Home() {
  return (
    <div className="home-container">
      <div className="hero-section">
        <h1>Welcome to AutoFix X</h1>
        <p>On-Demand Vehicle Repair Service at Your Doorstep</p>
        <div className="hero-buttons">
          <Link to="/register?role=CUSTOMER" className="btn-hero btn-customer">
            Book as Customer
          </Link>
          <Link to="/register?role=MECHANIC" className="btn-hero btn-mechanic">
            Work as Mechanic
          </Link>
        </div>
      </div>

      <div className="features-section">
        <h2>Why Choose AutoFix X?</h2>
        <div className="features-grid">
          <div className="feature-card">
            <h3>Quick Booking</h3>
            <p>Book service in just 2 minutes. Describe your vehicle issue and get matched with a nearby mechanic.</p>
          </div>
          <div className="feature-card">
            <h3>Real-Time Tracking</h3>
            <p>Track your mechanic's location in real-time. Know exactly when they'll arrive at your doorstep.</p>
          </div>
          <div className="feature-card">
            <h3>Verified Mechanics</h3>
            <p>All mechanics are verified professionals with proper licenses and insurance.</p>
          </div>
          <div className="feature-card">
            <h3>Transparent Pricing</h3>
            <p>Know the cost upfront. No hidden charges. Fair rates for quality service.</p>
          </div>
          <div className="feature-card">
            <h3>Safe Payments</h3>
            <p>Secure payment options including card, UPI, and cash on delivery.</p>
          </div>
          <div className="feature-card">
            <h3>Ratings & Reviews</h3>
            <p>Rate your mechanic and help other customers find the best service providers.</p>
          </div>
        </div>
      </div>

      <div className="how-it-works">
        <h2>How It Works</h2>
        <div className="steps">
          <div className="step">
            <div className="step-number">1</div>
            <h3>Register</h3>
            <p>Sign up as a customer or mechanic</p>
          </div>
          <div className="step">
            <div className="step-number">2</div>
            <h3>Request Service</h3>
            <p>Describe your vehicle issue and book a mechanic</p>
          </div>
          <div className="step">
            <div className="step-number">3</div>
            <h3>Track & Chat</h3>
            <p>Track your mechanic and communicate in real-time</p>
          </div>
          <div className="step">
            <div className="step-number">4</div>
            <h3>Get Fixed & Pay</h3>
            <p>Mechanic fixes your vehicle, you pay and rate the service</p>
          </div>
        </div>
      </div>

      <div className="cta-section">
        <h2>Ready to Get Started?</h2>
        <p>Join thousands of customers and mechanics already using AutoFix X</p>
        <div className="cta-buttons">
          <Link to="/register?role=CUSTOMER" className="btn-cta btn-primary">
            Get Service Now
          </Link>
          <Link to="/register?role=MECHANIC" className="btn-cta btn-secondary">
            Become a Partner Mechanic
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Home;
