// Dashboard.tsx
import { useEffect, useState } from 'react';
import DoughnutChart from './doughnutChart';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Sidebar from './components/sidebar'

export default function Dashboard() {
const [isSidebarOpen, setSidebarOpen] = useState(false);

    // for TotalSpent
   const [amount, setAmount] = useState('');
   const [budget, setBudget] = useState('');
  const today = new Date();
  const month = today.getMonth() + 1; // Returns 0-11 (0 for January, 11 for December)
  const year = today.getFullYear(); // Returns 1-31
  const navigate = useNavigate();
  useEffect(() => {
    document.title = 'Dashboard';
  }, []);
  const goToHome = async () => {
      navigate('/home');
    }
  
  useEffect(() => {
    const getTotalSpent = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        console.error("User ID not found in localStorage");
        return;
      }

      const response = await axios.get(`http://localhost:8081/transactions/totalSpent/${userId}`);
      const responseData = response.data;
      console.log("User Total Spent:", responseData);
      setAmount(responseData); // Assuming it's just a number or formatted string

    } catch (err) {
      console.error("Failed to fetch total spent:", err);
    }
  };
    getTotalSpent();
  }, []);

   useEffect(() => {
    const getBudget = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        console.error("User ID not found in localStorage");
        return;
      }

      const response = await axios.get(`http://localhost:8081/budget/${userId}`, {
        params:{
            month:month,
            year: year
        }
      });
      const totalBudget = response.data.limit;
      console.log("Total Budget:", totalBudget);
      setBudget(totalBudget);
      //setAmount(responseData); // Assuming it's just a number or formatted string

    } catch (err) {
      console.error("Failed to fetch total spent:", err);
    }
  };
    getBudget();
  }, []);
  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navbar */}
      <nav className="flex justify-between items-center bg-white shadow px-6 py-4">
        <div className="flex items-center gap-2">
                   <button
            className="text-2xl"
            onClick={() => setSidebarOpen(true)}
          >
            &#9776;
          </button>
        </div>
        <h1 className="text-2xl font-bold">Dashboard</h1>
               <p className="text-1xl font-bold"
       
        >Budget Tracker</p>
      </nav>

      {/* Main Content */}
      <main className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Cards */}
        <div className="bg-white p-6 rounded-xl shadow-md">
          <h2 className="text-lg font-bold">Total Spent:</h2>
          <p className="text-2xl font-semibold text-red-500">PKR {amount}</p>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-md">
          <h2 className="text-lg font-bold">Budget:</h2>
          <p className="text-2xl font-semibold text-green-600">PKR {budget}</p>
        </div>

        {/* Chart or Additional Cards */}
        <div className="bg-white p-6 rounded-xl shadow-md col-span-1 md:col-span-2 flex justify-center">
          <DoughnutChart />
        </div>
      </main>
       {/* Sidebar */}
      <Sidebar isOpen={isSidebarOpen} onClose={() => setSidebarOpen(false)} />
    </div>
  );
}
