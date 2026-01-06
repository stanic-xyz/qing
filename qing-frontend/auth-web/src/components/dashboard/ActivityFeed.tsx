import React from 'react';
import { Card } from '../ui';

interface ActivityItem {
  id: string;
  type: 'user' | 'system' | 'alert';
  title: string;
  description: string;
  timestamp: Date;
  user?: {
    name: string;
    avatar?: string;
  };
}

interface ActivityFeedProps {
  activities: ActivityItem[];
  loading?: boolean;
  maxItems?: number;
}

const ActivityFeed: React.FC<ActivityFeedProps> = ({
  activities,
  loading = false,
  maxItems = 5,
}) => {
  const getTypeIcon = (type: ActivityItem['type']) => {
    switch (type) {
      case 'user':
        return '👤';
      case 'system':
        return '⚙️';
      case 'alert':
        return '⚠️';
      default:
        return '📋';
    }
  };

  const formatTime = (date: Date) => {
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    const hours = Math.floor(diff / (1000 * 60 * 60));
    
    if (hours < 1) return 'Just now';
    if (hours < 24) return `${hours}h ago`;
    return date.toLocaleDateString();
  };

  if (loading) {
    return (
      <Card title="Recent Activity" className="col-span-2">
        {[...Array(maxItems)].map((_, i) => (
          <div key={i} className="flex items-center space-x-3 py-3 border-b border-gray-200 last:border-b-0">
            <div className="h-8 w-8 bg-gray-200 rounded-full animate-pulse"></div>
            <div className="flex-1">
              <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
              <div className="h-3 bg-gray-200 rounded w-1/2"></div>
            </div>
          </div>
        ))}
      </Card>
    );
  }

  return (
    <Card title="Recent Activity" className="col-span-2">
      <div className="space-y-3">
        {activities.slice(0, maxItems).map((activity) => (
          <div key={activity.id} className="flex items-start space-x-3 py-2">
            <div className="flex-shrink-0 w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
              <span className="text-sm">{getTypeIcon(activity.type)}</span>
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 truncate">
                {activity.title}
              </p>
              <p className="text-sm text-gray-500 truncate">
                {activity.description}
              </p>
              <p className="text-xs text-gray-400 mt-1">
                {formatTime(activity.timestamp)}
              </p>
            </div>
          </div>
        ))}
      </div>
    </Card>
  );
};

export default ActivityFeed;