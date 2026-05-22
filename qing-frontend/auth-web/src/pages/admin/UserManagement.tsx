import React, {useEffect, useState} from 'react';
import {Table, Button, Space, Modal, Form, Input, message, Tag, Popconfirm, Card, Select} from 'antd';
import {getUsers, createUser, deleteUser, activateUser, deactivateUser, assignRolesToUser} from '../../api/user';
import {getRoles} from '../../api/role';
import type {User} from "../../api/types.ts";

const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isAssignRoleModalVisible, setIsAssignRoleModalVisible] = useState(false);
    const [selectedUser, setSelectedUser] = useState<any>(null);
    const [allRoles, setAllRoles] = useState<Role[]>([]);
    const [selectedRoleIds, setSelectedRoleIds] = useState<string[]>([]);
    const [form] = Form.useForm();
    const [pagination, setPagination] = useState({current: 0, pageSize: 10, total: 0});

    const fetchUsers = async (page = 0, size = 10) => {
        console.log('=== fetchUsers called ===');
        console.log('Page:', page, 'Size:', size);
        setLoading(true);
        try {
            const res = await getUsers({page, size});
            console.log('Response received:', res);
            console.log('res.success:', res?.success);
            if (res?.success) {
                const data = res.result?.content || res.result?.items || [];
                const total = res.result?.page?.totalElements || res.result?.total || 0;
                console.log('Setting users:', data);
                console.log('Users count:', data.length);
                setUsers(data);
                setPagination(prev => ({...prev, current: page, total: total}));
                console.log('Users state updated');
            } else {
                console.error('API failed:', res);
            }
        } catch (error) {
            console.error('Error fetching users:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        console.log('=== UserManagement mounted ===');
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
            if (record.active) {
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

    const openAssignRoleModal = async (record: any) => {
        setSelectedUser(record);
        try {
            const res = await getRoles({page: 0, size: 100});
            if (res?.success) {
                setAllRoles(res.result?.content || []);
            }
        } catch (error) {
            console.error('Failed to fetch roles:', error);
            message.error('Failed to fetch roles');
        }
        setIsAssignRoleModalVisible(true);
    };

    const handleAssignRoles = async () => {
        if (!selectedUser || selectedRoleIds.length === 0) {
            message.error('Please select at least one role');
            return;
        }

        try {
            await assignRolesToUser(String(selectedUser.id), selectedRoleIds);
            message.success('Roles assigned successfully');
            setIsAssignRoleModalVisible(false);
            setSelectedRoleIds([]);
        } catch (error) {
            console.error('Failed to assign roles:', error);
            message.error('Failed to assign roles');
        }
    };

    const columns = [
        {title: 'ID', dataIndex: 'id', key: 'id'},
        {title: 'Username', dataIndex: 'username', key: 'username'},
        {title: 'Nickname', dataIndex: 'nickname', key: 'nickname'},
        {title: 'Email', dataIndex: 'email', key: 'email'},
        {title: 'Phone', dataIndex: 'phone', key: 'phone'},
        {
            title: 'Status',
            dataIndex: 'active',
            key: 'active',
            render: (active: boolean) => (
                <Tag color={active ? 'green' : 'red'}>
                    {active ? 'ACTIVE' : 'INACTIVE'}
                </Tag>
            )
        },
        {
            title: 'Action',
            key: 'action',
            render: (_: any, record: any) => (
                <Space size="middle">
                    <Button type="link" onClick={() => handleStatusChange(record)}>
                        {record.active ? 'Deactivate' : 'Activate'}
                    </Button>
                    <Button type="link" onClick={() => openAssignRoleModal(record)}>Assign Role</Button>
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
                            <Button type="primary" onClick={() => fetchUsers(0)}>Search</Button>
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
                    dataSource={users}
                    columns={columns}
                    rowKey={(record) => String(record.id)}
                    loading={loading}
                    pagination={false}
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

            <Modal
                title={`Assign Role to ${selectedUser?.username || ''}`}
                open={isAssignRoleModalVisible}
                onOk={handleAssignRoles}
                onCancel={() => setIsAssignRoleModalVisible(false)}
            >
                <Form layout="vertical">
                    <Form.Item label="Select Roles">
                        <Select
                            mode="multiple"
                            placeholder="Select roles"
                            value={selectedRoleIds}
                            onChange={(values) => setSelectedRoleIds(values)}
                            options={allRoles.map(r => ({
                                label: r.name,
                                value: String(r.id)
                            }))}
                        />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default UserManagement;
