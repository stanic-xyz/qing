import React from 'react';
import {Card} from '../ui';

interface ChartCardProps {
    title: string;
    subtitle?: string;
    actions?: React.ReactNode;
    loading?: boolean;
    height?: number;
    children?: any
}

const ChartCard: React.FC<ChartCardProps> = ({
                                                 title,
                                                 subtitle,
                                                 actions,
                                                 loading = false,
                                                 height = 300,
                                                 children,
                                             }) => {
    return (
        <Card
            title={title}
            subtitle={subtitle}
            headerActions={actions}
            className="col-span-2"
        >
            {loading ? (
                <div
                    className="flex items-center justify-center"
                    style={{height: `${height}px`}}
                >
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"/>
                </div>
            ) : (
                <div style={{height: `${height}px`}}>
                    {children}
                </div>
            )}
        </Card>
    );
};

export default ChartCard;
