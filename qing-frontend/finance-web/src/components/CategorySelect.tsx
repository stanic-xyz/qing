import { useEffect, useState } from 'react';
import Select from 'react-select';
import axios from 'axios';

interface CategorySelectProps {
  value: string;
  onChange: (value: string) => void;
  disabled?: boolean;
  placeholder?: string;
}

export default function CategorySelect({ value, onChange, disabled, placeholder = '选择分类' }: CategorySelectProps) {
  const [options, setOptions] = useState<any[]>([]);

  useEffect(() => {
    axios.get('/api/categories').then(res => {
      const data = res.data.data || [];
      const formatOptions = (items: any[], parentPath = ''): any[] => {
        let result: any[] = [];
        items.forEach(item => {
          const currentPath = parentPath ? `${parentPath}/${item.name}` : item.name;
          result.push({ value: String(item.id), label: currentPath });
          if (item.children && item.children.length > 0) {
            result = result.concat(formatOptions(item.children, currentPath));
          }
        });
        return result;
      };
      setOptions(formatOptions(data));
    }).catch(console.error);
  }, []);

  const selectedOption = options.find(opt => opt.value === String(value)) || null;

  return (
    <Select
      value={selectedOption}
      onChange={(opt: any) => onChange(opt ? opt.value : '')}
      options={options}
      isDisabled={disabled}
      placeholder={placeholder}
      isClearable
      className="text-sm"
      classNamePrefix="react-select"
    />
  );
}
