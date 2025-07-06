// Dashboard.tsx
import { useEffect } from 'react';
import { DoughnutChart } from './DoughnutChart';

export default function Dashboard() {
  useEffect(() => {
    document.title = 'Dashboard';
  }, []);

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Navbar */}
      <nav className="flex justify-between items-center bg-white shadow px-6 py-4">
        <div className="flex items-center gap-2">
          <button className="text-2xl">&#9776;</button>
          <span className="text-lg">navBar</span>
        </div>
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <button className="text-sm font-semibold text-red-600 hover:underline">Logout</button>
      </nav>

      {/* Main Content */}
      <main className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Cards */}
        <div className="bg-white p-6 rounded-xl shadow-md">
          <h2 className="text-lg font-bold">Total Spent:</h2>
          <p className="text-2xl font-semibold text-red-500">14,070.60</p>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-md">
          <h2 className="text-lg font-bold">Budget:</h2>
          <p className="text-2xl font-semibold text-green-600">70,000</p>
        </div>

        {/* Chart or Additional Cards */}
        <div className="bg-white p-6 rounded-xl shadow-md col-span-1 md:col-span-2 flex justify-center">
          <DoughnutChart />
        </div>
      </main>
    </div>
  );
}
