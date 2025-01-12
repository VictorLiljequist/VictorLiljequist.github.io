// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: "mern-project-23eec.firebaseapp.com",
  projectId: "mern-project-23eec",
  storageBucket: "mern-project-23eec.appspot.com",
  messagingSenderId: "256980225488",
  appId: "1:256980225488:web:7a2a7bbdc36c0ac8347dd5"
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);