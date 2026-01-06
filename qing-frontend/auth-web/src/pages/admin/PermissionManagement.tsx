import React, {useEffect, useState} from 'react';
import {Table, Button, Space, Modal, Form, Input, message, Popconfirm, Select, InputNumber, Card} from 'antd';
import {getPermissions, createPermission, updatePermission, deletePermission} from '../../api/permission';
import type {Permission} from "../../api/types.ts";

const PermissionManagement: React.FC = () => {

    let permissionList: Permission[] = [];

    const [permissions, setPermissions] = useState(permissionList);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [currentPerm, setCurrentPerm] = useState<any>(null);
    const [form] = Form.useForm();

    const fetchPermissions = async () => {
        setLoading(true);
        try {
            const res = await getPermissions();
            if (res.success) {
                setPermissions(res.result || []);
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPermissions();
    }, []);

    const handleCreateOrUpdate = async (values: any) => {
        try {
            if (currentPerm) {
                await updatePermission(currentPerm.id, values);
                message.success('Permission updated');
            } else {
                await createPermission(values);
                message.success('Permission created');
            }
            setIsModalVisible(false);
            form.resetFields();
            setCurrentPerm(null);
            fetchPermissions();
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deletePermission(id);
            message.success('Permission deleted');
            fetchPermissions();
        } catch (error) {
            console.error(error);
        }
    };

    const columns = [
        {title: 'Name', dataIndex: 'name', key: 'name'},
        {title: 'Code', dataIndex: 'code', key: 'code'},
        {title: 'Type', dataIndex: 'type', key: 'type'},
        {title: 'Resource', dataIndex: 'resource', key: 'resource'},
        {title: 'Action', dataIndex: 'action', key: 'action'},
        {
            title: 'Action',
            key: 'action',
            render: (_: any, record: any) => (
                <Space size="middle">
                    <Button type="link" onClick={() => {
                        setCurrentPerm(record);
                        form.setFieldsValue(record);
                        setIsModalVisible(true);
                    }}>Edit</Button>
                    <Popconfirm title="Delete?" onConfirm={() => handleDelete(record.id)}>
                        <Button type="link" danger>Delete</Button>
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <Card variant="outlined">
            <div style={{marginBottom: 16}}>
                <Button type="primary" onClick={() => {
                    setCurrentPerm(null);
                    form.resetFields();
                    setIsModalVisible(true);
                }}>
                    Add Permission
                </Button>
            </div>
            <Table columns={columns} dataSource={permissions} rowKey="id" loading={loading} pagination={false}/>

            <Modal title={currentPerm ? "Edit Permission" : "Add Permission"} open={isModalVisible}
                   onOk={() => form.submit()} onCancel={() => setIsModalVisible(false)}>
                <Form form={form} onFinish={handleCreateOrUpdate} layout="vertical">
                    <Form.Item name="name" label="Name" rules={[{required: true}]}>
                        <Input/>
                    </Form.Item>
                    <Form.Item name="code" label="Code" rules={[{required: true}]}>
                        <Input/>
                    </Form.Item>
                    <Form.Item name="type" label="Type" rules={[{required: true}]}>
                        <Select>
                            <Select.Option value="MENU">Menu</Select.Option>
                            <Select.Option value="BUTTON">Button</Select.Option>
                            <Select.Option value="API">API</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item name="resource" label="Resource">
                        <Input/>
                    </Form.Item>
                    <Form.Item name="action" label="Action">
                        <Input/>
                    </Form.Item>
                    <Form.Item name="parentId" label="Parent ID">
                        <Input/>
                    </Form.Item>
                    <Form.Item name="sortOrder" label="Sort Order">
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item name="description" label="Description">
                        <Input.TextArea/>
                    </Form.Item>
                </Form>
            </Modal>
        </Card>
    );
};

export default PermissionManagement;
