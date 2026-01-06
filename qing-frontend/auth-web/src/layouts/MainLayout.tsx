import React, {useState} from 'react';
import {Layout, Menu, Avatar, Dropdown, message, Breadcrumb, theme, type MenuProps} from 'antd';
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

const {Header, Sider, Content, Footer} = Layout;

const MainLayout: React.FC = () => {
    const [collapsed, setCollapsed] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();
    const {
        token: {colorBgContainer, borderRadiusLG},
    } = theme.useToken();

    // Mock user info from localStorage or Context
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userInfo');
        localStorage.removeItem('userId');
        navigate('/login');
        message.success('Logged out successfully');
    };

    const menuItems = [
        {
            key: '/profile',
            icon: <UserOutlined/>,
            label: 'Personal Center',
        },
        {
            key: 'admin',
            icon: <SafetyCertificateOutlined/>,
            label: 'System Management',
            children: [
                {
                    key: '/admin/users',
                    icon: <TeamOutlined/>,
                    label: 'User Management',
                },
                {
                    key: '/admin/roles',
                    icon: <SafetyCertificateOutlined/>,
                    label: 'Role Management',
                },
                {
                    key: '/admin/permissions',
                    icon: <LockOutlined/>,
                    label: 'Permission Management',
                },
            ],
        },
    ];

    const items: MenuProps['items'] = [
        {
            key: 'profile',
            label: 'Profile',
            icon: <UserOutlined/>,
            onClick: () => navigate('/profile'),
        },
        {
            type: 'divider',
        },
        {
            key: 'logout',
            label: 'Logout',
            icon: <LogoutOutlined/>,
            onClick: handleLogout,
        },
    ];

    // Generate breadcrumb items based on location
    const breadcrumbItems = location.pathname.split('/').filter(i => i).map((path, index, arr) => {
        const url = `/${arr.slice(0, index + 1).join('/')}`;
        return {title: <Link to={url}>{path.charAt(0).toUpperCase() + path.slice(1)}</Link>};
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
                        <Breadcrumb items={[{title: <Link to="/">Home</Link>}, ...breadcrumbItems]}/>
                    </div>

                    <Dropdown menu={{items}} placement="bottomRight" arrow>
            <span style={{cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 8}}>
              <Avatar src={userInfo.avatar} style={{backgroundColor: '#1890ff'}} icon={<UserOutlined/>}/>
              <span style={{fontWeight: 500}}>{userInfo.nickname || userInfo.username || 'User'}</span>
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
