## ADDED Requirements

### Requirement: System displays article list on homepage
The system SHALL display a list of published articles on the homepage.

#### Scenario: Homepage shows article cards
- **WHEN** user navigates to the homepage
- **THEN** system displays a list of articles
- **AND** each article shows title, category, tags, and creation date
- **AND** articles are sorted by creation date in descending order
- **AND** each article has a link to the article detail page

#### Scenario: Empty article list
- **WHEN** there are no published articles
- **THEN** system displays a message indicating no articles are available

#### Scenario: Article list pagination
- **WHEN** there are more than 10 published articles
- **THEN** system displays pagination controls
- **AND** system shows 10 articles per page

### Requirement: System renders Markdown content
The system SHALL render Markdown content as HTML in the article detail view.

#### Scenario: Render Markdown with basic formatting
- **WHEN** user views an article containing Markdown formatting
- **THEN** system converts Markdown to HTML
- **AND** system displays headers, paragraphs, lists, and links correctly

#### Scenario: Render Markdown with code blocks
- **WHEN** article contains code blocks with syntax highlighting
- **THEN** system renders code blocks with appropriate syntax highlighting

### Requirement: System displays article detail page
The system SHALL display the full content of an article on a dedicated detail page.

#### Scenario: View article details
- **WHEN** user clicks on an article title or "Read More" link
- **THEN** system displays the article detail page
- **AND** system shows article title, category, tags, creation and update dates
- **AND** system renders the Markdown content as HTML

#### Scenario: View non-existent article
- **WHEN** user navigates to an article detail page with invalid ID
- **THEN** system displays a 404 Not Found error page
- **AND** system provides a link to return to the homepage

#### Scenario: Navigate back to article list
- **WHEN** user is viewing an article detail page
- **THEN** system provides navigation options to return to the homepage
