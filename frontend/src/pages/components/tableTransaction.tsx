import axios from 'axios';
import React, { useEffect } from 'react';

export default function TableTransaction() {
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
     // setAmount(responseData); // Assuming it's just a number or formatted string

    } catch (err) {
      console.error("Failed to fetch total spent:", err);
    }
  };
    transactionData();
  }, []);

    return(
        <>
        
        </>
    )
}