const isBrowser = typeof window !== 'undefined';

export const API_BASE = isBrowser
    ? import.meta.env.PUBLIC_API_BASE_BROWSER
    : import.meta.env.PUBLIC_API_BASE_SERVER;
