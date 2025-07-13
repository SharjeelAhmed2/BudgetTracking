import { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import Sidebar from './sidebar';

export default function CreateTransaction() {
    const goToHome = async () => {
      navigate('/home');
    }
      const [isSidebarOpen, setSidebarOpen] = useState(false);
    const [title, setTitle] = useState('');
    const [amount, setAmount] = useState('');
    const [type, setType] = useState('');
    const [category, setCategory] = useState('');

    const navigate = useNavigate();

  const addTransaction = async () => {
    const userId = localStorage.getItem('userId');
      if (!userId) {
        console.error("User ID not found in localStorage");
        return;
      }
    try {
      await axios.post(`http://localhost:8081/transactions/${userId}`, {
        title,
        amount,
        type,
        category,
      });
      alert('Transaction Added');
      navigate('/tableTransaction');
    } catch (err) {
      console.error(err);
      alert('Failed to add Transaction');
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
        <h1 className="text-2xl font-bold">Create Transaction</h1>
        <button className="text-1xl font-bold"
        onClick={goToHome}
        >Go to Dashboard</button>
      </nav>
    <div className="flex flex-col items-center justify-center">
        <div className="flex justify-center items-start pt-20">
      <div className="bg-white p-6 rounded shadow-md w-full">
        <h2 className="text-xl font-bold mb-4 text-center">Add Transaction</h2>
        <input
          type="title"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="w-full mb-2 px-3 py-2 border rounded"
        />
        <input
        type="number"
        step="0.01"
        min="0"
        placeholder="Amount"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        className="w-full mb-4 px-3 py-2 border rounded"
        />
        <select
        value={type}
        onChange={(e) => setType(e.target.value)}
        className="w-full mb-4 px-3 py-2 border rounded"
        >
        <option value="">Select a Type</option>
        <option value="INCOME">INCOME</option>
        <option value="EXPENSE">EXPENSE</option>
        </select>
       <select
        value={category}
        onChange={(e) => setCategory(e.target.value)}
        className="w-full mb-4 px-3 py-2 border rounded"
        >
        <option value="">Select a Category</option>
        <option value="FOOD">FOOD</option>
        <option value="TRAVEL">TRAVEL</option>
        <option value="SUBSCRIPTIONS">SUBSCRIPTIONS</option>
        <option value="RETAIL">RETAIL</option>
        <option value="UTILITIES">UTILITIES</option>
        <option value="ENTERTAINMENT">ENTERTAINMENT</option>
        <option value="OTHER">OTHER</option>
        </select>
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
