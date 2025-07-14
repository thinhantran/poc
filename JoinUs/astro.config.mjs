import { defineConfig } from 'astro/config';
import vue from '@astrojs/vue';
import staticAdapter from '@astrojs/static';

export default defineConfig({
    output: 'static',
    adapter: staticAdapter(),
    integrations: [vue()],
});
