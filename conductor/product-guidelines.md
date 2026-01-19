# Product Guidelines - Xinco DMS

## Tone of Voice
- **Professional & Formal:** All documentation and UI text must be clear, concise, and objective. Use professional language that instills confidence in corporate and regulated users. Avoid jargon where possible, but prioritize precision in technical contexts.

## Visual Identity & UI Principles
- **Clean & Minimalist:** The modernized interface should prioritize white space and clarity. Design for a low cognitive load by presenting information hierarchically and avoiding unnecessary visual clutter.
- **Action-Oriented:** UI elements should clearly indicate their purpose and guide users toward their next task.

## Technical Standards
- **Self-Documenting Code:** Prioritize clear, descriptive naming for classes, methods, and variables. Structure code logically so that its purpose is evident without excessive commenting.
- **Rigorous API Documentation:** All web services must be fully documented using JavaDoc and OpenAPI/Swagger specifications to facilitate integration and maintenance.
- **Strict Type Safety:** Fully leverage the type systems of modern Java and TypeScript to ensure code correctness and catch potential errors at compile time.

## Error Handling & Feedback
- **Informative & Actionable:** Error messages must clearly state what occurred and, whenever possible, provide specific steps for resolution.
- **Unobtrusive Notifications:** Use toast messages or subtle status indicators for non-critical updates (e.g., "File saved") to maintain user flow.
- **Fail-Safe Operations:** Systems must be designed to handle failures gracefully, ensuring data integrity is never compromised and the user is kept informed of the system state.

## Development Principles
- **Test-Driven Development (TDD):** Implement a rigorous TDD approach. Write unit and integration tests before writing implementation code to clarify requirements and ensure long-term stability.
- **Consistency Above All:** Adhere strictly to the established code style guides and architectural patterns to ensure the project remains maintainable by any contributor.
