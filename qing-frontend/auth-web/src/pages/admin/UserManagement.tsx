import React, {useEffect, useState} from 'react';
import {Table, Button, Space, Modal, Form, Input, message, Tag, Popconfirm, Card} from 'antd';
import {getUsers, createUser, deleteUser, activateUser, deactivateUser} from '../../api/user';
import type {User} from "../../api/types.ts";

const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [form] = Form.useForm();
    const [pagination, setPagination] = useState({current: 1, pageSize: 10, total: 0});

    const fetchUsers = async (page = 1, size = 10) => {
        setLoading(true);
        try {
            const res = await getUsers({page, size});
            if (res.success) {
                setUsers(res.result.items || []);
                setPagination({...pagination, current: page, total: res.result.total});
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleCreate = async (values: any) => {
        try {
            await createUser(values);
            message.success('User created successfully');
            setIsModalVisible(false);
            form.resetFields();
            fetchUsers();
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteUser(id);
            message.success('User deleted successfully');
            fetchUsers();
        } catch (error) {
            console.error(error);
        }
    };

    const handleStatusChange = async (record: any) => {
        try {
            if (record.status === 'ACTIVE') { // Assuming status field
                await deactivateUser(record.id);
            } else {
                await activateUser(record.id);
            }
            message.success('Status updated');
            fetchUsers();
        } catch (error) {
            console.error(error);
        }
    };

    const columns = [
        {title: 'ID', dataIndex: 'id', key: 'id'},
        {title: 'Username', dataIndex: 'username', key: 'username', render: (u: any) => u?.value || u},
        {title: 'Nickname', dataIndex: 'nickname', key: 'nickname'},
        {title: 'Email', dataIndex: 'email', key: 'email', render: (e: any) => e?.value || e},
        {title: 'Phone', dataIndex: 'phone', key: 'phone', render: (p: any) => p?.value || p},
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (status: string) => (
                <Tag color={status === 'ACTIVE' ? 'green' : 'red'}>{status}</Tag>
            )
        },
        {
            title: 'Action',
            key: 'action',
            render: (_: any, record: any) => (
                <Space size="middle">
                    <Button type="link" onClick={() => handleStatusChange(record)}>
                        {record.status === 'ACTIVE' ? 'Deactivate' : 'Activate'}
                    </Button>
                    <Button type="link">Assign Role</Button>
                    <Popconfirm title="Sure to delete?" onConfirm={() => handleDelete(record.id)}>
                        <Button type="link" danger>Delete</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <Card variant={'borderless'} style={{marginBottom: 24}}>
                <Form layout="inline">
                    <Form.Item name="username" label="Username">
                        <Input placeholder="Search username"/>
                    </Form.Item>
                    <Form.Item name="phone" label="Phone">
                        <Input placeholder="Search phone"/>
                    </Form.Item>
                    <Form.Item>
                        <Space>
                            <Button type="primary" onClick={() => fetchUsers(1)}>Search</Button>
                            <Button onClick={() => form.resetFields()}>Reset</Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Card>

            <Card variant={'borderless'}>
                <div style={{marginBottom: 16, display: 'flex', justifyContent: 'space-between'}}>
                    <Button type="primary" onClick={() => setIsModalVisible(true)}>
                        Add User
                    </Button>
                    <Space>
                        <Button>Export</Button>
                        <Button>Import</Button>
                    </Space>
                </div>
                <Table
                    columns={columns}
                    dataSource={users}
                    rowKey="id"
                    loading={loading}
                    pagination={{
                        ...pagination,
                        showSizeChanger: true,
                        showQuickJumper: true,
                        showTotal: (total) => `Total ${total} items`,
                        onChange: (page, pageSize) => fetchUsers(page, pageSize)
                    }}
                />
            </Card>

            <Modal title="Create User" open={isModalVisible} onOk={() => form.submit()}
                   onCancel={() => setIsModalVisible(false)}>
                <Form form={form} onFinish={handleCreate} layout="vertical">
                    <Form.Item name="username" label="Username" rules={[{required: true}]}>
                        <Input/>
                    </Form.Item>
                    <Form.Item name="password" label="Password" rules={[{required: true}]}>
                        <Input.Password/>
                    </Form.Item>
                    <Form.Item name="nickname" label="Nickname" rules={[{required: true}]}>
                        <Input/>
                    </Form.Item>
                    {/* Add other fields */}
                </Form>
            </Modal>
        </div>
    );
};

export default UserManagement;
