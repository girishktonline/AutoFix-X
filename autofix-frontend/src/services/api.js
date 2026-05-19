import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle response errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
  health: () => api.get('/auth/health'),
};

export const bookingAPI = {
  createBooking: (data) => api.post('/bookings/create', data),
  getMyBookings: () => api.get('/bookings/my-bookings'),
  getBookingDetails: (id) => api.get(`/bookings/${id}`),
  rateBooking: (id, data) => api.put(`/bookings/${id}/rate`, data),
};

export const mechanicAPI = {
  getAssignedJobs: () => api.get('/mechanics/jobs'),
  startJob: (id) => api.put(`/mechanics/jobs/${id}/start`),
  completeJob: (id, data) => api.put(`/mechanics/jobs/${id}/complete`, data),
  updateLocation: (data) => api.put('/mechanics/location', data),
  getEarnings: () => api.get('/mechanics/earnings'),
};

export const adminAPI = {
  getDashboard: () => api.get('/admin/dashboard'),
  getMechanics: () => api.get('/admin/mechanics'),
  getAnalytics: () => api.get('/admin/analytics'),
};

export const paymentAPI = {
  processPayment: (data) => api.post('/payments/process', data),
  getPaymentStatus: (bookingId) => api.get(`/payments/${bookingId}`),
};

export default api;
