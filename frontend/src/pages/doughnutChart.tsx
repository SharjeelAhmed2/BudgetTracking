import { useEffect, useState } from 'react';
import axios from 'axios';
import { Doughnut } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);




export default function DoughnutChart() {
  const [chartData, setChartData] = useState<any>(null);

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const userId = localStorage.getItem('userId');
        const response = await axios.get(`http://localhost:8081/transactions/${userId}`);
        const transactions = response.data;

        // Group by category
        const categoryTotals: Record<string, number> = {};
        transactions.forEach((t: any) => {
          const cat = t.category || 'Uncategorized';
          categoryTotals[cat] = (categoryTotals[cat] || 0) + t.amount;
        });

        const labels = Object.keys(categoryTotals);
        const data = Object.values(categoryTotals);

        setChartData({
          labels,
          datasets: [
            {
              label: 'Spending by Category',
              data,
              backgroundColor: [
               
                '#FF6384',
                '#36A2EB',
                '#FFCE56',
                '#4BC0C0',
                '#9966FF',
                '#FF9F40',
                 'CBC3E3',
              ],
              borderWidth: 1
            }
          ]
        });
      } catch (err) {
        console.error('Failed to load transactions:', err);
      }
    };

    fetchTransactions();
  }, []);

  return (
    <div style={{ width: '400px', margin: 'auto' }}>
      {chartData ? (
        console.log("charData: ", chartData.datasets),
        chartData.labels.length !== 0 ? (
        <Doughnut data={chartData}  />
        ) : (
            <p>User Has No Transactions</p>
        )
      ) : (
        <p>Loading chart...</p>
      )}
    </div>
  );
}



