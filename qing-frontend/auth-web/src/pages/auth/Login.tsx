import React from 'react';
import {Form, Input, Button, Card, message} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import {useNavigate, Link} from 'react-router-dom';
import {login} from '../../api/auth';

const Login: React.FC = () => {
    const navigate = useNavigate();

    const onFinish = async (values: any) => {
        try {
            console.info("登录开始！");
            const response = await login(values);
            console.info("登录结果：", response);
            if (response.success) {
                message.success('Login successful');
                console.debug(response);
                // Token is in result.accessToken based on new interface
                const token = response.result?.accessToken;

                if (token) {
                    localStorage.setItem('token', token);
                    // Decode token to get user info if possible, or just save username
                    // Simple base64 decode of jwt payload
                    try {
                        const payload = JSON.parse(atob(token.split('.')[1]));
                        // Assuming payload contains sub (username) and maybe id
                        const userId = payload.userId || payload.id;
                        if (userId) {
                            localStorage.setItem('userId', userId);
                        }
                        localStorage.setItem('userInfo', JSON.stringify({
                            username: payload.sub || values.username,
                            ...payload
                        }));
                    } catch (e) {
                        // Fallback
                        localStorage.setItem('userInfo', JSON.stringify({username: values.username}));
                    }

                    navigate('/profile');
                } else {
                    message.error('Login failed: No token received');
                }
            }
        } catch (error) {
            console.error(error);
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
                title={<div style={{textAlign: 'center', fontSize: 24, fontWeight: 'bold'}}>Qing Auth Login</div>}
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
                        rules={[{required: true, message: 'Please input your Username!'}]}
                    >
                        <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Username"/>
                    </Form.Item>
                    <Form.Item
                        name="password"
                        rules={[{required: true, message: 'Please input your Password!'}]}
                    >
                        <Input
                            prefix={<LockOutlined className="site-form-item-icon"/>}
                            type="password"
                            placeholder="Password"
                        />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" className="login-form-button" style={{width: '100%'}}>
                            Log in
                        </Button>
                        Or <Link to="/register">register now!</Link>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default Login;
