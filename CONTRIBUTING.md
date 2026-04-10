# Contributing to NexusBlog

Thank you for your interest in contributing to NexusBlog!

## Development Setup

### Prerequisites
- JDK 21
- Node.js 18+
- Maven (or use backend/mvnw)
- npm

### Backend Development

```bash
cd backend

# Run tests
./mvnw test

# Run application
./mvnw spring-boot:run

# Build
./mvnw clean package
```

### Frontend Development

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Code Style

### Backend
- Follow Java coding conventions
- Use Lombok to reduce boilerplate
- Write meaningful Javadoc for public APIs

### Frontend
- Use Vue 3 Composition API
- Follow Vue.js style guide
- Use TypeScript for new features (future enhancement)

## Submitting Changes

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write tests if applicable
5. Submit a pull request

## Getting Help

If you need help, please open an issue on GitHub.
