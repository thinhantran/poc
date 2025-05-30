import { defineCollection, z } from 'astro:content';

const events = defineCollection({
    schema: z.object({
        id: z.number(),
        title: z.string(),
        description: z.string(),
        date: z.string(),
        time: z.string().optional(),
        location: z.string(),
        price: z.number().optional(),
        total: z.number().optional(),
        image: z.string(),
        type: z.string(),
    }),
});

export const collections = {
    events,
};
