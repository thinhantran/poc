const isBrowser = typeof window !== 'undefined';

export const API_BASE = isBrowser
    ? "http://localhost:8080"
    : "http://backend:8080";