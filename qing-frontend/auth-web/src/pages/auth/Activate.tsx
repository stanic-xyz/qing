import React, {useEffect, useState} from 'react';
import {useSearchParams, useNavigate} from 'react-router-dom';
import {Result, Button} from 'antd';
import {activate} from '../../api/auth';

const Activate: React.FC = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [status, setStatus] = useState<'success' | 'error' | 'info'>('info');
    const [msg, setMsg] = useState('正在激活您的账户...');

    useEffect(() => {
        const token = searchParams.get('token');
        if (!token) {
            setStatus('error');
            setMsg('无效的激活令牌');
            return;
        }

        const doActivate = async () => {
            try {
                await activate(token);
                setStatus('success');
                setMsg('账户激活成功！');
            } catch (err: any) {
                setStatus('error');
                setMsg(err?.response?.data?.message || err?.message || '激活失败');
            }
        };

        doActivate();
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
                title={status === 'success' ? '激活成功' : status === 'error' ? '激活失败' : '激活中'}
                subTitle={msg}
                extra={[
                    <Button type="primary" key="login" onClick={() => navigate('/login')}>
                        去登录
                    </Button>,
                ]}
            />
        </div>
    );
};

export default Activate;
