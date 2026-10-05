# Nomura Java Interview — 7-Minute Master Module
## Financial Services / Customer Onboarding + Document Processing Contributions

> **Source boundary:** This module is built from the pasted interview material only. It keeps the contribution areas actually supported by that material: REST APIs, business validation, onboarding state management, downstream integrations, error handling, document processing, Azure AI Document Intelligence, OCR/classification, normalization, validation, confidence handling, retry/manual review, idempotency and troubleshooting.

---

# 1. Your 7-Minute Interview Story

## 0:00–0:45 — Start with the business context

> I have worked on an existing digital customer onboarding platform in the financial-services domain. My contribution has been mainly on the backend side, where I developed and enhanced REST APIs for the onboarding journey, implemented business validations and state transitions, integrated downstream services such as identity, KYC and validation services, and handled failures without allowing the workflow to move to an invalid state.

Then introduce the second major contribution:

> I also worked on the document-processing flow, where uploaded identity and address documents are processed through Azure AI Document Intelligence for OCR and classification. I handled the extracted data, normalization, validation, confidence-based handling and failure/manual-review scenarios.

---

# 2. 0:45–2:15 — Contribution #1: Customer Onboarding

## Business flow

```text
Customer
   ↓
POST /onboarding
   ↓
Validate Request
   ↓
Create Application
   ↓
Identity Verification
   ↓
KYC
   ↓
AML / Compliance
   ↓
Business Validation
   ↓
Decision
   ↓
Customer / Product Creation
```

## What I contributed

```text
REST APIs
+
Business Validations
+
State Management
+
Downstream Integrations
+
Error Handling
```

### Interview-ready answer

> I worked on developing and enhancing REST APIs for the customer onboarding journey. My responsibility was beyond just exposing endpoints. I implemented business validations, managed the onboarding state throughout the lifecycle, integrated the workflow with downstream services such as identity and KYC, and made sure failures did not incorrectly advance the application to the next state.

---

# 3. 2:15–3:15 — Java / Spring Boot Code-Level Discussion

## Correct layering

```text
Controller
    ↓
Service / Orchestrator
    ↓
Business Validation
    ↓
Downstream Services
    ↓
Repository
    ↓
Database
```

### Controller

```java
@PostMapping("/onboarding")
public OnboardingResponse initiate(
        @RequestBody OnboardingRequest request) {

    return onboardingService.initiate(request);
}
```

### Service

```java
public OnboardingResponse initiate(OnboardingRequest request) {

    validate(request);

    Onboarding application =
        repository.save(createApplication(request));

    identityService.verify(application);

    updateStatus(application, IDENTITY_VERIFIED);

    return response(application);
}
```

### Nomura follow-up

**Q: Why not put business logic inside the controller?**

> The controller should handle the API/transport layer. Business rules belong in the service or domain layer so that the application remains maintainable and the business logic is not coupled to HTTP concerns.

---

# 4. 3:15–4:00 — Contribution #2: State Management + Failure Handling

## State lifecycle

```text
INITIATED
    ↓
IDENTITY_VERIFIED
    ↓
KYC_COMPLETED
    ↓
AML_COMPLETED
    ↓
ELIGIBILITY_COMPLETED
    ↓
CUSTOMER_CREATED
    ↓
COMPLETED
```

## Failure states

```text
KYC_FAILED
AML_FAILED
ELIGIBILITY_FAILED
MANUAL_REVIEW
```

### Q: Why do you need onboarding status?

> Onboarding is a multi-step workflow and every step can succeed or fail independently. Persisting the state tells us exactly where the application is, prevents invalid state transitions, and helps us safely resume or retry failed operations.

### Q: What happens if KYC is unavailable?

> I would handle the downstream failure through appropriate timeout, retry and failure-state handling. The onboarding application should not move to the next state until the required KYC operation has successfully completed. If the failure is retryable, we can retry according to the defined policy; otherwise, the application can move to an appropriate failed or manual-review state.

### Strong follow-up

**Q: Why not simply retry the complete onboarding flow?**

> Because completed steps should not be repeated blindly. We should know the persisted application state and retry only the operation that is safely retryable.

---

# 5. 4:00–5:20 — Contribution #3: Document Processing + Azure OCR

## End-to-end flow

```text
Passport / ID / Address Document
              ↓
            Upload
              ↓
      Document Processing
              ↓
 Azure AI Document Intelligence
              ↓
       OCR + Classification
              ↓
        Extracted Data
              ↓
         Normalization
              ↓
          Validation
              ↓
       KYC / Onboarding
```

## My contribution

```text
Upload Flow
    ↓
Azure Integration
    ↓
Extracted Data Handling
    ↓
Normalization
    ↓
Validation
    ↓
Failure / Manual Review
```

### Q: How did you integrate Azure Document Intelligence?

> The document-processing flow receives the uploaded document and sends it to Azure AI Document Intelligence for OCR and document classification. Azure returns extracted fields along with confidence information. We then normalize the extracted data into our application's expected format and validate it against our business rules before allowing the onboarding flow to proceed.

---

# 6. 5:20–6:00 — Strongest Practical Example: Normalization

## Real issue

Azure could return:

```text
MM-DD-YYYY
```

while the backend expected:

```text
YYYY-MM-DD
```

## Solution

```text
Azure Response
      ↓
Normalization Layer
      ↓
YYYY-MM-DD
      ↓
Business Validation
```

### Interview answer

> One practical issue we handled was a format inconsistency between the OCR output and our backend contract. For example, a date could be returned in a different format than what the backend expected. Instead of spreading format-conversion logic across the application, we introduced a normalization step before validation.

### Follow-up: Why normalize before validation?

> Validation should operate on a consistent internal representation. Otherwise every validator would need to understand multiple external formats.

---

# 7. 6:00–6:35 — OCR Confidence + Failure Classification

## Confidence handling

```text
OCR
 ↓
Confidence Check
 ↓
Meets Threshold?
 ├── YES → Normalize → Validate
 └── NO  → Retry / Re-upload
                  ↓
            Repeated Failure
                  ↓
            Manual Review
```

### Q: How do you know OCR output is reliable?

> We consider the confidence information returned by the document intelligence service together with our business validations. If the confidence is below the configured threshold or mandatory fields cannot be reliably validated, we don't blindly continue the onboarding flow. We can request another document submission or route the case for manual review based on the business rules.

## OCR failure vs validation failure

### OCR failure

```text
Document
   ↓
OCR
   ↓
Extraction Failed
```

Technical/document-processing problem.

### Validation failure

```text
OCR
   ↓
Data Extracted
   ↓
Business Validation
   ↓
Validation Failed
```

Business-rule problem.

### Golden answer

> I distinguish technical processing failures from business validation failures because their recovery mechanisms are different.

---

# 8. 6:35–7:00 — Reliability + Security + Troubleshooting

## Duplicate document submission

### Q: What if the same document is uploaded twice?

> We should identify the document or application using a business identifier or idempotency mechanism and avoid creating duplicate processing records. Database constraints can provide an additional safety layer.

## Late downstream response

### Q: What if Azure or another downstream service responds after a timeout?

> We need idempotent processing and state validation so that a late response does not cause duplicate state transitions or duplicate customer or document records.

## Troubleshooting sequence

```text
Correlation ID
      ↓
Application ID
      ↓
Document Processing Status
      ↓
Azure Request / Response Metadata
      ↓
Confidence
      ↓
Normalization
      ↓
Validation
      ↓
DB State
```

### Answer

> I would trace the request using the correlation ID and application ID, check the processing state, inspect the downstream metadata, verify confidence and normalization results, then validate the database state to identify where the flow diverged.

## Logging

Log safe operational information such as:

```text
applicationId
documentId
correlationId
status
errorCode
confidence
```

Avoid unnecessarily logging sensitive document contents or PII.

---

# 9. “What Exactly Did YOU Do?” — Most Important Answer

Use this when the interviewer asks for your personal contribution:

> My contribution was primarily on the backend implementation. I developed and enhanced REST APIs, implemented business validations and state transitions, integrated downstream services, implemented the document-processing flow, handled Azure OCR responses, normalized extracted data, and implemented failure and manual-review handling.

### Important credibility line

> I contributed to these specific modules within the existing platform.

### Do NOT say

> “I designed the entire banking platform.”

---

# 10. Nomura-Style Follow-Up Questions + Answers

## Q1. Why did you choose REST APIs for onboarding?

> REST provides a standard HTTP-based interface between the onboarding client and backend services, with clear resource-oriented contracts and stateless communication.

---

## Q2. Where should validation happen?

> Basic request validation can happen at the API boundary using Bean Validation, while business validations should remain in the service or domain layer.

Example:

```java
@NotBlank
private String documentType;
```

But:

```text
Is this document valid for this onboarding journey?
```

belongs to business logic.

---

## Q3. Why is state management important in a banking onboarding workflow?

> Because the onboarding journey contains multiple dependent steps. Persisted state tells us what has completed, prevents invalid transitions, and allows controlled retry or recovery.

---

## Q4. What if KYC is down?

> Use timeout, controlled retry where safe, and an explicit failure state. Do not advance the onboarding state until the required KYC operation succeeds.

---

## Q5. What if Azure is down?

> Handle timeout and downstream errors gracefully, persist the appropriate processing state, retry only where the operation is safely retryable, and route persistent failures to manual review when required.

---

## Q6. What if Azure returns only partial data?

> We normalize the response first and then validate mandatory fields. If required information is missing or unreliable, the onboarding flow should not continue blindly.

---

## Q7. Why not validate the raw OCR response directly?

> Because external responses may use formats different from our internal contract. A normalization layer gives the application one consistent representation, which keeps validation logic simpler and more maintainable.

---

## Q8. What would you store for document processing?

```text
document_id
application_id
status
attempt_count
confidence
error_code
created_at
updated_at
```

Possible states:

```text
UPLOADED
PROCESSING
PROCESSED
VALIDATION_FAILED
FAILED
MANUAL_REVIEW
```

---

## Q9. OCR extracted the data successfully. Is that enough?

> No. OCR extraction and business validation are separate concerns. Successfully extracting a value does not mean that the value satisfies the application's business rules.

---

## Q10. What is the difference between technical failure and business failure?

> A technical failure can be an OCR extraction failure or downstream timeout. A business failure occurs when the data is extracted but does not satisfy a validation rule. The recovery path can therefore be different.

---

## Q11. What if a downstream call times out but it actually processed successfully?

> We should not assume that a timeout means the downstream operation definitely failed. Idempotency and state validation are needed so that a late response or retry does not create duplicate records or invalid state transitions.

---

## Q12. Why not retry indefinitely?

> Indefinite retry can overload the downstream system and does not solve non-retryable failures. We should use a controlled retry policy and move persistent failures to an appropriate failed or manual-review state.

---

## Q13. What would you log for OCR troubleshooting?

> I would log correlationId, applicationId, documentId, processing status, error code and confidence information, while avoiding unnecessary logging of sensitive document contents or PII.

---

## Q14. How do you prevent duplicate document-processing records?

> Use a stable business identifier or idempotency mechanism for the document/application and reinforce that with database-level uniqueness where appropriate.

---

## Q15. How would you debug an onboarding failure in production?

> I would start with the correlation ID, identify the last successful state transition, inspect the relevant API and downstream metadata, verify the stored workflow or document-processing state, check validation results and then confirm the database state.

---

# 11. Nomura Java Interview Angle

When explaining this project, repeatedly connect the story to:

```text
Java / Spring
      ↓
REST APIs
      ↓
Service Layer
      ↓
Business Validation
      ↓
State Management
      ↓
Downstream Integration
      ↓
Database State
      ↓
Failure Handling
      ↓
Troubleshooting
```

The strongest technical framing is not just:

> “I worked on onboarding.”

Instead:

> “I implemented backend onboarding capabilities where REST APIs triggered service-layer business validation, downstream verification, persisted state transitions, document processing and controlled failure/recovery.”

---

# 12. Nomura Interview Traps

## Trap 1 — OCR = validated data

❌ Wrong

```text
OCR extraction ≠ business validation
```

---

## Trap 2 — Timeout = confirmed downstream failure

❌ Wrong

A downstream system may have processed the request even if the response was lost.

Use:

```text
Idempotency + State Validation
```

---

## Trap 3 — @Valid = all validation

❌ Wrong

```text
Bean Validation
+
Business Validation
```

---

## Trap 4 — Status is only for UI

❌ Wrong

State drives:

```text
Workflow
Recovery
Retry
Troubleshooting
```

---

## Trap 5 — Retry means rerun everything

❌ Wrong

Retry only the appropriate operation from the persisted state.

---

## Trap 6 — Log complete OCR output

❌ Risky

Log operational metadata, not unnecessary sensitive document data.

---

## Trap 7 — Claim ownership of everything

❌ Avoid

> “I designed the entire banking platform.”

✅ Say:

> “I contributed to these specific backend modules within the existing platform.”

---

# 13. If the Interviewer Asks “Walk Me Through One Complete Request”

Use this flow:

```text
Client
  ↓
POST /onboarding
  ↓
Basic Request Validation
  ↓
Application Created
  ↓
Identity Verification
  ↓
KYC
  ↓
AML / Compliance
  ↓
Business Validation
  ↓
Decision
  ↓
Customer / Product Creation
  ↓
COMPLETED
```

Then add:

> For document-based verification, the document-processing path integrates Azure AI Document Intelligence, performs OCR and classification, normalizes the extracted fields, validates them and either continues the onboarding journey or moves the case into retry/manual-review handling.

---

# 14. If Asked “What Was Your Strongest Engineering Contribution?”

Use the normalization story:

> One of the practical engineering issues I handled was the mismatch between an external OCR response format and our backend contract. Azure could return a date in a different format from what the backend expected. I introduced normalization before validation so that the rest of the application worked with a consistent representation instead of spreading external-format handling across multiple validators.

Then connect it to design:

```text
External Contract
       ↓
Normalization Boundary
       ↓
Internal Model
       ↓
Business Validation
```

---

# 15. Final 60-Second Revision Sheet

## CUSTOMER ONBOARDING

```text
REST API
  ↓
Request Validation
  ↓
Application Created
  ↓
Identity
  ↓
KYC
  ↓
AML
  ↓
Business Validation
  ↓
Customer / Product Creation
```

### My contribution

```text
REST APIs
Business Validations
State Management
Downstream Integration
Error Handling
```

---

## DOCUMENT PROCESSING

```text
Document Upload
      ↓
Azure AI Document Intelligence
      ↓
OCR + Classification
      ↓
Extracted Data
      ↓
Normalization
      ↓
Validation
      ↓
Accept / Retry / Manual Review
```

### My contribution

```text
Azure Integration
Extracted Data Handling
Normalization
Validation
Failure Handling
Manual Review
```

---

## CORE FAILURE PRINCIPLES

```text
Timeout
  ↓
Controlled Retry if Safe
  ↓
State Validation
  ↓
Failed / Manual Review if Persistent
```

```text
Late Response
  ↓
Idempotency
+
State Check
  ↓
Prevent Duplicate Processing
```

---

# 16. Golden Interview Lines

### Contribution

> “My contribution was primarily on the backend implementation within the existing platform.”

### Architecture

> “I kept API handling, business validation, downstream integration and persistence concerns separated.”

### State

> “Persisted state allowed us to control valid transitions and safely recover from failures.”

### OCR

> “OCR extraction and business validation are separate concerns.”

### Normalization

> “We normalize external data at the boundary so internal validation works on a consistent representation.”

### Failure

> “A timeout does not necessarily prove that the downstream operation failed, so idempotency and state validation are important.”

### Credibility

> “I contributed to these specific modules within the existing platform rather than claiming ownership of the entire platform.”

---

# 17. Final Mental Model

Before the Nomura interview, remember:

```text
             MY CONTRIBUTION

      Customer Onboarding
              +
      Document Processing
              |
              v
        REST APIs
              |
              v
       Service Layer
              |
              v
     Business Validation
              |
              v
      State Management
              |
              v
   Downstream Integrations
       /            \
     KYC         Azure OCR
                   |
              Normalization
                   |
               Validation
                   |
          Retry / Manual Review
                   |
                   v
        Reliable Onboarding
```

## One-line summary

> **I worked on backend capabilities for a financial-services onboarding platform, covering REST APIs, business validations, state transitions, downstream integrations, document processing with Azure OCR, data normalization, failure handling, idempotency and troubleshooting.**
