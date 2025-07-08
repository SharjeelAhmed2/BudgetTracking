import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Register from './pages/register'
import Login from './pages/login';
import Home from './pages/dashboard';
import TableTransaction from './pages/components/tableTransaction';
import CreateTransaction from './pages/components/createTransaction';
import CreateBudget from './pages/components/createBudget';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/register" />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} /> 
        <Route path="/home" element={<Home />} />
        <Route path="/tableTransaction" element={<TableTransaction />} />
        <Route path="/createTransaction" element={<CreateTransaction/>} />
        <Route path="/createBudget" element={<CreateBudget/>} />
      </Routes>
    </Router>
  );
}

export default App;