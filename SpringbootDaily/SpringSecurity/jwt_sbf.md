# Spring Security in Banking Onboarding — 3–5 Minute Revision Notes

## 1. Complete workflow

1. Customer visits the Angular onboarding portal and enters their mobile number.
2. Mobile Verification Service: Sends and verifies the OTP.
3. Authentication: After successful OTP verification, the authentication system can issue a short-lived, restricted JWT for onboarding.
4. Identity Verification Service: Customer uploads identity documents; the service processes and verifies them.
5. AML Service: Performs the required anti-money-laundering screening.
6. KYC Service: Performs KYC checks and may mark the application as pending manual review.
7. IAM Service: Creates customer credentials after all mandatory checks and approvals are completed.

## 2. How Spring Security works

- Authentication: Establishes the identity of the user or calling service.
- Authorization: Checks whether that identity has permission to access a particular API.
- Spring Security filter chain: Intercepts requests and applies the configured security rules.
- JWT validation: Verifies the token's signature, expiry, issuer and other relevant claims.
- Angular interceptor: Attaches the access token to requests using `Authorization: Bearer <token>`. It does not perform trusted backend validation.
- Endpoint protection: Public OTP endpoints may use `permitAll()`, while protected onboarding APIs require authentication and appropriate scopes or roles.
- Ownership checks: The backend verifies that the customer can access the requested application, rather than trusting a customer ID supplied by Angular.

## 3. Code-level implementation

Example Spring Security configuration:

```
@Bean
SecurityFilterChain securityFilterChain(
        HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth -> auth
        .requestMatchers(
            "/api/auth/otp/request",
            "/api/auth/otp/verify"
        ).permitAll()
        .requestMatchers("/api/onboarding/**")
            .hasAuthority("SCOPE_onboarding")
        .anyRequest().authenticated()
    );

    http.oauth2ResourceServer(oauth2 ->
        oauth2.jwt(Customizer.withDefaults())
    );

    return http.build();
}
```

Technical points to remember:

- Configure trusted JWT issuer or verification keys.
- Spring Security validates the JWT and establishes the authenticated principal.
- `SCOPE_onboarding` is an example authority; the token and configuration must support it.
- Public OTP endpoints still need rate limiting, OTP expiry and attempt limits.
- Use least-privilege permissions and never rely on frontend routing as a security control.

## 4. Service-to-service communication

- The onboarding service orchestrates the workflow.
- It calls Identity Verification, AML and KYC services as required.
- Internal services must authenticate their callers and enforce permissions.
- Possible mechanisms include OAuth 2.0 client credentials, workload identity or mutual TLS, depending on the architecture.
- The receiving service should not automatically trust a request merely because it originated inside the network.
- User tokens and service credentials serve different purposes; use the appropriate identity and permissions for each call.

## 5. Application restart or expired JWT — how does the customer resume?

Scenario: The customer completed identity verification, but KYC is still pending manual review.

1. Customer returns to the portal.
2. The old JWT may have expired, so the customer verifies OTP again.
3. The backend authenticates the customer and resolves their existing onboarding application.
4. The onboarding service retrieves the persisted workflow state from the database.
5. The API returns the existing status, for example `KYC_MANUAL_REVIEW_PENDING`.
6. Angular displays the KYC pending screen instead of restarting onboarding.

Key technical distinction:

- JWT stores authentication-related claims; it should not be the sole source of workflow progress.
- The database stores the persistent workflow state.
- Spring Security handles API authentication and authorization.
- The onboarding service manages workflow transitions and recovery.

## 6. Failure handling and reliability

- Idempotency: Prevents duplicate requests from producing duplicate business effects.
- Retries with backoff: Handle suitable transient failures without retrying every error indefinitely.
- Persistent workflow state: Helps resume processing after application restarts.
- Transactions: Maintain consistency for related database updates.
- Outbox pattern: Can coordinate database updates with reliable event publication.
- State validation: Prevents customers from skipping identity, AML or KYC requirements by directly calling later-stage APIs.

## 7. Common interview follow-up questions

| Question                                             | Key answer                                                                                                    |
| ---------------------------------------------------- | ------------------------------------------------------------------------------------------------------------- |
| Why Spring Security if the customer is new?          | To protect APIs and enforce permissions, even without traditional username-password login.                    |
| Does Spring Security remember the onboarding stage?  | No. The onboarding service persists the workflow state in the database.                                       |
| What if the JWT expires?                             | Re-authenticate or obtain a new access token through the supported flow, then retrieve saved progress.        |
| How do you prevent access to another customer's KYC? | Validate the authenticated identity and application ownership on the backend.                                 |
| How do you secure internal APIs?                     | Authenticate service identities and enforce least-privilege permissions.                                      |
| What if AML fails?                                   | Track the failure, retry suitable transient errors and do not mark AML completed without a valid outcome.     |
| Can OTP success grant full banking access?           | Not automatically. OTP proves control of a verification channel; further checks and permissions are required. |
| What do HTTP 401 and 403 mean?                       | `401`: missing or invalid authentication. `403`: access denied.                                               |

## 8. Final interview answer — 30-second summary

> “Spring Security protects the backend APIs throughout our onboarding workflow. After OTP verification, the authentication system can issue a short-lived, restricted JWT. Spring Security validates the token and enforces authorization rules for identity verification, AML and KYC operations. Internal service communication is also authenticated and authorized. The onboarding service persists the workflow state in the database, so if the application restarts or the token expires, the customer can authenticate again and resume from the existing stage rather than starting over. Finally, IAM creates credentials only after all mandatory verification and approval requirements are satisfied.”

Important: Treat JWT issuance after OTP and the specific service-to-service authentication mechanism as implementation details to confirm against your actual project. The above is a technically sound reference design, not proof that every component was implemented exactly this way.