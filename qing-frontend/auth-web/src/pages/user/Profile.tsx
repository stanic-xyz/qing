import React, {useEffect, useState} from 'react';
import {Form, Input, Button, Card, message, Descriptions, Avatar, Modal} from 'antd';
import {UserOutlined} from '@ant-design/icons';
import {getUserInfo, updateUser, sendEmailCode, changeEmail} from '../../api/user';
import type {User} from "../../api/types.ts";

const Profile: React.FC = () => {
    const [user, setUser] = useState<User>();
    const [loading, setLoading] = useState(true);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isEmailModalVisible, setIsEmailModalVisible] = useState(false);
    const [form] = Form.useForm();
    const [emailForm] = Form.useForm();

    const userId = localStorage.getItem('userId') || '1';

    const fetchUser = async () => {
        try {
            const res = await getUserInfo(userId);
            if (res.success) {
                setUser(res.result);
                localStorage.setItem('userInfo', JSON.stringify(res.result));
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUser();
    }, []);

    const handleUpdate = async (values: any) => {
        try {
            await updateUser(userId, values);
            message.success('个人信息更新成功');
            setIsModalVisible(false);
            fetchUser();
        } catch (error) {
            console.error(error);
        }
    };

    const handleSendCode = async () => {
        try {
            const email = emailForm.getFieldValue('newEmail');
            if (!email) {
                message.error('请先输入新邮箱');
                return;
            }
            await sendEmailCode({email, userId});
            message.success('验证码已发送');
        } catch (error) {
            console.error(error);
        }
    };

    const handleChangeEmail = async (values: any) => {
        try {
            await changeEmail({userId, ...values});
            message.success('邮箱修改成功');
            setIsEmailModalVisible(false);
            fetchUser();
        } catch (error) {
            console.error(error);
        }
    };

    if (loading) return <div>加载中...</div>;

    return (
        <div>
            <Card title="个人信息" extra={<Button type="primary"
                                                   onClick={() => {
                                                       form.setFieldsValue(user);
                                                       setIsModalVisible(true);
                                                   }}>编辑资料</Button>}>
                <div style={{display: 'flex', alignItems: 'center', marginBottom: 24}}>
                    <Avatar size={64} src={user?.avatar} icon={<UserOutlined/>}/>
                    <div style={{marginLeft: 24}}>
                        <h2>{user?.nickname}</h2>
                        <p>{user?.description || '暂无描述'}</p>
                    </div>
                </div>
                <Descriptions bordered column={1}>
                    <Descriptions.Item label="用户名">{user?.username?.value}</Descriptions.Item>
                    <Descriptions.Item label="手机号">{user?.phone?.value}</Descriptions.Item>
                    <Descriptions.Item label="邮箱">
                        {user?.email?.value}
                        <Button type="link" onClick={() => setIsEmailModalVisible(true)}>修改邮箱</Button>
                    </Descriptions.Item>
                    <Descriptions.Item label="角色">
                        {user?.roles?.map((r: any) => r.name).join(', ')}
                    </Descriptions.Item>
                </Descriptions>
            </Card>

            <Modal title="编辑资料" open={isModalVisible} onOk={() => form.submit()} onCancel={() => setIsModalVisible(false)}>
                <Form form={form} onFinish={handleUpdate} layout="vertical">
                    <Form.Item name="nickname" label="昵称" rules={[{required: true}]}>
                        <Input/>
                    </Form.Item>
                    <Form.Item name="avatar" label="头像URL">
                        <Input/>
                    </Form.Item>
                    <Form.Item name="phone" label="手机号">
                        <Input/>
                    </Form.Item>
                    <Form.Item name="description" label="个人描述">
                        <Input.TextArea/>
                    </Form.Item>
                </Form>
            </Modal>

            <Modal title="修改邮箱" open={isEmailModalVisible} onOk={() => emailForm.submit()}
                   onCancel={() => setIsEmailModalVisible(false)}>
                <Form form={emailForm} onFinish={handleChangeEmail} layout="vertical">
                    <Form.Item name="newEmail" label="新邮箱" rules={[{required: true, type: 'email'}]}>
                        <Input/>
                    </Form.Item>
                    <Form.Item label="验证码" style={{marginBottom: 0}}>
                        <div style={{display: 'flex', gap: 8}}>
                            <Form.Item name="code" rules={[{required: true}]} noStyle>
                                <Input/>
                            </Form.Item>
                            <Button onClick={handleSendCode}>发送验证码</Button>
                        </div>
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default Profile;
