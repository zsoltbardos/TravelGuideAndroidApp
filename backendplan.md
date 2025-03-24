# Backend Plan for Travel Guide App

## Overview

This document outlines the backend architecture, API endpoints, and data models for the Travel Guide App. The backend will be implemented using ASP.NET Core Web API with Entity Framework Core for database operations.

## Technology Stack

- **Framework**: ASP.NET Core Web API (.NET 7.0+)
- **Database**: SQL Server / Azure SQL
- **ORM**: Entity Framework Core
- **Authentication**: JWT-based authentication
- **API Documentation**: Swagger/OpenAPI
- **Deployment**: Azure App Service

## Data Models

### 1. Destination

```csharp
public class Destination
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public string Location { get; set; }
    public float AverageRating { get; set; }
    public List<string> PointsOfInterest { get; set; }
    public List<string> ImageUrls { get; set; }
    public string CoverImageUrl { get; set; }
    public string Region { get; set; }
    public string Category { get; set; }
    public DateTime CreatedAt { get; set; }
    public DateTime UpdatedAt { get; set; }
    
    // Navigation properties
    public virtual ICollection<Review> Reviews { get; set; }
}
```

### 2. Review

```csharp
public class Review
{
    public int Id { get; set; }
    public int DestinationId { get; set; }
    public int? UserId { get; set; }
    public string ReviewerName { get; set; }
    public string ReviewText { get; set; }
    public float Rating { get; set; }
    public DateTime ReviewDate { get; set; }
    
    // Navigation properties
    public virtual Destination Destination { get; set; }
    public virtual User User { get; set; }
}
```

### 3. User

```csharp
public class User
{
    public int Id { get; set; }
    public string Username { get; set; }
    public string Email { get; set; }
    public string PasswordHash { get; set; }
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public DateTime CreatedAt { get; set; }
    public DateTime LastLogin { get; set; }
    
    // Navigation properties
    public virtual ICollection<Review> Reviews { get; set; }
    public virtual ICollection<Destination> FavoriteDestinations { get; set; }
}
```

## API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/auth/register` | Register a new user | `{username, email, password, firstName, lastName}` | `{userId, username, token}` |
| POST | `/api/auth/login` | Authenticate a user | `{email, password}` | `{userId, username, token}` |
| POST | `/api/auth/refresh-token` | Refresh JWT token | `{refreshToken}` | `{token, refreshToken}` |
| GET | `/api/auth/user` | Get authenticated user details | _none_ | User object |
| PUT | `/api/auth/user` | Update user details | User object | Updated user object |
| POST | `/api/auth/logout` | Logout a user | _none_ | Success message |

### Destinations Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/destinations` | Get all destinations (with pagination) | _none_ | Array of destination objects |
| GET | `/api/destinations/{id}` | Get destination by ID | _none_ | Destination object |
| POST | `/api/destinations` | Create a new destination | Destination object | Created destination |
| PUT | `/api/destinations/{id}` | Update a destination | Destination object | Updated destination |
| DELETE | `/api/destinations/{id}` | Delete a destination | _none_ | Success message |
| GET | `/api/destinations/search` | Search destinations | Query params: `q`, `region`, `category`, `minRating` | Array of destination objects |
| GET | `/api/destinations/popular` | Get popular destinations | _none_ | Array of destination objects |
| GET | `/api/destinations/categories` | Get all destination categories | _none_ | Array of category strings |
| GET | `/api/destinations/regions` | Get all destination regions | _none_ | Array of region strings |

### Reviews Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/reviews` | Get all reviews (with pagination) | _none_ | Array of review objects |
| GET | `/api/reviews/{id}` | Get review by ID | _none_ | Review object |
| GET | `/api/destinations/{destinationId}/reviews` | Get reviews for a destination | _none_ | Array of review objects |
| POST | `/api/destinations/{destinationId}/reviews` | Add a review for a destination | Review object | Created review |
| PUT | `/api/reviews/{id}` | Update a review | Review object | Updated review |
| DELETE | `/api/reviews/{id}` | Delete a review | _none_ | Success message |

### User Profile Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/users/{id}/reviews` | Get reviews by a user | _none_ | Array of review objects |
| GET | `/api/users/{id}/favorites` | Get user's favorite destinations | _none_ | Array of destination objects |
| POST | `/api/users/{id}/favorites/{destinationId}` | Add destination to favorites | _none_ | Success message |
| DELETE | `/api/users/{id}/favorites/{destinationId}` | Remove destination from favorites | _none_ | Success message |

### Images Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/destinations/{destinationId}/images` | Get images for a destination | _none_ | Array of image URLs |
| POST | `/api/destinations/{destinationId}/images` | Upload an image for a destination | Form data with image | Image URL |
| DELETE | `/api/destinations/{destinationId}/images/{imageId}` | Delete an image | _none_ | Success message |

## Query Parameters

Most GET endpoints will support the following query parameters:

- `page`: Page number (default: 1)
- `pageSize`: Number of items per page (default: 10)
- `sortBy`: Field to sort by
- `sortOrder`: Sort order (asc/desc)

## Authentication and Authorization

- All endpoints except GET `/api/destinations/*` and GET `/api/reviews/*` require authentication
- Only admin users can create, update, or delete destinations
- Users can only update or delete their own reviews
- Admin dashboard endpoints require admin role

## Rate Limiting

- API will implement rate limiting to prevent abuse
- Anonymous users: 60 requests per minute
- Authenticated users: 120 requests per minute

## Caching Strategy

- GET requests for destinations and reviews will be cached
- Cache invalidation will occur when data is updated

## Internationalization

- API will support content in multiple languages
- Clients can specify preferred language using `Accept-Language` header
- Responses will include translated content when available

## Implementation Phases

### Phase 1: Core API
- Basic authentication
- CRUD operations for destinations
- CRUD operations for reviews

### Phase 2: Enhanced Features
- User profiles and favorites
- Image uploads
- Search functionality

### Phase 3: Optimization
- Caching
- Rate limiting
- Content internationalization 