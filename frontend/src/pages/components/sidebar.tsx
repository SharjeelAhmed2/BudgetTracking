import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

interface SidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function Sidebar({ isOpen, onClose }: SidebarProps) {
    const navigate = useNavigate();
  const handleLogout = async () => {
    try {
    
     console.log("Local Storage: ",localStorage.getItem('userId'));
       localStorage.removeItem('userId'); // store userId for future use
       
      alert('Logout Successful!');
      navigate('/login');
    } catch (err) {
      console.error(err);
    }
  };
    console.log("Local Storage:", localStorage.getItem("name"))
    const nameU = localStorage.getItem("name")

  return (
    <div
      className={`fixed top-0 left-0 h-full w-64 bg-white shadow-lg transform transition-transform duration-300 ${
        isOpen ? 'translate-x-0' : '-translate-x-full'
      }`}
    >
      <div className="p-4 border-b text-lg font-bold flex justify-between items-center">
        {nameU}
        <button onClick={onClose} className="text-xl font-bold">
          ✕
        </button>
      </div>
      <ul className="p-4 space-y-4 font-semibold">
        <li><Link bg-className="bg-white p-6" to="/createTransaction">Add Transaction</Link></li>

        <li><a href="#">Add Budget</a></li>
        <li><Link bg-className="bg-white p-6" to="/tableTransaction">View All Transactions</Link></li>
        <li>        <button className="text-sm font-semibold text-red-600 hover:underline"
        onClick={handleLogout}
        >Logout</button></li>
      </ul>
    </div>
  );
}