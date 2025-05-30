FROM node:18-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers du projet dans le conteneur
COPY . .

# Installer les dépendances
RUN npm install

# Exposer le port utilisé par Vite
EXPOSE 4321

# Lancer l'application
CMD ["npm", "run", "preview"]