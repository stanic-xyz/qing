import React, {useState} from 'react';
import {Layout, Menu, Avatar, Dropdown, message, Breadcrumb, theme, type MenuProps, Modal} from 'antd';
import {
    UserOutlined,
    TeamOutlined,
    SafetyCertificateOutlined,
    LockOutlined,
    LogoutOutlined,
    MenuUnfoldOutlined,
    MenuFoldOutlined,
} from '@ant-design/icons';
import {useNavigate, useLocation, Outlet, Link} from 'react-router-dom';
import {logout as logoutApi} from '../api/auth';

const {Header, Sider, Content, Footer} = Layout;

const MainLayout: React.FC = () => {
    const [collapsed, setCollapsed] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const {
        token: {colorBgContainer, borderRadiusLG},
    } = theme.useToken();

    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');

    const handleLogout = async () => {
        Modal.confirm({
            title: '确认登出',
            content: '确定要退出登录吗？',
            okText: '确认',
            cancelText: '取消',
            async onOk() {
                try {
                    await logoutApi();
                    message.success('登出成功');
                } catch (error) {
                    console.error('登出失败:', error);
                } finally {
                    localStorage.removeItem('token');
                    localStorage.removeItem('userInfo');
                    localStorage.removeItem('userId');
                    localStorage.removeItem('refreshToken');
                    navigate('/login');
                }
            },
        });
    };

    const menuItems: MenuProps['items'] = [
        {
            key: '/profile',
            icon: <UserOutlined/>,
            label: '个人中心',
        },
        {
            key: 'admin',
            icon: <SafetyCertificateOutlined/>,
            label: '系统管理',
            children: [
                {
                    key: '/admin/users',
                    icon: <TeamOutlined/>,
                    label: '用户管理',
                },
                {
                    key: '/admin/roles',
                    icon: <SafetyCertificateOutlined/>,
                    label: '角色管理',
                },
                {
                    key: '/admin/permissions',
                    icon: <LockOutlined/>,
                    label: '权限管理',
                },
            ],
        },
    ];

    const userMenuItems: MenuProps['items'] = [
        {
            key: 'profile',
            label: '个人信息',
            icon: <UserOutlined/>,
            onClick: () => navigate('/profile'),
        },
        {
            type: 'divider',
        },
        {
            key: 'logout',
            label: '退出登录',
            icon: <LogoutOutlined/>,
            onClick: handleLogout,
            danger: true,
        },
    ];

    const breadcrumbItems = location.pathname.split('/').filter(i => i).map((path, index, arr) => {
        const url = `/${arr.slice(0, index + 1).join('/')}`;
        const labelMap: Record<string, string> = {
            profile: '个人中心',
            admin: '系统管理',
            users: '用户管理',
            roles: '角色管理',
            permissions: '权限管理',
        };
        return {title: <Link to={url}>{labelMap[path] || path}</Link>};
    });

    return (
        <Layout style={{minHeight: '100vh'}}>
            <Sider
                trigger={null}
                collapsible
                collapsed={collapsed}
                style={{
                    overflow: 'auto',
                    height: '100vh',
                    position: 'fixed',
                    left: 0,
                    top: 0,
                    bottom: 0,
                    zIndex: 100,
                }}
            >
                <div className="logo" style={{
                    height: 64,
                    margin: 0,
                    background: 'rgba(255, 255, 255, 0.1)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    color: 'white',
                    fontSize: '18px',
                    fontWeight: 'bold'
                }}>
                    {collapsed ? 'QA' : 'Qing Auth'}
                </div>
                <Menu
                    theme="dark"
                    mode="inline"
                    defaultSelectedKeys={[location.pathname]}
                    defaultOpenKeys={['admin']}
                    items={menuItems}
                    onClick={({key}) => navigate(key)}
                />
            </Sider>
            <Layout className="site-layout" style={{marginLeft: collapsed ? 80 : 200, transition: 'all 0.2s'}}>
                <Header style={{
                    padding: 0,
                    background: colorBgContainer,
                    position: 'sticky',
                    top: 0,
                    zIndex: 99,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    paddingRight: 24,
                    boxShadow: '0 1px 4px rgba(0,21,41,.08)'
                }}>
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        {React.createElement(collapsed ? MenuUnfoldOutlined : MenuFoldOutlined, {
                            className: 'trigger',
                            onClick: () => setCollapsed(!collapsed),
                            style: {padding: '0 24px', fontSize: '18px', cursor: 'pointer'}
                        })}
                        <Breadcrumb items={[{title: <Link to="/">首页</Link>}, ...breadcrumbItems]}/>
                    </div>

                    <Dropdown menu={{items: userMenuItems}} placement="bottomRight" arrow>
                        <span style={{cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 8}}>
                            <Avatar src={userInfo.avatar} style={{backgroundColor: '#1890ff'}} icon={<UserOutlined/>}/>
                            <span style={{fontWeight: 500}}>{userInfo.nickname || userInfo.username || '用户'}</span>
                        </span>
                    </Dropdown>
                </Header>
                <Content
                    style={{
                        margin: '24px 16px 0',
                        overflow: 'initial',
                    }}
                >
                    <div style={{
                        padding: 24,
                        minHeight: 360,
                        background: colorBgContainer,
                        borderRadius: borderRadiusLG
                    }}>
                        <Outlet/>
                    </div>
                </Content>
                <Footer style={{textAlign: 'center'}}>Qing Auth System ©2025 Created by Chen Yunlong</Footer>
            </Layout>
        </Layout>
    );
};

export default MainLayout;
