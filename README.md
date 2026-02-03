<div align="center">
  <a href="https://github.com/rykr0/handkerchief">
    <img src="sample_images/twitter_x_bird.png" alt="Handkerchief logo" width="120">
  </a>

  <h1>Handkerchief</h1>

  <p>A full-featured Twitter clone built with React and Spring Boot microservices.</p>

  ![React](https://img.shields.io/badge/react-%2320232a.svg?style=flat-square&logo=react&logoColor=%2361DAFB)
  ![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=flat-square&logo=typescript&logoColor=white)
  ![Java](https://img.shields.io/badge/java_17-%23ED8B00.svg?style=flat-square&logo=openjdk&logoColor=white)
  ![Spring Boot](https://img.shields.io/badge/spring_boot-%236DB33F.svg?style=flat-square&logo=spring&logoColor=white)
  ![PostgreSQL](https://img.shields.io/badge/postgres-%23316192.svg?style=flat-square&logo=postgresql&logoColor=white)
  ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat-square&logo=docker&logoColor=white)
  ![Apache Kafka](https://img.shields.io/badge/kafka-%23231F20.svg?style=flat-square&logo=apachekafka&logoColor=white)
  ![AWS](https://img.shields.io/badge/AWS_S3-%23FF9900.svg?style=flat-square&logo=amazon-aws&logoColor=white)
</div>

---

![Home page](https://i.ibb.co/vBsQTZT/1-Preview.jpg)

## Table of Contents

- [Architecture](#architecture)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Infrastructure Setup](#1-infrastructure-setup)
  - [Database Setup](#2-database-setup)
  - [AWS S3 Setup](#3-aws-s3-setup)
  - [API Keys](#4-api-keys)
  - [Backend](#5-backend)
  - [Frontend](#6-frontend)
- [Why I Made This](#why-i-made-this)

## Architecture

Handkerchief uses a **microservices architecture** with 13 independently deployable services, coordinated through service discovery and event-driven messaging.

```
                         ┌─────────────┐
                         │   Frontend   │ :3000
                         │  React + TS  │
                         └──────┬───────┘
                                │
                         ┌──────▼───────┐
                         │ API Gateway  │ :8000
                         │  JWT Auth    │
                         └──────┬───────┘
                                │
              ┌─────────────────┼─────────────────┐
              │                 │                  │
     ┌────────▼──┐    ┌────────▼──┐     ┌─────────▼─────┐
     │  Eureka   │    │  Config   │     │  Application  │
     │  Server   │    │  Server   │     │   Services    │
     │  :8761    │    │  :8888    │     │  (see below)  │
     └───────────┘    └───────────┘     └───────────────┘
```

### Application Services

| Service | Port | Description |
|---|---|---|
| `user-service` | 8001 | Accounts, profiles, follow/block/mute |
| `tweet-service` | 8002 | Tweets, likes, retweets, replies, quotes, scheduling |
| `topic-service` | 8003 | Trending topics |
| `tag-service` | 8004 | Hashtags and tag trends |
| `lists-service` | 8005 | User-curated lists |
| `chat-service` | 8006 | Direct messaging |
| `notification-service` | 8007 | Notification management |
| `email-service` | 8020 | Email dispatch via Gmail |
| `image-service` | 8021 | Image upload to AWS S3 |
| `websocket-service` | 8022 | Real-time WebSocket delivery |

### Supporting Infrastructure

| Service | Port | Purpose |
|---|---|---|
| PostgreSQL | 5432 | Primary database (schemas: `user`, `tweet`, `chat`, `lists`, `notification`, `tag`, `topic`) |
| pgAdmin | 5050 | Database management UI |
| Kafka + Zookeeper | 9092 / 2181 | Event streaming between services |
| RabbitMQ | 5672 / 15672 | AMQP messaging |
| Zipkin | 9411 | Distributed tracing |
| Prometheus | 9090 | Metrics collection |
| Grafana | 3001 | Monitoring dashboards |

## Features

### Implemented

- **Tweets** — create, like, retweet, reply, quote, schedule, delete, send via DM, bookmark
- **Media** — image uploads (S3), polls, link previews, YouTube video embeds
- **Users** — follow, block, mute, profile editing
- **Lists** — create, edit, follow, pin, manage members
- **Direct Messages** — real-time chat via WebSocket
- **Notifications** — real-time with customizable email notification settings
- **Themes** — dark mode and multiple color schemes

### Not Yet Implemented

- Search
- User mentions (`@username`)
- Tweet threads

## Getting Started

### Prerequisites

| Requirement | Link |
|---|---|
| Java 17 | [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) |
| Maven | [Install guide](https://www.baeldung.com/install-maven-on-windows-linux-mac) |
| Node.js + npm | [Install guide](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) |
| PostgreSQL | [Download](https://www.postgresql.org/download/) |
| Docker | [Download](https://www.docker.com/products/docker-desktop/) |
| IntelliJ IDEA (recommended) | [Download](https://www.jetbrains.com/idea/) |

You will also need accounts for:
- **AWS** — S3 bucket for image storage
- **Google Cloud** — YouTube Data API key
- **Gmail** — for sending email notifications

### 1. Infrastructure Setup

Start the infrastructure containers with Docker Compose:

```bash
docker compose up -d postgres pgadmin zipkin rabbitmq zookeeper broker
```

### 2. Database Setup

1. Open pgAdmin at http://localhost:5050
2. Connect to the PostgreSQL server (`postgres:5432`, user: `postgres`, password: `root`)
3. Create these databases: `user`, `tweet`, `chat`, `lists`, `notification`, `tag`, `topic`

### 3. AWS S3 Setup

1. Create a new S3 bucket and set its access to **public**
2. Add a [public read access policy](https://docs.aws.amazon.com/AmazonS3/latest/userguide/access-policy-language-overview.html) to the bucket
3. Generate [AWS access keys](https://supsystic.com/documentation/id-secret-access-key-amazon-s3/)
4. Add the bucket name, access key, and secret key to `config-server/src/main/resources/config/image-service.yml`

### 4. API Keys

**YouTube Data API:**
1. Go to the [Google Cloud Console](https://console.cloud.google.com/) and generate a [YouTube Data API key](https://developers.google.com/youtube/v3/getting-started#before-you-start)
2. Add the key to `config-server/src/main/resources/config/tweet-service.yml`

**Email (Gmail):**
1. Add your Gmail address and an [app password](https://support.google.com/accounts/answer/185833) to `config-server/src/main/resources/config/email-service.yml`

### 5. Backend

1. In IntelliJ, install the **Lombok** plugin (`Settings > Plugins`)
2. Set the project SDK to **Java 17** (`Project Structure > Project`)
3. Build with Maven
4. Run the Spring Boot services **in this order**:
   1. `eureka-server`
   2. `config-server`
   3. `api-gateway`
   4. `user-service`
   5. All remaining services (any order)

### 6. Frontend

```bash
cd frontend
npm install
npm start
```

Open http://localhost:3000/home

> **Default credentials:**
> Email: `user2024@gmail.com`
> Password: `qwerty123`

---

## Why I Made This

X sucks.

Here are principles you should live by:
- Don't instagramify. You don't need to compete against TikTok with a variant of reels.
- Don't have bloated UX. "The everything app" kills lightweight legacies.
- Don't be a Twitter-UI-inspired derivative with no conception of content discovery.
- Don't take government subsidies, lest you are making a honeypot.
- Try not to have your two largest content ecosystems be politics and porn, given their controversial and non-commercial nature.
- Don't rely on decades-old techno-humanist marketing ploys, as the lowest-common-denominator in society does not use technology.
- And, most stereotypically, above all else: **the network that generates culture and promotes content discovery controls the web.**

God died in the 1800s. And now X is on life support.

Cheers.
