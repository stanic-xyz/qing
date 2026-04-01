import React, { useState, useRef, useEffect } from 'react';
import { ChevronDown, X, Check } from 'lucide-react';

interface Option {
  value: string | number;
  label: string;
}

interface MultiSelectProps {
  options: Option[];
  value: (string | number)[];
  onChange: (value: (string | number)[]) => void;
  placeholder?: string;
  disabled?: boolean;
}

export default function MultiSelect({ options, value, onChange, placeholder = '请选择', disabled = false }: MultiSelectProps) {
  const [isOpen, setIsOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);

  // 点击外部关闭弹窗
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleToggleOption = (optionValue: string | number, e: React.MouseEvent) => {
    e.stopPropagation();
    if (disabled) return;
    
    const newValue = value.includes(optionValue)
      ? value.filter(v => v !== optionValue)
      : [...value, optionValue];
    onChange(newValue);
  };

  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (disabled) return;
    onChange([]);
  };

  const handleRemoveTag = (optionValue: string | number, e: React.MouseEvent) => {
    e.stopPropagation();
    if (disabled) return;
    onChange(value.filter(v => v !== optionValue));
  };

  // 选中的项对象列表
  const selectedOptions = options.filter(opt => value.includes(opt.value));
  
  // 标签展示逻辑：最多展示2个，超出的显示 +N
  const maxDisplay = 1;
  const displayOptions = selectedOptions.slice(0, maxDisplay);
  const restCount = selectedOptions.length - maxDisplay;

  return (
    <div className="relative w-full text-sm" ref={containerRef}>
      <div 
        className={`flex items-center justify-between min-h-[38px] p-1.5 border rounded-md shadow-sm transition-colors ${
          disabled ? 'bg-gray-100 border-gray-200 cursor-not-allowed text-gray-500' : 'bg-white border-gray-300 cursor-pointer hover:border-blue-400'
        }`}
        onClick={() => !disabled && setIsOpen(!isOpen)}
      >
        <div className="flex flex-wrap gap-1 flex-1 overflow-hidden">
          {selectedOptions.length === 0 ? (
            <span className="text-gray-400 px-1">{placeholder}</span>
          ) : (
            <>
              {displayOptions.map(opt => (
                <span 
                  key={opt.value} 
                  className="inline-flex items-center px-2 py-0.5 rounded bg-blue-50 text-blue-700 border border-blue-100 text-xs truncate max-w-full"
                >
                  <span className="truncate">{opt.label}</span>
                  {!disabled && (
                    <X 
                      className="w-3 h-3 ml-1 cursor-pointer hover:text-blue-900 flex-shrink-0" 
                      onClick={(e) => handleRemoveTag(opt.value, e)} 
                    />
                  )}
                </span>
              ))}
              {restCount > 0 && (
                <span className="inline-flex items-center px-2 py-0.5 rounded bg-gray-100 text-gray-600 text-xs">
                  +{restCount}
                </span>
              )}
            </>
          )}
        </div>
        
        <div className="flex items-center px-1 space-x-1 text-gray-400 flex-shrink-0">
          {value.length > 0 && !disabled && (
            <X 
              className="w-4 h-4 cursor-pointer hover:text-gray-600" 
              onClick={handleClear} 
            />
          )}
          <ChevronDown className={`w-4 h-4 transition-transform duration-200 ${isOpen ? 'transform rotate-180' : ''}`} />
        </div>
      </div>

      {/* 下拉面板 */}
      {isOpen && !disabled && (
        <div className="absolute z-50 w-full mt-1 bg-white border border-gray-200 rounded-md shadow-lg max-h-60 overflow-auto">
          {options.length === 0 ? (
            <div className="p-3 text-center text-gray-500 text-sm">暂无数据</div>
          ) : (
            <ul className="py-1">
              {options.map(opt => {
                const isSelected = value.includes(opt.value);
                return (
                  <li 
                    key={opt.value}
                    className={`px-3 py-2 cursor-pointer flex items-center justify-between hover:bg-gray-50 transition-colors ${isSelected ? 'bg-blue-50/50 text-blue-700' : 'text-gray-700'}`}
                    onClick={(e) => handleToggleOption(opt.value, e)}
                  >
                    <span className="truncate">{opt.label}</span>
                    {isSelected && <Check className="w-4 h-4 text-blue-600 flex-shrink-0" />}
                  </li>
                );
              })}
            </ul>
          )}
        </div>
      )}
    </div>
  );
}
