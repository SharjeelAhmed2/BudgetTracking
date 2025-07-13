import axios from 'axios';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';


export default function GenerateSummary() {
  
  const[summary, getSummary] = useState('');   
  const today = new Date();
  const month = today.getMonth() + 1; // Returns 0-11 (0 for January, 11 for December)
  const year = today.getFullYear(); // Returns 1-31

   useEffect(() => {
    const getBudget = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        console.error("User ID not found in localStorage");
        return;
      }

      const response = await axios.get(`http://localhost:8081/summary/${userId}`, {
        params:{
            month:month,
            year: year
        }
      });
      const summaryResult = response.data.summary;
      console.log("Summary:", summaryResult);
      getSummary(summaryResult);
      //setAmount(responseData); // Assuming it's just a number or formatted string

    } catch (err) {
      console.error("Failed to fetch total spent:", err);
    }
  };
    getBudget();
  }, []);
  return (
    <>
     <div>
  <div className="relative z-10" aria-labelledby="dialog-title" role="dialog" aria-modal="true">
    <div className="fixed inset-0 bg-gray-500/75 transition-opacity" aria-hidden="true"></div>

    <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
      <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">

        <div className="relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg">
          <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div className="sm:flex sm:items-start">
              <div className="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                <h3 className="text-base font-semibold text-gray-900" id="dialog-title">Transaction Summary</h3>
                <div className="mt-2">
                  <p className="text-sm text-gray-500">{summary}</p>
                </div>
              </div>
            </div>
          </div>
          <div className="bg-gray-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6">
            <button type="button" className="inline-flex w-full justify-center rounded-md bg-green-600 px-3 py-2 text-sm font-semibold text-white shadow-xs hover:bg-green-500 sm:ml-3 sm:w-auto"><Link to="/home">Back to Dashboard</Link></button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
    </>
  );
}

