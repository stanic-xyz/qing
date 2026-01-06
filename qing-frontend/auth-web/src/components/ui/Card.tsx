import React from 'react';
import type { BaseComponentProps } from './types';

interface CardProps extends BaseComponentProps {
  title?: string;
  subtitle?: string;
  headerActions?: React.ReactNode;
  footer?: React.ReactNode;
  variant?: 'default' | 'elevated' | 'outlined';
  hover?: boolean;
}

const Card: React.FC<CardProps> = ({
  title,
  subtitle,
  headerActions,
  footer,
  variant = 'default',
  hover = false,
  className = '',
  children,
  ...props
}) => {
  const baseClasses = 'rounded-lg';

  const variantClasses = {
    default: 'bg-white border border-gray-200',
    elevated: 'bg-white shadow-md',
    outlined: 'bg-white border-2 border-gray-200',
  };

  const hoverClasses = hover ? 'transition-shadow duration-200 hover:shadow-lg' : '';

  const classes = [
    baseClasses,
    variantClasses[variant],
    hoverClasses,
    className,
  ].filter(Boolean).join(' ');

  return (
    <div className={classes} {...props}>
      {(title || subtitle || headerActions) && (
        <div className="px-6 py-4 border-b border-gray-200">
          <div className="flex items-center justify-between">
            <div>
              {title && (
                <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
              )}
              {subtitle && (
                <p className="text-sm text-gray-600 mt-1">{subtitle}</p>
              )}
            </div>
            {headerActions && (
              <div className="flex items-center space-x-2">
                {headerActions}
              </div>
            )}
          </div>
        </div>
      )}
      <div className="px-6 py-4">
        {children}
      </div>
      {footer && (
        <div className="px-6 py-4 bg-gray-50 border-t border-gray-200 rounded-b-lg">
          {footer}
        </div>
      )}
    </div>
  );
};

export default Card;
