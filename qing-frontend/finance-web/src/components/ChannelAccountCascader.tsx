import { useEffect, useState } from 'react';
import { useChannelStore } from '../store/useChannelStore';
import type { Channel, Account } from '../types/channel';
import MultiSelect from './ui/MultiSelect';

interface Props {
  selectedChannels: number[];
  selectedAccounts: number[];
  onChange: (channels: number[], accounts: number[]) => void;
  disabled?: boolean;
}

export default function ChannelAccountCascader({ selectedChannels, selectedAccounts, onChange, disabled }: Props) {
  const { fetchEffectiveChannels, fetchEffectiveAccountsByChannel } = useChannelStore();
  const [channels, setChannels] = useState<Channel[]>([]);
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadChannels = async () => {
      setLoading(true);
      const data = await fetchEffectiveChannels();
      setChannels(data);
      setLoading(false);
    };
    loadChannels();
  }, [fetchEffectiveChannels]);

  useEffect(() => {
    const loadAccounts = async () => {
      if (selectedChannels.length === 0) {
        setAccounts([]);
        // 自动清空已选账户
        if (selectedAccounts.length > 0) {
          onChange(selectedChannels, []);
        }
        return;
      }

      setLoading(true);
      const allAccounts: Account[] = [];
      for (const cid of selectedChannels) {
        const accs = await fetchEffectiveAccountsByChannel(cid);
        allAccounts.push(...accs);
      }
      
      // 去重
      const uniqueAccounts = Array.from(new Map(allAccounts.map(item => [item.id, item])).values());
      setAccounts(uniqueAccounts);
      
      // 校验当前选中的账户是否还在可用列表中
      const validAccountIds = selectedAccounts.filter(id => uniqueAccounts.some(a => a.id === id));
      if (validAccountIds.length !== selectedAccounts.length) {
        onChange(selectedChannels, validAccountIds);
      }
      
      setLoading(false);
    };

    loadAccounts();
  }, [selectedChannels, fetchEffectiveAccountsByChannel]);

  const handleChannelChange = (newChannels: (string | number)[]) => {
    const channelIds = newChannels.map(Number);
    onChange(channelIds, selectedAccounts);
  };

  const handleAccountChange = (newAccounts: (string | number)[]) => {
    const accountIds = newAccounts.map(Number);
    onChange(selectedChannels, accountIds);
  };

  const channelOptions = channels.map(c => ({ value: c.id, label: c.name }));
  const accountOptions = accounts.map(a => ({ value: a.id, label: `${a.accountName} (${a.bankName})` }));

  return (
    <div className="flex space-x-2 w-full">
      <div className="w-1/2">
        <MultiSelect
          options={channelOptions}
          value={selectedChannels}
          onChange={handleChannelChange}
          placeholder="选择渠道"
          disabled={disabled || loading}
        />
      </div>
      <div className="w-1/2">
        <MultiSelect
          options={accountOptions}
          value={selectedAccounts}
          onChange={handleAccountChange}
          placeholder={selectedChannels.length === 0 ? "请先选择渠道" : "选择账户"}
          disabled={disabled || loading || accounts.length === 0}
        />
      </div>
    </div>
  );
}
