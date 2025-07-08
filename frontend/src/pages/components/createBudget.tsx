import { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import Sidebar from './sidebar';

export default function CreateBudget() {
    const goToHome = async () => {
      navigate('/home');
    }
      const [isSidebarOpen, setSidebarOpen] = useState(false);
    const [month, setMonth] = useState('');
    const [limit, setLimit] = useState('');


    const navigate = useNavigate();

  const addTransaction = async () => {
    const userId = localStorage.getItem('userId');
      if (!userId) {
        console.error("User ID not found in localStorage");
        return;
      }
    try {
        let year = parseInt(month.substring(0,4));
        let months = parseInt(month.slice(5)); 
      await axios.post(`http://localhost:8081/budget/${userId}`, {
        month: months,
        year: year,
        limitAmount: limit,
      });
      alert('Budget Added');
      navigate('/home');
    } catch (err) {
      console.error(err);
      alert('Something went wrong');
    }
  };

  return (
    <>
    <div className="min-h-screen bg-gray-100">
          <nav className="flex justify-between items-center bg-white shadow px-6 py-4">
        <div className="flex items-center gap-2">
                   <button
            className="text-2xl"
            onClick={() => setSidebarOpen(true)}
          >
            &#9776;
          </button>
        </div>
        <h1 className="text-2xl font-bold">Create Budget</h1>
        <button className="text-1xl font-bold"
        onClick={goToHome}
        >Go to Dashboard</button>
      </nav>
    <div className="flex flex-col items-center justify-center">
        <div className="flex justify-center items-start pt-20">
      <div className="bg-white p-6 rounded shadow-md w-full">
        <h2 className="text-xl font-bold mb-4 text-center">Add Budget for the Month</h2>
        <input
        type="month"
        value={month}
        onChange={(e) => setMonth(e.target.value)}
        className="w-full mb-2 px-3 py-2 border rounded"
        />
        <input
        type="number"
        step="0.01"
        min="0"
        placeholder="Amount"
        value={limit}
        onChange={(e) => setLimit(e.target.value)}
        className="w-full mb-4 px-3 py-2 border rounded"
        />
        <button
          onClick={addTransaction}
          className="w-full bg-red-500 text-white py-2 rounded hover:bg-red-600"
        >
          Add
        </button>
      </div>
      </div>
       <Sidebar isOpen={isSidebarOpen} onClose={() => setSidebarOpen(false)} />
    </div>
    </div>
    </>
  );
}
