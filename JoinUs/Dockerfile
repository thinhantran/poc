FROM node:18-alpine

WORKDIR /app

COPY . .

RUN npm install

EXPOSE 4321

CMD ["npm", "run", "preview"]