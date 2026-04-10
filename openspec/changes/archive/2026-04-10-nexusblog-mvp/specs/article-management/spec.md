## ADDED Requirements

### Requirement: System allows article creation
The system SHALL allow authenticated users to create new blog articles.

#### Scenario: Create article with all fields
- **WHEN** authenticated user submits article form with title, Markdown content, category, and tags
- **THEN** system creates a new article record in the database
- **AND** system sets creation timestamp to current time
- **AND** system sets update timestamp to creation time
- **AND** user is redirected to the article list

#### Scenario: Create article requires authentication
- **WHEN** unauthenticated user attempts to create an article
- **THEN** system denies access
- **AND** system redirects user to the login page

### Requirement: System allows article reading
The system SHALL allow users to retrieve articles for display.

#### Scenario: Retrieve article list
- **WHEN** user requests the article list
- **THEN** system returns all published articles with title, category, tags, and timestamps
- **AND** articles are sorted by creation date in descending order

#### Scenario: Retrieve single article
- **WHEN** user requests a specific article by ID
- **THEN** system returns the full article with title, Markdown content, category, tags, and timestamps

#### Scenario: Retrieve non-existent article
- **WHEN** user requests an article with invalid ID
- **THEN** system returns a 404 Not Found error

### Requirement: System allows article updating
The system SHALL allow authenticated users to update existing articles.

#### Scenario: Update article fields
- **WHEN** authenticated user modifies and saves an existing article
- **THEN** system updates the article record in the database
- **AND** system updates the update timestamp to current time
- **AND** all modified fields are persisted

#### Scenario: Update requires authentication
- **WHEN** unauthenticated user attempts to update an article
- **THEN** system denies access
- **AND** system redirects user to the login page

#### Scenario: Update non-existent article
- **WHEN** authenticated user attempts to update a non-existent article
- **THEN** system returns a 404 Not Found error

### Requirement: System allows article deletion
The system SHALL allow authenticated users to delete articles.

#### Scenario: Delete existing article
- **WHEN** authenticated user confirms deletion of an existing article
- **THEN** system removes the article record from the database
- **AND** user is redirected to the article list

#### Scenario: Delete requires authentication
- **WHEN** unauthenticated user attempts to delete an article
- **THEN** system denies access
- **AND** system redirects user to the login page

#### Scenario: Delete non-existent article
- **WHEN** authenticated user attempts to delete a non-existent article
- **THEN** system returns a 404 Not Found error
