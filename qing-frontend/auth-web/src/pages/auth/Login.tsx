import React, {useState} from 'react';
import {Form, Input, Button, Card, message} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import {useNavigate, Link} from 'react-router-dom';
import {getCurrentUser, login} from '../../api/auth';

const Login: React.FC = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const onFinish = async (values: { username: string; password: string }) => {
        setLoading(true);
        try {
            console.log('开始登录...', values);
            const response = await login(values);
            console.log('登录响应:', response);

            let result = response;
            if (response.data) {
                result = response.data;
            }

            console.log('处理后的响应:', result);

            if (result.success || result.code === 200) {
                const data = result.result || result;
                console.log('登录数据:', data);

                const token = data.token || data.accessToken;

                if (!token) {
                    console.error('未找到 token:', data);
                    message.error('登录失败：未收到认证令牌');
                    return;
                }

                localStorage.setItem('token', token);

                try {
                    const userRes = await getCurrentUser();
                    console.log('获取当前用户信息:', userRes);

                    if (userRes?.success && userRes.result) {
                        const userData = userRes.result;
                        const userId = userData.id || userData.userId;

                        localStorage.setItem('userId', String(userId));
                        localStorage.setItem('userInfo', JSON.stringify(userData));

                        console.log('用户信息已存储:', userId);
                    } else {
                        console.warn('获取用户信息失败，使用默认信息');
                        const userId = data.userId || data.id;
                        const username = data.username || data.userName;
                        const userInfo = {
                            id: userId,
                            username: username,
                            nickname: username,
                            avatar: data.avatar || '',
                        };
                        localStorage.setItem('userId', String(userId || ''));
                        localStorage.setItem('userInfo', JSON.stringify(userInfo));
                    }
                } catch (userError) {
                    console.error('获取用户信息失败:', userError);
                    const userId = data.userId || data.id;
                    const username = data.username || data.userName;
                    const userInfo = {
                        id: userId,
                        username: username,
                        nickname: username,
                        avatar: data.avatar || '',
                    };
                    localStorage.setItem('userId', String(userId || ''));
                    localStorage.setItem('userInfo', JSON.stringify(userInfo));
                }

                console.log('登录成功，准备跳转...');
                message.success('登录成功');
                navigate('/profile');
            } else {
                console.error('登录失败:', result.message);
                message.error(result.message || '登录失败，请检查用户名和密码');
            }
        } catch (error: any) {
            console.error('登录异常:', error);
            const errorMsg = error?.response?.data?.message ||
                           error?.message ||
                           error?.data?.message ||
                           '登录失败，请检查用户名和密码';
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
