import React, {useState} from 'react';
import {Form, Input, Button, Card, message} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import {useNavigate, Link} from 'react-router-dom';
import {login} from '../../api/auth';

const Login: React.FC = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const onFinish = async (values: { username: string; password: string }) => {
        setLoading(true);
        try {
            const response = await login(values);
            if (response.success && response.result) {
                const {token, userId, username, avatar} = response.result;

                localStorage.setItem('token', token);
                localStorage.setItem('userId', String(userId));

                const userInfo = {
                    id: userId,
                    username: username,
                    nickname: username,
                    avatar: avatar || '',
                };
                localStorage.setItem('userInfo', JSON.stringify(userInfo));

                message.success('登录成功');
                navigate('/profile');
            }
        } catch (error: any) {
            console.error('登录失败:', error);
            const errorMsg = error?.response?.data?.message || error?.message || '登录失败，请检查用户名和密码';
            message.error(errorMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            background: '#f0f2f5',
            backgroundImage: 'url(https://gw.alipayobjects.com/zos/rmsportal/TVYTbAXWheQpRcWDaDMu.svg)',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'center 110%',
            backgroundSize: '100%'
        }}>
            <Card
                title={<div style={{textAlign: 'center', fontSize: 24, fontWeight: 'bold'}}>Qing Auth 登录</div>}
                style={{width: 400, boxShadow: '0 4px 12px rgba(0,0,0,0.1)'}}
            >
                <Form
                    name="normal_login"
                    className="login-form"
                    initialValues={{remember: true}}
                    onFinish={onFinish}
                >
                    <Form.Item
                        name="username"
                        rules={[{required: true, message: '请输入用户名!'}]}
                    >
                        <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="用户名"/>
                    </Form.Item>
                    <Form.Item
                        name="password"
                        rules={[{required: true, message: '请输入密码!'}]}
                    >
                        <Input
                            prefix={<LockOutlined className="site-form-item-icon"/>}
                            type="password"
                            placeholder="密码"
                        />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" className="login-form-button" style={{width: '100%'}}
                                loading={loading}>
                            登录
                        </Button>
                        或 <Link to="/register">立即注册!</Link>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default Login;
