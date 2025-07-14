import { defineConfig } from 'astro/config';
import vue from '@astrojs/vue';
import node from '@astrojs/node';

export default defineConfig({
    output: 'server',
    adapter: node({ mode: 'standalone' }),
    integrations: [vue()],
    vite: {
        server: {
            allowedHosts: ['poc-front-2.onrender.com'],
        },
    },
});
