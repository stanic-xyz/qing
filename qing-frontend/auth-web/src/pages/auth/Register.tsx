import React, {useState} from 'react';
import {Form, Input, Button, Card, message} from 'antd';
import {useNavigate, Link} from 'react-router-dom';
import {register} from '../../api/auth';

const Register: React.FC = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const onFinish = async (values: any) => {
        setLoading(true);
        try {
            const response = await register(values);
            if (response.success) {
                message.success('注册成功！请登录。');
                navigate('/login');
            }
        } catch (error: any) {
            console.error('注册失败:', error);
            const errorMsg = error?.response?.data?.message || error?.message || '注册失败';
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
                title={<div style={{textAlign: 'center', fontSize: 24, fontWeight: 'bold'}}>注册 Qing Auth</div>}
                style={{width: 480, boxShadow: '0 4px 12px rgba(0,0,0,0.1)'}}
                variant="outlined"
            >
                <Form
                    name="register"
                    onFinish={onFinish}
                    layout="vertical"
                >
                    <Form.Item
                        name="username"
                        label="用户名"
                        rules={[{required: true, message: '请输入用户名!'}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        name="password"
                        label="密码"
                        rules={[{required: true, message: '请输入密码!'}]}
                        hasFeedback
                    >
                        <Input.Password/>
                    </Form.Item>

                    <Form.Item
                        name="confirm"
                        label="确认密码"
                        dependencies={['password']}
                        hasFeedback
                        rules={[
                            {required: true, message: '请确认密码!'},
                            ({getFieldValue}) => ({
                                validator(_, value) {
                                    if (!value || getFieldValue('password') === value) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error('两次输入的密码不一致!'));
                                },
                            }),
                        ]}
                    >
                        <Input.Password/>
                    </Form.Item>

                    <Form.Item
                        name="nickname"
                        label="昵称"
                        rules={[{required: true, message: '请输入昵称!', whitespace: true}]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        name="email"
                        label="邮箱"
                        rules={[
                            {type: 'email', message: '请输入有效的邮箱!'},
                            {required: true, message: '请输入邮箱!'},
                        ]}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        name="phone"
                        label="手机号"
                        rules={[{required: true, message: '请输入手机号!'}]}
                    >
                        <Input style={{width: '100%'}}/>
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" style={{width: '100%'}} loading={loading}>
                            注册
                        </Button>
                        或 <Link to="/login">登录</Link>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default Register;
