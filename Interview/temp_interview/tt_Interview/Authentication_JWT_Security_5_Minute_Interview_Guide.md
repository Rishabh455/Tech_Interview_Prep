# Authentication, JWT & REST API Security — 5-Minute Interview Guide

> **Goal:** Use this as a spoken interview answer. It is designed to cover the four questions in a connected, systematic flow.  
> **Important:** Keep only the implementation details that are true for your project. If you used a different token-storage or validation approach, replace that line before the interview.

## Questions this guide covers

1. Have you implemented authentication in your application? If so, how did you implement it, and which technologies did you use?
2. Where is a JWT token validated, and where is it stored in your application?
3. What is the difference between a session token and an ID token?
4. How do you secure a REST API in Spring Boot?

Related follow-up questions:
5. What is the difference between authentication and authorization?
6. What are the parts of a JWT?
7. How does a Spring Security filter chain validate a JWT?
8. What are access tokens and refresh tokens?
9. Where should a token be stored on the frontend?
10. How do you handle token expiry and logout?
11. How do you protect against CSRF, XSS, and CORS issues?
12. How do you implement role-based access control?
13. What is the difference between a signed JWT and an encrypted token?
14. How do you secure service-to-service communication?
15. How would you debug a 401 Unauthorized versus a 403 Forbidden response?

---

## The 5-minute spoken answer

### 1. Start with the overall flow

“Yes, I understand authentication and API security as a complete request flow. Authentication verifies **who the user is**, while authorization checks **what the user is allowed to do**.

In a typical Spring Boot application, the user first logs in using valid credentials. The identity provider or authentication service verifies those credentials. If authentication succeeds, the client receives a token, commonly an access token in a JWT format. The frontend then sends that token in the `Authorization` header as a Bearer token when calling protected REST APIs.”

### 2. Explain JWT simply

“JWT stands for JSON Web Token. A JWT commonly has three parts: **header, payload, and signature**.

- The **header** identifies the token type and signing algorithm.
- The **payload** contains claims, such as subject or user ID, issuer, audience, expiry time, and possibly roles or scopes.
- The **signature** helps the server verify that the token has not been altered.

A JWT payload is usually encoded, not encrypted, so we should not put passwords or sensitive personal data in it.”

### 3. Where is the token validated?

“When a request reaches a protected Spring Boot endpoint, Spring Security’s filter chain processes the request before it reaches the controller. A JWT authentication filter or resource-server support extracts the Bearer token and validates it.

Validation typically includes checking the signature, expiry time, issuer, and audience where applicable. If validation succeeds, Spring Security establishes the authenticated security context, and the request proceeds to authorization checks. If the token is missing or invalid, the request is generally rejected with `401 Unauthorized`. If the user is authenticated but does not have the required permission, the response is generally `403 Forbidden`.

So, token validation should happen on the trusted backend or API gateway/resource server—not only in Angular. Frontend checks are useful for user experience, but they are not a security boundary.”

### 4. Where should a token be stored?

“Token storage depends on the application’s security architecture.

For browser applications, an `HttpOnly`, `Secure`, appropriately configured `SameSite` cookie can reduce the risk of JavaScript stealing the token, although cookie-based authentication requires proper CSRF protection. If an application stores a token in browser storage such as `localStorage`, JavaScript can access it, so an XSS vulnerability can expose it. Therefore, I would follow the organization’s approved architecture, keep access tokens short-lived, and avoid storing sensitive information in the token.

On the backend, a stateless JWT is not necessarily stored in a server-side session. The server validates the token on each request. However, refresh-token records, revocation data, or session information may be stored server-side when the design requires it.”

### 5. Session token versus ID token

“A **session token or session ID** identifies a server-managed login session. The server typically keeps session state and uses the session ID to look up that state.

An **ID token**, commonly used in OpenID Connect, tells the client about the authenticated user and is intended for the client application. It is not the same as an access token: an **access token** is presented to an API to access protected resources.

A JWT is a token format; it does not automatically mean that a token is an ID token or an access token. The token’s purpose is determined by the protocol and its claims.”

### 6. How do you secure REST APIs in Spring Boot?

“I would secure REST APIs in layers:

1. **Authentication:** Use Spring Security with an approved identity provider or authentication mechanism.
2. **Token validation:** Validate JWT signature and relevant claims, including expiry, issuer, and audience as appropriate.
3. **Authorization:** Apply role- or authority-based access control using request rules and, where useful, method-level security such as `@PreAuthorize`.
4. **Transport security:** Use HTTPS/TLS.
5. **Input validation:** Validate request bodies and parameters, use DTOs, and avoid exposing internal errors.
6. **Database safety:** Use parameterized queries or JPA safely and enforce least-privilege database access.
7. **Browser security:** Configure CORS with explicit trusted origins. CORS is not authentication. Apply CSRF protection when using cookie-based credentials.
8. **Secrets and operations:** Keep secrets out of source code, avoid logging tokens/passwords, rate-limit sensitive endpoints, and monitor security events.”

### 7. Finish with a project-style summary

“So the short version is: the identity system authenticates the user, the client sends an access token with API requests, Spring Security validates that token on the backend, and authorization rules decide whether the user can access the endpoint. I would combine that with HTTPS, input validation, least privilege, safe token handling, and monitoring. The exact token-storage and identity-provider details depend on the implementation used in the project.”

---

## Quick memory technique: **L-T-V-P**

Remember four words:

- **L — Login:** Credentials are verified by the identity/authentication system.
- **T — Token:** The client receives the appropriate token.
- **V — Validate:** Backend/Spring Security validates the token on protected requests.
- **P — Permission:** Authorization checks roles/scopes before allowing access.

Then add the security layers: **HTTPS + validation + least privilege + safe storage + monitoring**.

## Follow-up questions with concise interview-ready answers

### Q1. What is the difference between authentication and authorization?

**Answer:** Authentication verifies identity—who the user is. Authorization determines permissions—what the authenticated user can do. For example, logging in is authentication; checking whether the user can access an admin endpoint is authorization.

### Q2. What are the three parts of a JWT?

**Answer:** Header, payload, and signature. The header describes the token and algorithm, the payload contains claims, and the signature protects integrity. The payload is not automatically encrypted.

### Q3. How does Spring Security validate a JWT?

**Answer:** The security filter chain extracts the Bearer token, validates its signature and relevant claims, and creates an authenticated security context if it is valid. Authorization rules are then applied before the controller is reached.

### Q4. What is the difference between an access token and a refresh token?

**Answer:** An access token is presented to APIs and is usually short-lived. A refresh token is used with the authorization system to obtain new access tokens. Refresh tokens need stronger protection and should be rotated or revoked according to the system’s design.

### Q5. Should we store JWT in localStorage or cookies?

**Answer:** There is no universal answer independent of architecture. JavaScript-accessible storage such as `localStorage` is exposed if XSS occurs. `HttpOnly` cookies prevent JavaScript from reading the cookie, but cookie-based authentication needs appropriate CSRF defenses. I would follow the organization’s security design.

### Q6. How do you handle token expiry?

**Answer:** The backend rejects expired access tokens. The client can use the approved refresh flow, if available, to obtain a new access token. If refresh fails or the session is revoked, the user must authenticate again.

### Q7. What happens during logout if JWT is stateless?

**Answer:** With a purely stateless access token, logout on the client does not automatically invalidate a copied token before it expires. Common approaches include short-lived access tokens and revocable/rotating refresh tokens; some systems also maintain a denylist or server-side session state.

### Q8. What is the difference between 401 and 403?

**Answer:** `401 Unauthorized` usually means authentication is missing or invalid. `403 Forbidden` means the request is understood but the authenticated user lacks permission. Exact behavior can depend on the security configuration.

### Q9. What is CORS? Does it secure an API?

**Answer:** CORS is a browser mechanism that controls which origins may read cross-origin responses. It is not authentication and does not stop non-browser clients from calling an API. The API still needs authentication and authorization.

### Q10. What is CSRF, and when is protection needed?

**Answer:** CSRF tricks a browser into sending an unwanted authenticated request, especially when credentials are automatically attached, such as cookies. Cookie-based authentication generally needs CSRF protection. A Bearer token manually added in an Authorization header has a different CSRF risk profile, but XSS and token theft still matter.

### Q11. How do you implement role-based authorization?

**Answer:** Define roles or authorities for users and protect endpoints with Spring Security request rules or method-level checks such as `@PreAuthorize("hasRole('ADMIN')")`. The backend must enforce permissions; hiding a frontend button is not enough.

### Q12. Is a JWT encrypted?

**Answer:** A typical signed JWT (JWS) is integrity-protected but its payload is readable by anyone who obtains it. JWTs can also be encrypted using JWE, but we should never assume encryption unless the actual token format and configuration provide it.

### Q13. How do you secure service-to-service APIs?

**Answer:** Use authenticated service identities, short-lived credentials or tokens where supported, TLS, least-privilege scopes, secret management, and authorization at each service. Do not blindly trust requests just because they originate from an internal network.

### Q14. What would you check for a 401 or 403 error?

**Answer:** For 401, check whether the token is present, expired, malformed, signed by the expected key, and has the expected issuer/audience. For 403, check roles/scopes, endpoint rules, and method-level authorization. Never log the full token or credentials while debugging.

### Q15. What should never be included in a JWT payload or logs?

**Answer:** Do not include passwords, secrets, private keys, or sensitive personal information unless there is a carefully reviewed and necessary design. Do not log raw access or refresh tokens. Log safe identifiers and security events instead.

---

## Safe project-answer template

Use this template only after replacing the brackets with details that match your actual implementation:

“In my application, authentication is handled using **[identity provider / Spring Security / approved mechanism]**. After successful login, the client receives **[access token / session cookie]**. For protected API requests, **[API gateway / Spring Security resource server / JWT filter]** validates the credentials or token, and role-based rules control access to endpoints. We use **[actual storage approach]** for client-side credentials and **[actual refresh/logout approach]** for session lifecycle. Along with that, we use HTTPS, input validation, least-privilege access, and safe logging.”

**Do not claim a specific implementation—such as OAuth2, Keycloak, Azure AD, a custom JWT filter, Redis sessions, or HttpOnly cookies—unless you actually used it.** If you are unsure, explain the standard flow as a design approach rather than claiming it was implemented in your project.

## 30-second final revision

“Login verifies identity. The client sends an access token to protected APIs. Spring Security validates the token on the backend, then authorization checks roles and permissions. A session ID identifies server-side session state; an OIDC ID token identifies the user to the client; an access token authorizes API access. I secure APIs using HTTPS, token validation, least privilege, input validation, correct CORS/CSRF configuration, safe token storage, and monitoring.”
