import axios from 'axios';
import React, { useEffect, useState } from 'react';
import Sidebar from './sidebar';
import { useNavigate } from 'react-router-dom';

export default function TableTransaction() {
  const [transactions, setTransactions] = useState([]);
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate();
    const goToHome = async () => {
      navigate('/home');
    }
  useEffect(() => {
    const transactionData = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        console.error("User ID not found in localStorage");
        return;
      }
      const response = await axios.get(`http://localhost:8081/transactions/${userId}`);
      const responseData = response.data;
      console.log("Tolal Transaction", responseData);
      setTransactions(response.data);
     // setAmount(responseData); // Assuming it's just a number or formatted string

    } catch (err) {
      console.error("Failed to fetch total spent:", err);
    }
  };
    transactionData();
  }, []);

  return (
    <>
      <nav className="flex justify-between items-center bg-white shadow px-6 py-4">
        <div className="flex items-center gap-2">
                   <button
            className="text-2xl"
            onClick={() => setSidebarOpen(true)}
          >
            &#9776;
          </button>
        </div>
        <h1 className="text-2xl font-bold">All Transactions</h1>
        <button className="text-1xl font-bold"
        onClick={goToHome}
        >Go to Dashboard</button>
      </nav>
      <div className="overflow-x-auto">
        <table className="min-w-full border border-gray-300">
          <thead>
            <tr className="bg-gray-200">
              <th className="py-2 px-4 border w-1/5">Title</th>
              <th className="py-2 px-4 border w-1/5">Amount</th>
              <th className="py-2 px-4 border w-1/5">Type</th>
              <th className="py-2 px-4 border w-1/5">Category</th>
              {/* <th className="py-2 px-4 border">Timestamp</th> */}
            </tr>
          </thead>
          <tbody>
            {transactions.length > 0 ? (
              transactions.map((tx: any, index) => (
                <tr key={index} className="text-center">
                  <td className="py-2 px-4 border w-1/5">{tx.title}</td>
                  <td className="py-2 px-4 border w-1/5">{tx.amount}</td>
                  <td className="py-2 px-4 border w-1/5">{tx.type}</td>
                  <td className="py-2 px-4 border w-1/5">{tx.category}</td>
                  {/* <td className="py-2 px-4 border">{new Date(tx.timestamp).toLocaleString()}</td> */}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={5} className="py-4 text-center text-gray-500">
                  No transactions found 🥲
                </td>
              </tr>
            )}
          </tbody>
        </table>
        <Sidebar isOpen={isSidebarOpen} onClose={() => setSidebarOpen(false)} />
      </div>
    </>
  );
}