// src/pages/Register.tsx
import { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

export default function Register() {

  const [email, setemail] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      await axios.post('http://localhost:8081/users/login', {
        email,
      
      });
      alert('Login successful!');
      navigate('/home');
    } catch (err) {
      console.error(err);
      alert('Login Failed');
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md w-80">
        <h2 className="text-xl font-bold mb-4 text-center">Login</h2>
        <input
          type="text"
          placeholder="Email"
          value={email}
          onChange={(e) => setemail(e.target.value)}
          className="w-full mb-2 px-3 py-2 border rounded"
        />
        <button
          onClick={handleLogin}
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
        >
          Register
        </button>
         <div className="pt-6">
            <p>Not a User?  <Link bg-className="bg-white p-6" to="/register">Register</Link></p>
        </div>
      </div>
    </div>
  );
}
