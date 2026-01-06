import React, {useEffect, useState} from 'react';
import {useSearchParams, useNavigate} from 'react-router-dom';
import {Result, Button, message} from 'antd';
import {activate} from '../../api/auth';

const Activate: React.FC = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [status, setStatus] = useState<'success' | 'error' | 'info'>('info');
    const [msg, setMsg] = useState('Activating your account...');

    useEffect(() => {
        const token = searchParams.get('token');
        if (!token) {
            setStatus('error');
            setMsg('Invalid activation token');
            return;
        }

        activate(token)
            .then(() => {
                setStatus('success');
                setMsg('Account activated successfully!');
                message.success('Account activated successfully!');
            })
            .catch((err) => {
                setStatus('error');
                setMsg(err.message || 'Activation failed');
            });
    }, [searchParams]);

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            background: '#f0f2f5'
        }}>
            <Result
                status={status}
                title={status === 'success' ? 'Activation Successful' : status === 'error' ? 'Activation Failed' : 'Activating'}
                subTitle={msg}
                extra={[
                    <Button type="primary" key="login" onClick={() => navigate('/login')}>
                        Go to Login
                    </Button>,
                ]}
            />
        </div>
    );
};

export default Activate;
