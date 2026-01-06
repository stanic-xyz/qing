import React, {useEffect, useState} from 'react';
import {Table, Button, Space, Modal, Form, Input, message, Popconfirm, Drawer, Tree, Card, type TreeDataNode} from 'antd';
import {getRoles, createRole, updateRole, deleteRole, getRolePermissions, assignPermissions} from '../../api/role';
import {getPermissions} from '../../api/permission';
import type {Role} from "../../api/types.ts";

const RoleManagement: React.FC = () => {

    let rolesState: Role[] = [];
    const [roles, setRoles] = useState(rolesState);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isPermDrawerVisible, setIsPermDrawerVisible] = useState(false);
    const [currentRole, setCurrentRole] = useState<any>(null);
    const [permissions, setPermissions] = useState<TreeDataNode[]>([]);
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>([]);
    const [form] = Form.useForm();

    const fetchRoles = async () => {
        setLoading(true);
        try {
            const res = await getRoles({});
            if (res.success) {
                setRoles(res.result.items || []);
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const fetchPermissions = async () => {
        const res = await getPermissions();
        if (res.success) {
            // Transform flat list to tree if needed, or if backend returns tree
            // Assuming backend returns tree or list that Tree component can handle
            setPermissions([]);
        }
    }

    useEffect(() => {
        fetchRoles();
        fetchPermissions();
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
            // Assuming res.result is list of permission IDs
            // Or if it returns objects, map to IDs.
            // Adjust based on actual API response for role permissions
            const perms = res.result || [];
            // If perms are objects
            setCheckedKeys(perms.map((p: any) => p.id || p));
        }
    };

    const handleAssignPermissions = async () => {
        try {
            await assignPermissions(currentRole.id, checkedKeys as string[]);
            message.success('Permissions assigned');
            setIsPermDrawerVisible(false);
        } catch (error) {
            console.error(error);
        }
    };

    const columns = [
        {title: 'ID', dataIndex: 'id', key: 'id'},
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
                    <Popconfirm title="Delete?" onConfirm={() => handleDelete(record.id)}>
                        <Button type="link" danger>Delete</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <Card variant={"borderless"} style={{marginBottom: 24}}>
                <Form layout="inline">
                    <Form.Item name="name" label="Role Name">
                        <Input placeholder="Search role name"/>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" onClick={fetchRoles}>Search</Button>
                    </Form.Item>
                </Form>
            </Card>

            <Card variant="borderless">
                <div style={{marginBottom: 16}}>
                    <Button type="primary" onClick={() => {
                        setCurrentRole(null);
                        form.resetFields();
                        setIsModalVisible(true);
                    }}>
                        Create Role
                    </Button>
                </div>
                <Table columns={columns} dataSource={roles} rowKey="id" loading={loading}/>
            </Card>

            <Modal title={currentRole ? "Edit Role" : "Create Role"} open={isModalVisible} onOk={() => form.submit()}
                   onCancel={() => setIsModalVisible(false)}>
                <Form form={form} onFinish={handleCreateOrUpdate} layout="vertical">
                    <Form.Item name="code" label="Code" rules={[{required: true}]}>
                        <Input/>
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
