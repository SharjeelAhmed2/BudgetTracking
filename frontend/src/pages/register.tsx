// src/pages/Register.tsx
import { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

export default function Register() {

  const [email, setemail] = useState('');
  const [name, setname] = useState('');
  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      await axios.post('http://localhost:8081/users/signup', {
        email,
        name,
      });
      alert('Registration successful!');
      navigate('/home');
    } catch (err) {
      console.error(err);
      alert('Registration failed');
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-6 rounded shadow-md w-80">
        <h2 className="text-xl font-bold mb-4 text-center">Register</h2>
        <input
          type="text"
          placeholder="Email"
          value={email}
          onChange={(e) => setemail(e.target.value)}
          className="w-full mb-2 px-3 py-2 border rounded"
        />
        <input
          type="name"
          placeholder="name"
          value={name}
          onChange={(e) => setname(e.target.value)}
          className="w-full mb-4 px-3 py-2 border rounded"
        />
        <button
          onClick={handleRegister}
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
        >
          Register
        </button>
        <div className="pt-6">
            <p>Already a User?  <Link bg-className="bg-white p-6" to="/login">Login</Link></p>
        </div>
      </div>
    </div>
  );
}
