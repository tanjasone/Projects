import './App.css';
import "./components/Header"
import Header from './components/Header';
import Player from './components/Player';

import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useState } from 'react';

function App() {
  
  var [isDisplayingSettings, setIsDisplayingSettings] = useState(false);


  var playerElem = (<Player isDisplayingSettings={isDisplayingSettings} setIsDisplayingSettings={setIsDisplayingSettings}/>)
  return (
    <div className="App">
      <BrowserRouter>
        <Header setIsDisplayingSettings={setIsDisplayingSettings} />
        <Routes>
          <Route path = "/player" element={playerElem} />
          <Route path = "/*" element={playerElem} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
