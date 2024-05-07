import './App.css';
import "./components/Header"
import Header from './components/Header';
import Player from './components/Player';
import Settings from './components/Settings';

import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useEffect } from 'react';
import { baseUrl } from '../environment';

function App() {
  useEffect(() => {
    fetch(baseUrl + "/settings").then(async res => {
      var data = await res.json();
      console.log(data);
      
    }, rej => {
      console.error(rej);
    })
  })
  return (
    <div className="App">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path = "/player" element={<Player />} />
          <Route path = "/*" element={<Player />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
