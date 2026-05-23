import React, {useEffect, useState} from 'react';
import {
    Table,
    Button,
    Space,
    Modal,
    Form,
    Input,
    message,
    Popconfirm,
    Drawer,
    Tree,
    Card,
    type TreeDataNode
} from 'antd';
import {getRoles, createRole, updateRole, deleteRole, getRolePermissions, assignPermissions} from '../../api/role';
import {getUsers} from '../../api/user';
import type {Role} from "../../api/types.ts";

const RoleManagement: React.FC = () => {
    const [roles, setRoles] = useState<Role[]>([]);
    const [users, setUsers] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isPermDrawerVisible, setIsPermDrawerVisible] = useState(false);
    const [, setIsAssignRoleDrawerVisible] = useState(false);
    const [currentRole, setCurrentRole] = useState<Role | null>(null);
    const [permissions] = useState<TreeDataNode[]>([]);
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);
    const [form] = Form.useForm();
    const [pagination, setPagination] = useState({current: 0, pageSize: 20, total: 0});

    const fetchRoles = async (page = 0, size = 20) => {
        setLoading(true);
        try {
            const res = await getRoles({page, size});
            if (res?.success) {
                const data = res.result?.content || res.result?.items || [];
                const total = res.result?.page?.totalElements || res.result?.total || 0;
                setRoles(data);
                setPagination({current: page, pageSize: size, total: total});
            }
        } catch (error) {
            console.error('Error fetching roles:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        console.debug(users, pagination);
        fetchRoles();
    }, []);

    const handleCreateOrUpdate = async (values: any) => {
        try {
            if (currentRole) {
                await updateRole(currentRole.id, values);
                message.success('Role updated');
            } else {
                await createRole(values);
                message.success('Role created');
            }
            setIsModalVisible(false);
            form.resetFields();
            setCurrentRole(null);
            fetchRoles();
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteRole(id);
            message.success('Role deleted');
            fetchRoles();
        } catch (error) {
            console.error(error);
        }
    };

    const openPermissionDrawer = async (record: any) => {
        setCurrentRole(record);
        setIsPermDrawerVisible(true);
        const res = await getRolePermissions(record.id);
        if (res.success) {
            setCheckedKeys(res.result || []);
        }
    };

    const handleAssignPermissions = async () => {
        try {
            if (currentRole) {
                await assignPermissions(currentRole.id, checkedKeys as string[]);
                message.success('Permissions assigned');
                setIsPermDrawerVisible(false);
            }
        } catch (error) {
            console.error(error);
        }
    };

    const openAssignRoleDrawer = async (record: any) => {
        setCurrentRole(record);
        setIsAssignRoleDrawerVisible(true);
        try {
            const res = await getUsers({page: 0, size: 100});
            if (res?.success) {
                setUsers(res.result?.content || res.result?.items || []);
            }
        } catch (error) {
            console.error('Failed to fetch users:', error);
            message.error('Failed to fetch users');
        }
    };
    const columns = [
        {title: 'Code', dataIndex: 'code', key: 'code'},
        {title: 'Name', dataIndex: 'name', key: 'name'},
        {title: 'Description', dataIndex: 'description', key: 'description'},
        {
            title: 'Action',
            key: 'action',
            render: (_: any, record: any) => (
                <Space size="middle">
                    <Button type="link" onClick={() => {
                        setCurrentRole(record);
                        form.setFieldsValue(record);
                        setIsModalVisible(true);
                    }}>Edit</Button>
                    <Button type="link" onClick={() => openPermissionDrawer(record)}>Permissions</Button>
                    <Button type="link" onClick={() => openAssignRoleDrawer(record)}>Assign Role</Button>
                    <Popconfirm title="Delete?" onConfirm={() => handleDelete(record.id)}>
                        <Button type="link" danger>Delete</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <Card variant="borderless" style={{marginBottom: 24}}>
                <Form layout="inline">
                    <Form.Item name="name" label="Role Name">
                        <Input placeholder="Search role name"/>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" onClick={() => fetchRoles()}>Search</Button>
                    </Form.Item>
                </Form>
            </Card>

            <Card variant="borderless">
                <Button type="primary" onClick={() => {
                    setCurrentRole(null);
                    form.resetFields();
                    setIsModalVisible(true);
                }} style={{marginBottom: 16}}>
                    Create Role
                </Button>

                <Table
                    dataSource={roles}
                    columns={columns}
                    rowKey={(record) => String(record.id)}
                    loading={loading}
                    pagination={false}
                />
            </Card>

            <Modal title={currentRole ? "Edit Role" : "Create Role"}
                   open={isModalVisible}
                   onOk={() => form.submit()}
                   onCancel={() => setIsModalVisible(false)}>
                <Form form={form} onFinish={handleCreateOrUpdate} layout="vertical">
                    <Form.Item name="code" label="Code" rules={[{required: true}]}>
                        <Input disabled={!!currentRole}/>
                    </Form.Item>
                    <Form.Item name="name" label="Name" rules={[{required: true}]}>
                        <Input/>
                    </Form.Item>
                    <Form.Item name="description" label="Description">
                        <Input.TextArea/>
                    </Form.Item>
                </Form>
            </Modal>

            <Drawer title={`Assign Permissions to ${currentRole?.name}`} placement="right"
                    onClose={() => setIsPermDrawerVisible(false)} open={isPermDrawerVisible} width={500}
                    extra={
                        <Button type="primary" onClick={handleAssignPermissions}>
                            Save
                        </Button>
                    }
            >
                <Tree
                    checkable
                    checkedKeys={checkedKeys}
                    onCheck={(keys) => setCheckedKeys(keys as React.Key[])}
                    treeData={permissions}
                    fieldNames={{title: 'name', key: 'id', children: 'children'}}
                />
            </Drawer>
        </div>
    );
};

export default RoleManagement;
