import React from 'react';
import {Card, Button} from '../ui';

export interface QuickAction {
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
                                                       title = '快捷操作',
                                                   }) => {
    const gridCols = 'grid-cols-2';

    return (
        <Card title={title}>
            <div className={`grid ${gridCols} gap-3`}>
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
