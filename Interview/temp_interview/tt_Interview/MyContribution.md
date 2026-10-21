# I In-Person Interview --- 3-Minute Project Contribution

**Project:** Digital Customer Onboarding & KYC Platform\
**Focus:** Java backend, REST APIs, workflow state management, Azure
OCR/document processing, failure handling\
**Purpose:** Quick revision for a Java Developer interview at 

> **Credibility note:** This summary is based on the existing Nomura
> interview notes. Describe only the modules and tools you personally
> worked on. Do not claim ownership of the entire platform or mention
> tools as hands-on experience unless you actually used them.

------------------------------------------------------------------------

## 1. Three-Minute Project Explanation

### 0:00--0:30 --- Business context

"I worked on an existing digital customer onboarding platform in the
financial-services domain. The platform supports a multi-step onboarding
journey involving identity verification, KYC, AML/compliance checks, and
document processing. My contribution was primarily on the backend
implementation."

### 0:30--1:15 --- Backend onboarding contribution

"I developed and enhanced REST APIs and implemented business validations
and onboarding state transitions. I also integrated downstream services
such as identity and KYC services and handled failures so that an
application would not move to the next state before the required step
succeeded.

We persisted the onboarding status because each step can succeed or fail
independently. This helps identify the last successful step, prevent
invalid transitions, and support controlled retries or recovery."

**Flow to remember:**

`REST API → Request Validation → Application State → Identity/KYC/AML → Decision → Completion`

### 1:15--2:15 --- Document processing and strongest example

"I also contributed to the document-processing flow. Uploaded identity
or address documents were processed through Azure AI Document
Intelligence for OCR and document classification. The extracted fields
were normalized and validated before the onboarding flow could continue.

One practical issue was a date-format mismatch: the OCR service could
return a date in `MM-DD-YYYY`, while our backend expected `YYYY-MM-DD`.
We handled this by introducing a normalization step before business
validation. This kept external-format conversion separate from the
validation logic and gave the application a consistent internal format."

**Flow to remember:**

`Upload → Azure AI Document Intelligence → OCR/Classification → Normalize → Validate → Continue / Retry / Manual Review`

### 2:15--3:00 --- Reliability and impact

"We distinguished technical processing failures from business validation
failures because their recovery paths are different. For low-confidence
or incomplete OCR results, the flow could request a re-upload or route
the case for manual review, based on the configured business rules.

For troubleshooting, I would use the application or correlation ID to
locate the request, check its processing status, review downstream error
metadata, verify normalization and validation results, and confirm the
persisted database state. We also need to avoid unnecessary logging of
sensitive document data.

Overall, my contribution was to make the backend onboarding and
document-processing flow more consistent, traceable, and resilient to
failures."

------------------------------------------------------------------------

## 2. My Contribution --- Quick Revision

-   **REST APIs:** Developed/enhanced onboarding endpoints.
-   **Business validation:** Applied business rules before advancing the
    workflow.
-   **State management:** Maintained onboarding status and controlled
    valid transitions.
-   **Service integration:** Integrated downstream identity/KYC-related
    services.
-   **Azure OCR flow:** Handled extracted fields and classification
    results from Azure AI Document Intelligence.
-   **Normalization:** Converted external date formats into the
    backend's expected format before validation.
-   **Failure handling:** Supported controlled retry, re-upload,
    failure-state, and manual-review handling.
-   **Troubleshooting:** Followed request/correlation IDs, processing
    status, error metadata, validation results, and database state.

**One-line summary:**\
"I worked on backend capabilities for a financial-services onboarding
platform, covering REST APIs, business validations, state transitions,
downstream integrations, Azure OCR data handling, normalization, and
failure troubleshooting."

------------------------------------------------------------------------

## 3. Likely Infosys Follow-Up Questions

### Q1. Why keep business logic out of the controller?

"The controller should handle the HTTP/API layer. Business rules belong
in the service or domain layer, which keeps the code easier to test,
maintain, and reuse."

### Q2. Why is onboarding state important?

"Onboarding has multiple dependent steps that can fail independently.
Persisted state shows what has completed, prevents invalid transitions,
and helps us resume or retry the appropriate operation."

### Q3. What if the KYC service is unavailable?

"I would handle the timeout or downstream error, retry only when it is
safe and allowed by the retry policy, and keep the application from
advancing until the required KYC step succeeds. Persistent failures
should move to an appropriate failure or manual-review state."

### Q4. Why normalize OCR data before validation?

"External services may return data in a different format from our
internal contract. Normalizing at the boundary gives the application one
consistent representation and keeps validation logic simpler."

### Q5. Is successful OCR enough to approve a document?

"No. OCR means data was extracted; it does not prove that the data
satisfies business rules. We still need confidence checks,
mandatory-field checks, and business validation."

### Q6. What is the difference between OCR failure and validation failure?

"OCR failure means the system could not reliably extract the data.
Validation failure means data was extracted, but it did not satisfy a
business rule. Their recovery paths can be different."

### Q7. What if a request times out but the downstream service processed it?

"A timeout does not prove that the operation failed. Before retrying, we
need to check the operation's state and use idempotency where supported
to avoid duplicate processing or invalid state transitions."

### Q8. How would you debug a failed onboarding request?

"I would start with the correlation ID or application ID, identify the
last successful state, inspect relevant API and downstream error
metadata, check normalization and validation results, and verify the
persisted database state."

### Q9. What would you log?

"I would log operational metadata such as application ID, document ID,
correlation ID, status, error code, and relevant confidence information,
while avoiding unnecessary sensitive document contents or PII."

### Q10. What exactly was your contribution?

"My contribution was primarily in specific backend modules of the
existing platform: onboarding APIs, business validations, state
management, downstream integration, OCR response handling,
normalization, and failure troubleshooting. I would not claim that I
designed the entire platform."

------------------------------------------------------------------------

## 4. Technical Flow to Draw on a Whiteboard

``` text
Client
  |
  v
REST Controller
  |
  v
Service Layer
  |
  +--> Business Validation
  |
  +--> Persist Onboarding State
  |
  +--> Identity / KYC / AML Integrations
  |
  +--> Document Processing
          |
          v
      Azure AI Document Intelligence
          |
          v
      Extracted Data
          |
          v
      Normalization
          |
          v
      Business Validation
          |
          +--> Continue
          +--> Retry / Re-upload
          +--> Manual Review
```

**Design principle:** Keep API handling, business validation,
external-format normalization, downstream integration, and persistence
responsibilities separated.

------------------------------------------------------------------------

## 5. Last-Minute Revision Card

  -----------------------------------------------------------------------
  Topic                               Remember this line
  ----------------------------------- -----------------------------------
  My role                             Backend implementation within an
                                      existing onboarding platform

  REST API                            Controller handles HTTP; service
                                      layer handles business logic

  State                               Persisted state prevents invalid
                                      transitions and supports recovery

  OCR                                 Extraction is not the same as
                                      business validation

  Normalization                       Convert external data to a
                                      consistent internal format before
                                      validation

  Timeout                             A timeout does not necessarily mean
                                      the downstream operation failed

  Retry                               Retry only the appropriate
                                      operation when safe; do not blindly
                                      restart everything

  Troubleshooting                     Correlation ID → last successful
                                      state → downstream metadata →
                                      validation → database

  Security                            Do not log unnecessary sensitive
                                      document data or PII
  -----------------------------------------------------------------------

## 6. Interview Delivery Tips

-   Keep the opening focused on the **business problem**, then explain
    **your specific backend contribution**.
-   Use the date-format mismatch as your concrete engineering example;
    it is easier to explain than listing many technologies.
-   Be ready to explain the controller/service separation, state
    transitions, validation, and failure recovery in simple terms.
-   Do not invent performance improvements, transaction volumes, team
    ownership, or implementation details that are not supported by your
    actual experience.
-   If asked about a tool or design you did not personally implement, be
    transparent: explain your understanding rather than claiming
    hands-on ownership.
