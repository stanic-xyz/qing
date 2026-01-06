import React from 'react';
import {Card, Button} from '../ui';

interface QuickAction {
    id: string;
    label: string;
    icon: React.ReactNode;
    onClick: () => void;
    variant?: 'primary' | 'secondary' | 'success' | 'warning' | 'danger';
    disabled?: boolean;
}

interface QuickActionsProps {
    actions: QuickAction[];
    title?: string;
    columns?: number;
}

const QuickActions: React.FC<QuickActionsProps> = ({
                                                       actions,
                                                       title = 'Quick Actions',
                                                       columns = 2,
                                                   }) => {
    const gridCols = {
        1: 'grid-cols-1',
        2: 'grid-cols-2',
        3: 'grid-cols-3',
        4: 'grid-cols-4',
    };
    console.info(columns);

    return (
        <Card title={title}>
            <div className={`grid ${gridCols[2]} gap-3`}>
                {actions.map((action) => (
                    <Button
                        key={action.id}
                        variant={action.variant || 'secondary'}
                        onClick={action.onClick}
                        disabled={action.disabled}
                        className="w-full justify-center"
                        icon={action.icon}
                        iconPosition="left"
                    >
                        {action.label}
                    </Button>
                ))}
            </div>
        </Card>
    );
};

export default QuickActions;
