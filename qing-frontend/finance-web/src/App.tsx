import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import Dashboard from './pages/Dashboard';
import Import from './pages/Import';
import Transactions from './pages/Transactions';
import Accounts from './pages/Accounts';
import ImportHistory from './pages/ImportHistory';
import TransactionCompare from './pages/TransactionCompare';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="import" element={<Import />} />
          <Route path="import-history" element={<ImportHistory />} />
          <Route path="transactions" element={<Transactions />} />
          <Route path="transaction-compare" element={<TransactionCompare />} />
          <Route path="accounts" element={<Accounts />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
