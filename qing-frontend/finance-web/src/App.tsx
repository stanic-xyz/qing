import {BrowserRouter, Routes, Route} from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import Dashboard from './pages/Dashboard';
import Import from './pages/Import';
import Transactions from './pages/Transactions';
import Accounts from './pages/Accounts';
import ChannelAccountManager from './pages/ChannelAccountManager';
import FundsFlowChart from './pages/FundsFlowChart';
import Categories from './pages/Categories';
import Matchers from './pages/Matchers';
import Counterparties from './pages/Counterparties';
import Channels from "./pages/Channels";
import Tags from './pages/Tags';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<MainLayout/>}>
                    <Route index element={<Dashboard/>}/>
                    <Route path="import" element={<Import/>}/>
                    <Route path="transactions" element={<Transactions/>}/>
                    <Route path="accounts" element={<Accounts/>}/>
                    <Route path="channels" element={<Channels/>}/>
                    <Route path="channel-accounts" element={<ChannelAccountManager/>}/>
                    <Route path="funds-flow" element={<FundsFlowChart/>}/>
                    <Route path="categories" element={<Categories/>}/>
                    <Route path="counterparties" element={<Counterparties/>}/>
                    <Route path="matchers" element={<Matchers/>}/>
                    <Route path="tags" element={<Tags/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
