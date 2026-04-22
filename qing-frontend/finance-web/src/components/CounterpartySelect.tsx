import React from 'react';
import AsyncSelect from 'react-select/async';
import axios from 'axios';

interface CounterpartySelectProps {
  value: string;
  onChange: (value: string) => void;
  disabled?: boolean;
  placeholder?: string;
}

export default function CounterpartySelect({ value, onChange, disabled, placeholder = '搜索交易对手' }: CounterpartySelectProps) {
  const loadOptions = async (inputValue: string) => {
    if (inputValue.length < 2) return [];
    try {
      const res = await axios.get(`/api/finance/counterparties/search?keyword=${encodeURIComponent(inputValue)}`);
      const data = res.data.data || [];
      return data.map((cp: any) => {
        let accountStr = '';
        if (cp.accounts && cp.accounts.length > 0) {
           const firstAcc = cp.accounts[0];
           accountStr = ` | ${firstAcc.accountNo || ''} ${firstAcc.bankName || ''}`;
        }
        return {
          value: String(cp.id),
          label: `${cp.name}${accountStr}`
        };
      });
    } catch (e) {
      console.error(e);
      return [];
    }
  };

  const [initialOption, setInitialOption] = React.useState<any>(null);

  React.useEffect(() => {
    if (value) {
      axios.get('/api/finance/counterparties').then(res => {
        const all = res.data.data || [];
        const found = all.find((c: any) => String(c.id) === String(value));
        if (found) {
          setInitialOption({ value: String(found.id), label: found.name });
        }
      }).catch(console.error);
    }
  }, [value]);

  return (
    <AsyncSelect
      cacheOptions
      defaultOptions
      loadOptions={loadOptions}
      value={initialOption}
      onChange={(opt: any) => {
        setInitialOption(opt);
        onChange(opt ? opt.value : '');
      }}
      isDisabled={disabled}
      placeholder={placeholder}
      isClearable
      className="text-sm"
      classNamePrefix="react-select"
    />
  );
}