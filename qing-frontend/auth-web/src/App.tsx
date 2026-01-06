import React, {type JSX} from 'react';
import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Activate from './pages/auth/Activate';
import MainLayout from './layouts/MainLayout';
import Profile from './pages/user/Profile';
import UserManagement from './pages/admin/UserManagement';
import RoleManagement from './pages/admin/RoleManagement';
import PermissionManagement from './pages/admin/PermissionManagement';
import './App.css';

const App: React.FC = () => {
    // Simple auth guard
    const isAuthenticated = () => {
        return !!localStorage.getItem('token');
    };

    const ProtectedRoute = ({children}: { children: JSX.Element }) => {
        if (!isAuthenticated()) {
            return <Navigate to="/login" replace/>;
        }
        return children;
    };

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login/>}/>
                <Route path="/register" element={<Register/>}/>
                <Route path="/activate" element={<Activate/>}/>

                <Route path="/" element={<ProtectedRoute><MainLayout/></ProtectedRoute>}>
                    <Route index element={<Navigate to="/profile" replace/>}/>
                    <Route path="profile" element={<Profile/>}/>

                    <Route path="admin">
                        <Route path="users" element={<UserManagement/>}/>
                        <Route path="roles" element={<RoleManagement/>}/>
                        <Route path="permissions" element={<PermissionManagement/>}/>
                    </Route>
                </Route>

                <Route path="*" element={<Navigate to="/login" replace/>}/>
            </Routes>
        </Router>
    );
};

export default App;
