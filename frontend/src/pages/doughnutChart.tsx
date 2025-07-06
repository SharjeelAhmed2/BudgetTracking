import { Doughnut } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

const data = {
  labels: ['Food', 'Travel', 'Subscriptions', 'Shopping'],
  datasets: [
    {
      label: 'Spending Breakdown',
      data: [4000, 2500, 1500, 2070.6],
      backgroundColor: [
        '#60A5FA', // Blue
        '#F59E0B', // Amber
        '#10B981', // Green
        '#EF4444', // Red
      ],
      borderWidth: 1,
    },
  ],
};

const options = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom' as const,
    },
  },
};

export default function DoughnutChart() {
  return (
    <div className="w-full h-64 sm:h-80 md:h-96 lg:h-[400px]">
      <Doughnut data={data} options={options} />
    </div>
  );
}