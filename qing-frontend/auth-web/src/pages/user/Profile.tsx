import React, { useEffect, useState } from 'react';
import { Form, Input, Button, Card, message, Descriptions, Avatar, Modal } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { getUserInfo, updateUser, sendEmailCode, changeEmail } from '../../api/user';
import type {User} from "../../api/types.ts";

const Profile: React.FC = () => {
  const [user, setUser] = useState<User>();
  const [loading, setLoading] = useState(true);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isEmailModalVisible, setIsEmailModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [emailForm] = Form.useForm();

  // Assuming we store userId in localStorage after login
  // Or parse it from token. For demo, let's assume we have it.
  // Ideally, backend /auth/login should return userId.
  // If not, we might need to fetch /users/me if it existed, or rely on stored ID.
  // For now, I'll assume we can get it or I will fetch a hardcoded ID if missing for demo.
  // In real app, store userId in login.
  const userId = localStorage.getItem('userId') || '1'; // Default to 1 for dev if missing

  const fetchUser = async () => {
    try {
      const res = await getUserInfo(userId);
      if (res.success) {
        setUser(res.result);
        // Update local storage info
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
      message.success('Profile updated successfully');
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
              message.error('Please enter new email first');
              return;
          }
          await sendEmailCode({ email, userId });
          message.success('Verification code sent');
      } catch (error) {
          console.error(error);
      }
  };

  const handleChangeEmail = async (values: any) => {
      try {
          await changeEmail({ userId, ...values });
          message.success('Email changed successfully');
          setIsEmailModalVisible(false);
          fetchUser();
      } catch (error) {
          console.error(error);
      }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <Card title="User Profile" extra={<Button type="primary" onClick={() => { form.setFieldsValue(user); setIsModalVisible(true); }}>Edit Profile</Button>}>
        <div style={{ display: 'flex', alignItems: 'center', marginBottom: 24 }}>
          <Avatar size={64} src={user?.avatar} icon={<UserOutlined />} />
          <div style={{ marginLeft: 24 }}>
            <h2>{user?.nickname}</h2>
            <p>{user?.description || 'No description'}</p>
          </div>
        </div>
        <Descriptions bordered column={1}>
          <Descriptions.Item label="Username">{user?.username?.value}</Descriptions.Item>
          <Descriptions.Item label="Phone">{user?.phone?.value}</Descriptions.Item>
          <Descriptions.Item label="Email">
              {user?.email?.value}
              <Button type="link" onClick={() => setIsEmailModalVisible(true)}>Change Email</Button>
          </Descriptions.Item>
          <Descriptions.Item label="Roles">
              {user?.roles?.map((r: any) => r.name).join(', ')}
          </Descriptions.Item>
        </Descriptions>
      </Card>

      <Modal title="Edit Profile" open={isModalVisible} onOk={() => form.submit()} onCancel={() => setIsModalVisible(false)}>
        <Form form={form} onFinish={handleUpdate} layout="vertical">
          <Form.Item name="nickname" label="Nickname" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="avatar" label="Avatar URL">
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone">
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="Change Email" open={isEmailModalVisible} onOk={() => emailForm.submit()} onCancel={() => setIsEmailModalVisible(false)}>
          <Form form={emailForm} onFinish={handleChangeEmail} layout="vertical">
              <Form.Item name="newEmail" label="New Email" rules={[{ required: true, type: 'email' }]}>
                  <Input />
              </Form.Item>
              <Form.Item label="Verification Code" style={{ marginBottom: 0 }}>
                  <div style={{ display: 'flex', gap: 8 }}>
                      <Form.Item name="code" rules={[{ required: true }]} noStyle>
                          <Input />
                      </Form.Item>
                      <Button onClick={handleSendCode}>Send Code</Button>
                  </div>
              </Form.Item>
          </Form>
      </Modal>
    </div>
  );
};

export default Profile;
