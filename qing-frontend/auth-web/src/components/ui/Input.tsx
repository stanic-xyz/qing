import React, { forwardRef } from 'react';
import type { BaseComponentProps, InputSize, InputVariant } from './types';

interface InputProps extends BaseComponentProps {
  size?: InputSize;
  variant?: InputVariant;
  disabled?: boolean;
  placeholder?: string;
  value?: string;
  defaultValue?: string;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur?: (event: React.FocusEvent<HTMLInputElement>) => void;
  onFocus?: (event: React.FocusEvent<HTMLInputElement>) => void;
  type?: 'text' | 'password' | 'email' | 'number' | 'tel' | 'url';
  name?: string;
  id?: string;
  error?: boolean;
  errorMessage?: string;
  label?: string;
  required?: boolean;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
}

const Input = forwardRef<HTMLInputElement, InputProps>(({
  size = 'md',
  variant = 'default',
  disabled = false,
  placeholder,
  value,
  defaultValue,
  onChange,
  onBlur,
  onFocus,
  type = 'text',
  name,
  id,
  error = false,
  errorMessage,
  label,
  required = false,
  prefix,
  suffix,
  className = '',
  ...props
}, ref) => {
  const baseClasses = 'w-full transition-colors duration-200 focus:outline-none';

  const variantClasses = {
    default: 'border border-gray-300 focus:border-blue-500 focus:ring-1 focus:ring-blue-500',
    filled: 'bg-gray-100 border border-transparent focus:bg-white focus:border-blue-500 focus:ring-1 focus:ring-blue-500',
    outlined: 'border-2 border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-200',
  };

  const sizeClasses = {
    sm: 'px-3 py-1.5 text-sm rounded-md',
    md: 'px-4 py-2 text-sm rounded-md',
    lg: 'px-4 py-3 text-base rounded-lg',
  };

  const errorClasses = error ? 'border-red-500 focus:border-red-500 focus:ring-red-500' : '';
  const disabledClasses = disabled ? 'bg-gray-100 text-gray-500 cursor-not-allowed' : '';

  const inputClasses = [
    baseClasses,
    variantClasses[variant],
    sizeClasses[size],
    errorClasses,
    disabledClasses,
    className,
  ].filter(Boolean).join(' ');

  const renderInput = () => (
    <div className="relative">
      {prefix && (
        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          {prefix}
        </div>
      )}
      <input
        ref={ref}
        type={type}
        className={`${inputClasses} ${prefix ? 'pl-10' : ''} ${suffix ? 'pr-10' : ''}`}
        disabled={disabled}
        placeholder={placeholder}
        value={value}
        defaultValue={defaultValue}
        onChange={onChange}
        onBlur={onBlur}
        onFocus={onFocus}
        name={name}
        id={id}
        required={required}
        {...props}
      />
      {suffix && (
        <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
          {suffix}
        </div>
      )}
    </div>
  );

  if (label) {
    return (
      <div className="space-y-1">
        <label htmlFor={id} className="block text-sm font-medium text-gray-700">
          {label}
          {required && <span className="text-red-500 ml-1">*</span>}
        </label>
        {renderInput()}
        {error && errorMessage && (
          <p className="text-sm text-red-600">{errorMessage}</p>
        )}
      </div>
    );
  }

  return renderInput();
});

Input.displayName = 'Input';

export default Input;
