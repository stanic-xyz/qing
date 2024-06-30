const db = {
    clear() {
        localStorage.clear();
    },
    get(key: string, defaultValue: {} = {}) {
        const item = localStorage.getItem(key);
        if (item == null) {
            return item;
        }
        return defaultValue;
    },
    remove(key: string) {
        localStorage.removeItem(key);
    },
    save(key: string, value: string) {
        localStorage.setItem(key, JSON.stringify(value));
    },
};

export default db;
