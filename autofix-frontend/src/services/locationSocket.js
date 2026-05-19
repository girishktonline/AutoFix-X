import io from 'socket.io-client';

const BACKEND_URL = 'http://localhost:8080';

class LocationSocket {
  constructor() {
    this.socket = null;
    this.connected = false;
  }

  connect() {
    if (this.socket) return;

    this.socket = io(BACKEND_URL, {
      reconnection: true,
      reconnectionDelay: 1000,
      reconnectionDelayMax: 5000,
      reconnectionAttempts: 5,
      transports: ['websocket', 'polling']
    });

    this.socket.on('connect', () => {
      console.log('✅ WebSocket Connected:', this.socket.id);
      this.connected = true;
    });

    this.socket.on('disconnect', () => {
      console.log('❌ WebSocket Disconnected');
      this.connected = false;
    });

    this.socket.on('error', (error) => {
      console.error('❌ WebSocket Error:', error);
    });
  }

  disconnect() {
    if (this.socket) {
      this.socket.disconnect();
      this.socket = null;
      this.connected = false;
    }
  }

  // Send mechanic location update
  updateLocation(bookingId, mechanicId, latitude, longitude) {
    if (!this.connected) {
      console.warn('WebSocket not connected');
      return;
    }

    this.socket.emit('location:update', {
      bookingId,
      mechanicId,
      latitude,
      longitude,
      timestamp: Date.now()
    });
  }

  // Listen for location updates for a specific booking
  onBookingLocationUpdate(bookingId, callback) {
    if (!this.socket) return;
    this.socket.on(`location:booking:${bookingId}`, callback);
  }

  // Stop listening for location updates
  offBookingLocationUpdate(bookingId) {
    if (!this.socket) return;
    this.socket.off(`location:booking:${bookingId}`);
  }

  // Listen for booking status changes
  onBookingStatusChange(bookingId, callback) {
    if (!this.socket) return;
    this.socket.on(`booking:${bookingId}:status`, callback);
  }

  // Listen for job assignments
  onJobAssigned(callback) {
    if (!this.socket) return;
    this.socket.on('job:assigned', callback);
  }
}

export default new LocationSocket();
