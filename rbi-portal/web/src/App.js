import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/home/Home';
import Connect from './pages/connect/Connect';
import TempConnect from './pages/connect/TempConnect';

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route exact path={'/'} element={<Home/>} />
        <Route exact path={'/temp'} element={<TempConnect/>} />
        <Route exact path={'/connect'} element={<Connect/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
