import { create } from 'zustand';
import axios from 'axios';
import type { Channel, Account } from '../types/channel';

interface CacheItem<T> {
  data: T;
  timestamp: number;
}

interface ChannelState {
  channelsCache: CacheItem<Channel[]> | null;
  accountsByChannelCache: Record<number, CacheItem<Account[]>>;

  fetchEffectiveChannels: () => Promise<Channel[]>;
  fetchEffectiveAccountsByChannel: (channelId: number) => Promise<Account[]>;
  clearCache: () => void;
}

const CACHE_TTL = 5 * 60 * 1000; // 5 minutes

export const useChannelStore = create<ChannelState>((set, get) => ({
  channelsCache: null,
  accountsByChannelCache: {},

  fetchEffectiveChannels: async () => {
    const { channelsCache } = get();
    const now = Date.now();

    if (channelsCache && now - channelsCache.timestamp < CACHE_TTL) {
      return channelsCache.data;
    }

    try {
      const res = await axios.get('/api/finance/channels/effective');
      if (res.data.code === 200) {
        const channels = res.data.data;
        set({ channelsCache: { data: channels, timestamp: now } });
        return channels;
      }
    } catch (e) {
      console.error('Failed to fetch channels', e);
    }
    return [];
  },

  fetchEffectiveAccountsByChannel: async (channelId: number) => {
    const { accountsByChannelCache } = get();
    const now = Date.now();
    const cached = accountsByChannelCache[channelId];

    if (cached && now - cached.timestamp < CACHE_TTL) {
      return cached.data;
    }

    try {
      const res = await axios.get(`/api/finance/channels/${channelId}/accounts`);
      if (res.data.code === 200) {
        const accounts = res.data.data;
        set(state => ({
          accountsByChannelCache: {
            ...state.accountsByChannelCache,
            [channelId]: { data: accounts, timestamp: now }
          }
        }));
        return accounts;
      }
    } catch (e) {
      console.error(`Failed to fetch accounts for channel ${channelId}`, e);
    }
    return [];
  },

  clearCache: () => {
    set({ channelsCache: null, accountsByChannelCache: {} });
  }
}));
