import React, {useState, useEffect} from 'react';
import {Container, Grid} from '../components/ui';
import {MetricCard, ChartCard, ActivityFeed, QuickActions} from '../components/dashboard';

// Mock data for demonstration
const mockMetrics = [
    {
        id: '1',
        title: 'Total Users',
        value: '2,456',
        change: {value: 12.5, isPositive: true},
        icon: '👥',
        subtitle: 'Active users this month',
    },
    {
        id: '2',
        title: 'Revenue',
        value: '$24,568',
        change: {value: 8.2, isPositive: true},
        icon: '💰',
        subtitle: 'Current month revenue',
    },
    {
        id: '3',
        title: 'Conversion Rate',
        value: '4.8%',
        change: {value: 2.1, isPositive: true},
        icon: '📊',
        subtitle: 'Overall conversion rate',
    },
    {
        id: '4',
        title: 'Active Sessions',
        value: '1,234',
        change: {value: 3.4, isPositive: false},
        icon: '🌐',
        subtitle: 'Active right now',
    },
];

const mockActivities = [
    {
        id: '1',
        type: 'user' as const,
        title: 'New user registration',
        description: 'John Doe registered as a new user',
        timestamp: new Date(Date.now() - 1000 * 60 * 5), // 5 minutes ago
        user: {name: 'John Doe'},
    },
    {
        id: '2',
        type: 'system' as const,
        title: 'System update completed',
        description: 'Database maintenance completed successfully',
        timestamp: new Date(Date.now() - 1000 * 60 * 30), // 30 minutes ago
    },
    {
        id: '3',
        type: 'alert' as const,
        title: 'High traffic alert',
        description: 'Unusual traffic pattern detected',
        timestamp: new Date(Date.now() - 1000 * 60 * 120), // 2 hours ago
    },
    {
        id: '4',
        type: 'user' as const,
        title: 'Profile updated',
        description: 'Jane Smith updated her profile information',
        timestamp: new Date(Date.now() - 1000 * 60 * 240), // 4 hours ago
        user: {name: 'Jane Smith'},
    },
    {
        id: '5',
        type: 'system' as const,
        title: 'Backup completed',
        description: 'Nightly backup completed successfully',
        timestamp: new Date(Date.now() - 1000 * 60 * 360), // 6 hours ago
    },
];

// const quickActions = [
//     {
//         id: 'add-user',
//         label: 'Add User',
//         icon: '➕',
//         onClick: () => console.log('Add user clicked'),
//         variant: 'primary' as const,
//     },
//     {
//         id: 'export-data',
//         label: 'Export Data',
//         icon: '📤',
//         onClick: () => console.log('Export data clicked'),
//         variant: 'secondary' as const,
//     },
//     {
//         id: 'settings',
//         label: 'Settings',
//         icon: '⚙️',
//         onClick: () => console.log('Settings clicked'),
//         variant: 'secondary' as const,
//     },
//     {
//         id: 'help',
//         label: 'Help',
//         icon: '❓',
//         onClick: () => console.log('Help clicked'),
//         variant: 'ghost' as const,
//     },
// ];

const Dashboard: React.FC = () => {
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        // Simulate data loading
        const timer = setTimeout(() => {
            setLoading(false);
        }, 1500);

        return () => clearTimeout(timer);
    }, []);

    return (
        <Container maxWidth="xl" className="py-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-8">Dashboard</h1>

            {/* Metrics Grid */}
            <Grid cols={4} gap="lg" className="mb-8">
                {mockMetrics.map((metric) => (
                    <MetricCard
                        key={metric.id}
                        title={metric.title}
                        value={metric.value}
                        change={metric.change}
                        icon={<span className="text-2xl">{metric.icon}</span>}
                        subtitle={metric.subtitle}
                        loading={loading}
                    />
                ))}
            </Grid>

            {/* Main Content Grid */}
            <Grid cols={12} gap="lg" className="mb-8">
                {/* Chart Area */}
                <ChartCard
                    title="Revenue Overview"
                    subtitle="Monthly revenue trends"
                    loading={loading}
                    height={400}
                >
                    <div
                        className="w-full h-full bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg flex items-center justify-center">
                        <div className="text-center text-gray-500">
                            <div className="text-4xl mb-2">📈</div>
                            <p className="text-sm">Revenue chart will be displayed here</p>
                            <p className="text-xs text-gray-400 mt-1">(Chart integration placeholder)</p>
                        </div>
                    </div>
                </ChartCard>

                {/* Quick Actions */}
                <QuickActions
                    title="Quick Actions"
                    columns={1} actions={[]}/>
            </Grid>

            {/* Bottom Grid */}
            <Grid cols={12} gap="lg">
                {/* Activity Feed */}
                <ActivityFeed
                    activities={mockActivities}
                    loading={loading}
                    maxItems={5}
                />

                {/* System Status */}
                <ChartCard
                    title="System Status"
                    loading={loading}
                    height={300}
                >
                    <div
                        className="w-full h-full bg-gradient-to-r from-green-50 to-emerald-50 rounded-lg flex items-center justify-center">
                        <div className="text-center">
                            <div className="text-4xl mb-2">✅</div>
                            <p className="text-sm font-medium text-green-800">All Systems Operational</p>
                            <p className="text-xs text-green-600 mt-1">Last checked: Just now</p>
                        </div>
                    </div>
                </ChartCard>
            </Grid>
        </Container>
    );
};

export default Dashboard;
