# AutoFix X - Frontend (React 18.2 + Vite 5.0)

Professional React frontend for the AutoFix X vehicle service platform with real-time tracking, multi-role dashboards, and responsive design.

## 🚀 Quick Start

### Prerequisites
- Node.js 18+ with npm
- React 18.2
- Vite 5.0

### Setup

1. **Install Dependencies**
   ```bash
   cd autofix-frontend
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm run dev
   ```
   Frontend will start on `http://localhost:5173`

3. **Build for Production**
   ```bash
   npm run build
   ```

## 📁 Project Structure

```
autofix-frontend/
├── src/
│   ├── App.jsx                      # Main app router
│   ├── App.css                      # Global styles
│   ├── main.jsx                     # React entry point
│   ├── index.css                    # Base CSS
│   ├── components/
│   │   ├── Navigation.jsx           # Header navigation
│   │   └── Navigation.css
│   ├── pages/
│   │   ├── Home.jsx                 # Landing page
│   │   ├── Home.css
│   │   ├── Login.jsx                # Login page
│   │   ├── Register.jsx             # Registration page
│   │   ├── Auth.css                 # Auth page styles
│   │   ├── CustomerDashboard.jsx    # Customer main dashboard
│   │   ├── MechanicDashboard.jsx    # Mechanic main dashboard
│   │   ├── AdminDashboard.jsx       # Admin panel
│   │   ├── BookingPage.jsx          # Service booking form
│   │   └── Dashboard.css            # Dashboard styles
│   ├── services/
│   │   └── api.js                   # Axios API client & interceptors
│   ├── index.html
├── public/
├── package.json                     # NPM dependencies
├── vite.config.js                   # Vite configuration
└── README.md
```

## 🎯 Key Features

### User Roles
1. **Customer** - Book services, track mechanics, manage bookings
2. **Mechanic** - Accept jobs, update location, track earnings
3. **Admin** - Platform analytics, mechanic management, revenue tracking

### Pages

#### Public Pages
- **Home** - Landing page with features & CTA
- **Login** - User authentication
- **Register** - New user registration (Customer/Mechanic/Admin)

#### Customer Pages
- **Dashboard** - View bookings, stats, booking history
- **Book Service** - Create new service request with vehicle & symptoms

#### Mechanic Pages
- **Jobs Dashboard** - View assigned jobs, earnings, completed history
- **Real-time Location Tracking** - GPS updates for customers

#### Admin Pages
- **Admin Dashboard** - Platform metrics, mechanic management, revenue analytics

## 🔌 API Integration

All API calls go through `src/services/api.js` with:
- Base URL: `http://localhost:8080/api`
- JWT token attached to all requests
- Auto-logout on 401 errors
- Error handling

### API Methods

```javascript
// Authentication
authAPI.register(data)
authAPI.login(data)

// Bookings
bookingAPI.createBooking(data)
bookingAPI.getMyBookings()
bookingAPI.getBookingDetails(id)

// Mechanic Operations
mechanicAPI.getAssignedJobs()
mechanicAPI.startJob(id)
mechanicAPI.completeJob(id, data)
mechanicAPI.updateLocation(data)

// Admin
adminAPI.getDashboard()
adminAPI.getMechanics()
adminAPI.getAnalytics()

// Payment
paymentAPI.processPayment(data)
paymentAPI.getPaymentStatus(bookingId)
```

## 🎨 Styling

- **Color Scheme**: Purple gradient (#667eea to #764ba2)
- **Components**: Cards, buttons, forms with consistent styling
- **Responsive**: Mobile-first design with breakpoints at 768px
- **No Emojis**: Professional enterprise design

## 🔐 Authentication Flow

1. User registers → Backend creates user + generates JWT
2. JWT stored in localStorage
3. All API requests include `Authorization: Bearer <token>`
4. Server validates token & returns user data
5. Invalid token → Auto-logout & redirect to login

## 🌐 CORS Configuration

Frontend connects to backend with:
- **Origins**: `http://localhost:5173`, `http://localhost:3000`
- **Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Credentials**: Allowed
- **Max Age**: 3600 seconds

## 📦 Dependencies

- **react** ^18.2.0 - UI library
- **react-dom** ^18.2.0 - DOM rendering
- **react-router-dom** ^6.20.0 - Client-side routing
- **axios** ^1.6.2 - HTTP client
- **chart.js** ^4.4.1 - Data visualization
- **react-chartjs-2** ^5.2.0 - Chart.js React wrapper
- **socket.io-client** ^4.7.2 - Real-time communication
- **google-map-react** ^2.2.0 - Google Maps integration
- **@react-google-maps/api** ^2.20.2 - Advanced Maps API

### Dev Dependencies

- **@vitejs/plugin-react** ^4.2.1 - React plugin for Vite
- **vite** ^5.0.8 - Build tool

## 🚀 Running the Application

### Development Mode
```bash
npm run dev
```
Starts Vite dev server with hot module replacement

### Production Build
```bash
npm run build
```
Generates optimized build in `dist/` folder

### Preview Production Build
```bash
npm run preview
```
Serves the production build locally

## 📱 Responsive Design

- **Desktop** (1200px+): Multi-column layouts
- **Tablet** (768px-1199px): 2-column or stacked layouts
- **Mobile** (<768px): Single column, full-width elements

## 🔄 Real-Time Features (Coming Soon)

- WebSocket connection for live mechanic location
- Real-time booking status updates
- Live chat between customer and mechanic
- Notification system

## 🗺️ Maps Integration (Coming Soon)

- Google Maps for mechanic location tracking
- Customer current location
- Distance calculation
- ETA updates

## 📊 Analytics Dashboard (In Progress)

- Revenue charts
- Booking completion rates
- Mechanic performance metrics
- Customer satisfaction trends

## 🛠️ Troubleshooting

### CORS Errors
- Ensure backend is running on `http://localhost:8080`
- Check CORS configuration in backend `application.properties`

### 401 Unauthorized
- Clear localStorage: `localStorage.clear()`
- Login again with valid credentials

### White Screen
- Check browser console for errors
- Verify backend API is running
- Check network tab for failed requests

## 📝 Environment Variables

Create `.env` file in root (optional):
```
VITE_API_URL=http://localhost:8080/api
```

## 🎓 Development Tips

1. **State Management**: Currently using React hooks (useState, useEffect)
2. **API Calls**: All in `services/api.js` for centralized management
3. **Routing**: React Router v6 with nested routes per role
4. **Styling**: CSS modules in each component's CSS file
5. **Testing**: Jest + React Testing Library (to be implemented)

## 🚀 Next Steps

1. Implement real-time WebSocket communication
2. Add Google Maps integration for tracking
3. Implement payment processing UI
4. Add push notifications
5. Create admin analytics charts
6. Add unit & integration tests
7. Deploy on AWS/Azure

## 📱 Mobile App (Future)

- React Native version planned
- Same API as web frontend
- Native location services
- Push notifications

## 👨‍💻 Contributing

Guidelines for adding new features:
1. Create feature branch
2. Add page in `src/pages/`
3. Add API methods in `src/services/api.js`
4. Update navigation if needed
5. Add proper error handling
6. Test with backend API

## 📞 Support

Questions? Check the [backend README](../autofix-backend/README.md) for API documentation.

