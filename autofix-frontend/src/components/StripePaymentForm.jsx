import React, { useState } from 'react';
import axios from 'axios';
import '../styles/Payment.css';

const StripePaymentForm = ({ bookingId, amount, onSuccess, onError }) => {
  const [cardNumber, setCardNumber] = useState('');
  const [expiry, setExpiry] = useState('');
  const [cvc, setCvc] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleCardNumberChange = (e) => {
    let value = e.target.value.replace(/\s/g, '');
    value = value.replace(/(\d{4})/g, '$1 ').trim();
    setCardNumber(value);
  };

  const handleExpiryChange = (e) => {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length >= 2) {
      value = value.slice(0, 2) + '/' + value.slice(2, 4);
    }
    setExpiry(value);
  };

  const handlePayment = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // Validate inputs
      if (!cardNumber.replace(/\s/g, '').match(/^\d{16}$/)) {
        throw new Error('Invalid card number');
      }
      if (!expiry.match(/^\d{2}\/\d{2}$/)) {
        throw new Error('Invalid expiry date');
      }
      if (!cvc.match(/^\d{3,4}$/)) {
        throw new Error('Invalid CVC');
      }

      // In production, use Stripe.js to tokenize the card
      // For demo, create a test token
      const stripeToken = `tok_visa_${bookingId}_${Date.now()}`;

      // Send payment to backend
      const response = await axios.post('/payments/stripe/process', null, {
        params: {
          bookingId,
          amount,
          stripeToken
        }
      });

      setSuccess(`✅ Payment Successful! Amount: ₹${amount}`);
      onSuccess && onSuccess(response.data);
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.message || 'Payment failed';
      setError(`❌ ${errorMsg}`);
      onError && onError(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="payment-form-container">
      <h2>💳 Complete Payment</h2>
      <p className="payment-amount">Amount to Pay: <strong>₹{amount}</strong></p>

      <form onSubmit={handlePayment}>
        <div className="form-group">
          <label>Card Number</label>
          <input
            type="text"
            placeholder="1234 5678 9012 3456"
            value={cardNumber}
            onChange={handleCardNumberChange}
            maxLength="19"
            disabled={loading}
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Expiry Date</label>
            <input
              type="text"
              placeholder="MM/YY"
              value={expiry}
              onChange={handleExpiryChange}
              maxLength="5"
              disabled={loading}
              required
            />
          </div>
          <div className="form-group">
            <label>CVC</label>
            <input
              type="text"
              placeholder="123"
              value={cvc}
              onChange={(e) => setCvc(e.target.value.replace(/\D/g, '').slice(0, 4))}
              maxLength="4"
              disabled={loading}
              required
            />
          </div>
        </div>

        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        <button 
          type="submit" 
          disabled={loading}
          className="btn-pay"
        >
          {loading ? '⏳ Processing...' : `Pay ₹${amount}`}
        </button>
      </form>

      <p className="test-card-info">
        💡 Test Card: 4242 4242 4242 4242 | Any future date | Any CVC
      </p>
    </div>
  );
};

export default StripePaymentForm;
