# Travel Guide API Documentation

This document provides comprehensive documentation for the Travel Guide API, which serves as the backend for the Travel Guide mobile application.

## Base URL

The API is accessible at `http://localhost:5078` when running locally.

## Authentication

The API uses JWT (JSON Web Token) based authentication. Protected endpoints require an `Authorization` header with a valid token.

### Authentication Endpoints

#### Register a New User

- **URL:** `/api/auth/register`
- **Method:** `POST`
- **Authentication Required:** No
- **Request Body:**
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string",
    "firstName": "string",
    "lastName": "string"
  }
  ```
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "User registered successfully",
    "data": {
      "userId": 1,
      "username": "string",
      "token": "string",
      "refreshToken": "string"
    },
    "errors": null
  }
  ```

#### Login

- **URL:** `/api/auth/login`
- **Method:** `POST`
- **Authentication Required:** No
- **Request Body:**
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Login successful",
    "data": {
      "userId": 1,
      "username": "string",
      "token": "string",
      "refreshToken": "string"
    },
    "errors": null
  }
  ```

#### Refresh Token

- **URL:** `/api/auth/refresh-token`
- **Method:** `POST`
- **Authentication Required:** No
- **Request Body:**
  ```json
  {
    "refreshToken": "string"
  }
  ```
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Token refreshed successfully",
    "data": {
      "userId": 1,
      "username": "string",
      "token": "string",
      "refreshToken": "string"
    },
    "errors": null
  }
  ```

## Destinations

Destinations represent travel locations with details such as description, images, and points of interest.

### Destination Endpoints

#### Get All Destinations

- **URL:** `/api/destinations`
- **Method:** `GET`
- **Authentication Required:** No
- **Query Parameters:**
  - `pageNumber`: Page number for pagination (default: 1)
  - `pageSize`: Number of items per page (default: 10, max: 50)
  - `sortBy`: Field to sort by (default: "Id")
  - `sortOrder`: Sort order "asc" or "desc" (default: "asc")
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Operation completed successfully",
    "data": {
      "items": [
        {
          "id": 1,
          "name": "string",
          "description": "string",
          "location": "string",
          "averageRating": 0,
          "pointsOfInterest": ["string"],
          "imageUrls": ["string"],
          "coverImageUrl": "string",
          "region": "string",
          "category": "string",
          "createdAt": "2023-01-01T00:00:00Z",
          "updatedAt": "2023-01-01T00:00:00Z"
        }
      ],
      "totalCount": 1,
      "pageNumber": 1,
      "pageSize": 10,
      "totalPages": 1,
      "hasPrevious": false,
      "hasNext": false
    },
    "errors": null
  }
  ```

#### Get Destination by ID

- **URL:** `/api/destinations/{id}`
- **Method:** `GET`
- **Authentication Required:** No
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Operation completed successfully",
    "data": {
      "id": 1,
      "name": "string",
      "description": "string",
      "location": "string",
      "averageRating": 0,
      "pointsOfInterest": ["string"],
      "imageUrls": ["string"],
      "coverImageUrl": "string",
      "region": "string",
      "category": "string",
      "createdAt": "2023-01-01T00:00:00Z",
      "updatedAt": "2023-01-01T00:00:00Z"
    },
    "errors": null
  }
  ```

#### Create a New Destination

- **URL:** `/api/destinations`
- **Method:** `POST`
- **Authentication Required:** Yes
- **Request Body:**
  ```json
  {
    "name": "string",
    "description": "string",
    "location": "string",
    "pointsOfInterest": ["string"],
    "imageUrls": ["string"],
    "coverImageUrl": "string",
    "region": "string",
    "category": "string"
  }
  ```
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Destination created successfully",
    "data": {
      "id": 1,
      "name": "string",
      "description": "string",
      "location": "string",
      "averageRating": 0,
      "pointsOfInterest": ["string"],
      "imageUrls": ["string"],
      "coverImageUrl": "string",
      "region": "string",
      "category": "string",
      "createdAt": "2023-01-01T00:00:00Z",
      "updatedAt": "2023-01-01T00:00:00Z"
    },
    "errors": null
  }
  ```

#### Update a Destination

- **URL:** `/api/destinations/{id}`
- **Method:** `PUT`
- **Authentication Required:** Yes
- **Request Body:**
  ```json
  {
    "name": "string",
    "description": "string",
    "location": "string",
    "pointsOfInterest": ["string"],
    "imageUrls": ["string"],
    "coverImageUrl": "string",
    "region": "string",
    "category": "string"
  }
  ```
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Destination updated successfully",
    "data": {
      "id": 1,
      "name": "string",
      "description": "string",
      "location": "string",
      "averageRating": 0,
      "pointsOfInterest": ["string"],
      "imageUrls": ["string"],
      "coverImageUrl": "string",
      "region": "string",
      "category": "string",
      "createdAt": "2023-01-01T00:00:00Z",
      "updatedAt": "2023-01-01T00:00:00Z"
    },
    "errors": null
  }
  ```

#### Delete a Destination

- **URL:** `/api/destinations/{id}`
- **Method:** `DELETE`
- **Authentication Required:** Yes
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Destination deleted successfully",
    "data": true,
    "errors": null
  }
  ```

#### Search Destinations

- **URL:** `/api/destinations/search`
- **Method:** `GET`
- **Authentication Required:** No
- **Query Parameters:**
  - `q`: Search query string
  - `region`: Filter by region
  - `category`: Filter by category
  - `minRating`: Filter by minimum rating
  - Pagination parameters (pageNumber, pageSize, sortBy, sortOrder)
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Operation completed successfully",
    "data": {
      "items": [
        {
          "id": 1,
          "name": "string",
          "description": "string",
          "location": "string",
          "averageRating": 0,
          "pointsOfInterest": ["string"],
          "imageUrls": ["string"],
          "coverImageUrl": "string",
          "region": "string",
          "category": "string",
          "createdAt": "2023-01-01T00:00:00Z",
          "updatedAt": "2023-01-01T00:00:00Z"
        }
      ],
      "totalCount": 1,
      "pageNumber": 1,
      "pageSize": 10,
      "totalPages": 1,
      "hasPrevious": false,
      "hasNext": false
    },
    "errors": null
  }
  ```

#### Get Popular Destinations

- **URL:** `/api/destinations/popular`
- **Method:** `GET`
- **Authentication Required:** No
- **Query Parameters:**
  - `count`: Number of destinations to return (default: 5)
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Operation completed successfully",
    "data": [
      {
        "id": 1,
        "name": "string",
        "description": "string",
        "location": "string",
        "averageRating": 0,
        "pointsOfInterest": ["string"],
        "imageUrls": ["string"],
        "coverImageUrl": "string",
        "region": "string",
        "category": "string",
        "createdAt": "2023-01-01T00:00:00Z",
        "updatedAt": "2023-01-01T00:00:00Z"
      }
    ],
    "errors": null
  }
  ```

## Reviews

Reviews are user feedback on destinations, including ratings and comments.

### Review Endpoints

#### Get All Reviews

- **URL:** `/api/reviews`
- **Method:** `GET`
- **Authentication Required:** No
- **Query Parameters:** Pagination parameters (pageNumber, pageSize, sortBy, sortOrder)
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Operation completed successfully",
    "data": {
      "items": [
        {
          "id": 1,
          "destinationId": 1,
          "userId": 1,
          "reviewerName": "string",
          "reviewText": "string",
          "rating": 5.0,
          "reviewDate": "2023-01-01T00:00:00Z",
          "destinationName": "string"
        }
      ],
      "totalCount": 1,
      "pageNumber": 1,
      "pageSize": 10,
      "totalPages": 1,
      "hasPrevious": false,
      "hasNext": false
    },
    "errors": null
  }
  ```

#### Get Review by ID

- **URL:** `/api/reviews/{id}`
- **Method:** `GET`
- **Authentication Required:** No
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Operation completed successfully",
    "data": {
      "id": 1,
      "destinationId": 1,
      "userId": 1,
      "reviewerName": "string",
      "reviewText": "string",
      "rating": 5.0,
      "reviewDate": "2023-01-01T00:00:00Z",
      "destinationName": "string"
    },
    "errors": null
  }
  ```

#### Get Reviews for a Destination

- **URL:** `/api/destinations/{destinationId}/reviews`
- **Method:** `GET`
- **Authentication Required:** No
- **Query Parameters:** Pagination parameters (pageNumber, pageSize, sortBy, sortOrder)
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Operation completed successfully",
    "data": {
      "items": [
        {
          "id": 1,
          "destinationId": 1,
          "userId": 1,
          "reviewerName": "string",
          "reviewText": "string",
          "rating": 5.0,
          "reviewDate": "2023-01-01T00:00:00Z",
          "destinationName": "string"
        }
      ],
      "totalCount": 1,
      "pageNumber": 1,
      "pageSize": 10,
      "totalPages": 1,
      "hasPrevious": false,
      "hasNext": false
    },
    "errors": null
  }
  ```

#### Add a Review for a Destination

- **URL:** `/api/destinations/{destinationId}/reviews`
- **Method:** `POST`
- **Authentication Required:** No (but authenticated users will have their reviews linked to their account)
- **Request Body:**
  ```json
  {
    "reviewerName": "string", // Only used for anonymous reviews
    "reviewText": "string",
    "rating": 5.0
  }
  ```
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Review added successfully",
    "data": {
      "id": 1,
      "destinationId": 1,
      "userId": 1,
      "reviewerName": "string",
      "reviewText": "string",
      "rating": 5.0,
      "reviewDate": "2023-01-01T00:00:00Z",
      "destinationName": "string"
    },
    "errors": null
  }
  ```

#### Update a Review

- **URL:** `/api/reviews/{id}`
- **Method:** `PUT`
- **Authentication Required:** Yes (users can only update their own reviews)
- **Request Body:**
  ```json
  {
    "reviewText": "string",
    "rating": 5.0
  }
  ```
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Review updated successfully",
    "data": {
      "id": 1,
      "destinationId": 1,
      "userId": 1,
      "reviewerName": "string",
      "reviewText": "string",
      "rating": 5.0,
      "reviewDate": "2023-01-01T00:00:00Z",
      "destinationName": "string"
    },
    "errors": null
  }
  ```

#### Delete a Review

- **URL:** `/api/reviews/{id}`
- **Method:** `DELETE`
- **Authentication Required:** Yes (users can only delete their own reviews)
- **Success Response:**
  ```json
  {
    "success": true,
    "message": "Review deleted successfully",
    "data": true,
    "errors": null
  }
  ```

## Response Format

All API responses follow a consistent structure:

```json
{
  "success": true|false,
  "message": "string",
  "data": object|null,
  "errors": ["string"]|null
}
```

### Paginated Responses

Endpoints that return lists of items use pagination with the following structure:

```json
{
  "items": [],
  "totalCount": 0,
  "pageNumber": 1,
  "pageSize": 10,
  "totalPages": 0,
  "hasPrevious": false,
  "hasNext": false
}
```

## Error Handling

The API returns HTTP status codes and error messages in the following format:

```json
{
  "success": false,
  "message": "Error message",
  "data": null,
  "errors": ["Detailed error 1", "Detailed error 2"]
}
```

Common HTTP status codes:
- `200 OK`: Request succeeded
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Authenticated but not authorized
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Authentication Flow

1. Register a user account or login with existing credentials
2. Store the returned JWT token and refresh token
3. Include the JWT token in the Authorization header for requests to protected endpoints:
   ```
   Authorization: Bearer {token}
   ```
4. When the JWT token expires (after 2 hours), use the refresh token to get a new JWT token

## Implementation Notes

- The JWT token has a 2-hour lifetime
- Refresh tokens are valid for 7 days
- Images are stored as URLs (the actual file storage is handled separately)
- Rating values range from 1.0 to 5.0
- Lists (like pointsOfInterest, imageUrls) are stored as JSON in the database but exposed as proper arrays in the API
``` 