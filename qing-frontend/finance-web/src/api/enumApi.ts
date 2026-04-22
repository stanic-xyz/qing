import axios from 'axios';

export const fetchAllEnums = () => axios.get('/enumDict/all');
export const fetchEnumByType = (type: string) => axios.get(`/enumDict/${type}`);
