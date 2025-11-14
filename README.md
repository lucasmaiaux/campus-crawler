# Campus Crawler

Interface moderne pour jeu de dungeon crawler RPG. Explorez les donjons, combattez des ennemis et collectez des trésors!  

Disponible ici : https://campuscrawler.lucas-maiaux.fr/

## À propos

Campus Crawler est un projet personnel que j'ai créé pour approfondir mes compétences sur plusieurs technos que j'apprécie et qui m'intéressent. L'idée était de construire quelque chose de concret en combinant un front moderne, une API solide et une infrastructure complète.

**Stack technique :**
- **Frontend** : React
- **Backend** : Spring Boot (Java)
- **Base de données** : MySQL
- **Déploiement** : Docker + VPS avec Nginx

## Architecture du projet

Le projet est organisé en monorepo pour faciliter le développement :
```
CampusCrawler/
├── backend/                          # API Spring Boot
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile                    # Dockerfile backend
│   ├── application.properties           # Config de base
│   ├── application-dev.properties       # Config developpement local
│   ├── application.prod-test.properties # Config production local
│   └── application-prod.properties      # Config production
│
├── frontend/                       # React App (Interface web graphique)
│   ├── src/
│   ├── package.json
│   ├── Dockerfile                  # Dockerfile frontend
│   ├── .env.development            # Variables développement   
│   └── .env.production             # Variables production
│
├── docker/                         # Configuration Docker
│   ├── nginx/
│   │   ├── nginx.prod-test.confg   # Config nginx (HTTP seulement)
│   │   └── nginx.prod.conf         # Config nginx (production : HTTPS + SSL)
│   └── mysql/
│       └── init.sql                # Script d'initialisation MySQL (optionnel)
│
├── docker-compose.yml           # BASE commune (MySQL, backend, frontend)
├── docker-compose.dev.yml       # Override DEV (ports exposés, volumes reload)
├── docker-compose.prod-test.yml # Override PROD LOCAL (sans SSL) 
├── docker-compose.prod.yml      # Override PROD VPS (avec SSL)
└── .env.prod                       # Variables prod (non commit)
```

### Environnements

J'ai mis en place trois environnements distincts :

- **dev** : Développement local
- **prod-test** : Test en local de la config production (sans SSL)
- **prod** : Déploiement sur VPS avec HTTPS

## Ce que j'ai appris

Ce projet m'a permis de :
- Maîtriser Docker et Docker Compose pour gérer des environnements complexes
- Implémenter une architecture REST propre avec Spring Boot
- Gérer différents environnements (dev/prod) de manière scalable
- Déployer et maintenir une app sur un serveur distant

---

*Projet personnel réalisé pour apprendre et expérimenter.*
