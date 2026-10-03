हाँ. इन दोनों modules को Nomura के Java/Spring Boot interview angle से पढ़ो। मैंने Nomura की current Mumbai postings को भी cross-check किया है—उनमें Java, Spring Boot, REST, SQL/database, troubleshooting, testing और financial-domain understanding पर जोर है। 

Module 1 + 2 — 10-Minute Interview Preparation

1️⃣ Customer Onboarding Workflow — 4–5 min

पहले ये कहानी समझो

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
Customer Creation / Product Creation

तुम्हारा contribution मुख्यतः:

REST APIs + business validations + state management + downstream integrations + error handling

Interview में ऐसे बोलना

> "I worked on developing and enhancing REST APIs for the customer onboarding journey. My responsibility was not just creating the APIs, but implementing the business validations and managing the onboarding state throughout the lifecycle. I also integrated the onboarding workflow with downstream services such as identity, KYC and other validation services. One important part was making sure that the application moved through valid states and that failures were handled without incorrectly advancing the workflow."




---

2️⃣ Code Level — कैसे implement किया?

Conceptually:

@PostMapping("/onboarding")
public OnboardingResponse initiate(
        @RequestBody OnboardingRequest request) {

    return onboardingService.initiate(request);
}

Service:

public OnboardingResponse initiate(OnboardingRequest request) {

    validate(request);

    Onboarding application =
        repository.save(createApplication(request));

    identityService.verify(application);

    updateStatus(application, IDENTITY_VERIFIED);

    return response(application);
}

लेकिन interview में controller के अंदर business logic मत दिखाना।

Correct layering:

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


---

3️⃣ State Management — VERY IMPORTANT

तुम बोल सकते हो:

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

Failure:

KYC_FAILED
AML_FAILED
ELIGIBILITY_FAILED
MANUAL_REVIEW

Interview trap

Q: Why do you need onboarding status?

> "Because onboarding is a multi-step workflow and every step can succeed or fail independently. Persisting the state allows us to know exactly where the application is, prevents invalid state transitions, and helps us safely resume or retry failed operations."



यह answer बहुत important है।


---

4️⃣ Downstream Service Failure

Interviewer:

> "What happens if KYC service is unavailable?"



Don't say:

> "We retry everything."



Say:

> "We would handle the downstream failure through appropriate timeout, retry and failure-state handling. The onboarding application should not move to the next state until the required KYC operation has successfully completed. If the failure is retryable, we can retry according to the defined policy; otherwise, the application can move to an appropriate failed or manual-review state."



Flow

Onboarding
    ↓
KYC
    ↓
Timeout
    ↓
Retryable?
 ┌──YES──→ Controlled Retry
 │
 └──NO───→ FAILED / MANUAL REVIEW


---

5️⃣ Document Processing & OCR — 4–5 min

अब दूसरा module.

Business problem

Customer document upload करता है:

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

तुम्हारा contribution:

Upload flow → Azure integration → extracted data handling → normalization → validation → failure/manual review


---

6️⃣ Azure OCR Integration — Interview Answer

अगर पूछा:

> "How did you integrate Azure Document Intelligence?"



बोलो:

> "The document processing flow receives the uploaded document and sends it to Azure AI Document Intelligence for OCR and document classification. Azure returns extracted fields along with confidence information. We then normalize the extracted data into our application's expected format and validate it against our business rules before allowing the onboarding flow to proceed."



Important:

Document
   ↓
Azure AI
   ↓
OCR
   ↓
Classification
   ↓
Extracted Fields
   ↓
Normalization
   ↓
Validation
   ↓
Accepted / Manual Review


---

7️⃣ VERY IMPORTANT — Normalization Story

तुम्हारा strongest real-world example:

Azure ने date दिया:

MM-DD-YYYY

लेकिन backend expect करता था:

YYYY-MM-DD

तो बीच में normalization layer:

Azure Response
      ↓
Normalization Layer
      ↓
YYYY-MM-DD
      ↓
Business Validation

Interview answer:

> "One issue we handled was format inconsistency between the OCR output and our backend contract. For example, a date could be returned in a different format than what our backend expected. Instead of spreading format-conversion logic across the application, we introduced a normalization step before validation."



यह बहुत अच्छा practical engineering example है।


---

8️⃣ OCR Confidence — Major Follow-up

Interviewer:

> "How do you know whether OCR output is reliable?"



Answer:

> "We consider the confidence information returned by the document intelligence service along with our business validations. If the confidence is below the configured threshold or mandatory fields cannot be reliably validated, we don't blindly continue the onboarding flow. We can request another document submission or route the case for manual review based on the business rules."



Your flow:

OCR
 ↓
Confidence Check
 ↓
≥ Threshold
 ├── YES → Normalize → Validate
 │
 └── NO → Retry / Re-upload
              ↓
        Repeated Failure
              ↓
        Manual Review


---

9️⃣ OCR Failure vs Validation Failure

Very important distinction.

OCR failure

Azure couldn't properly extract information.

Document
 ↓
OCR
 ↓
Extraction Failed

Validation failure

Data extracted successfully but business validation failed.

OCR
 ↓
Name = Rishabh
DOB = 01-01-1990
 ↓
Validation
 ↓
DOB doesn't match expected customer data

Interview में बोलना:

> "I distinguish technical processing failures from business validation failures because their recovery mechanisms are different."



🔥 Strong answer.


---

🔥 Top 10 Follow-up Questions

Q1. Why use REST API?

> "REST provides a standard HTTP-based interface between the onboarding client and backend services, with clear resource-oriented contracts and stateless communication."




---

Q2. Where should validation happen?

> "Basic request validation can happen at the API boundary using Bean Validation, while business validations should remain in the service/domain layer."



Example:

@NotBlank
private String documentType;

But:

Is this document valid for this onboarding journey?

belongs to business logic.


---

Q3. What if the same document is uploaded twice?

> "We should identify the document/application using a business identifier or idempotency mechanism and avoid creating duplicate processing records. Database constraints can provide an additional safety layer."




---

Q4. What if Azure is down?

> "The application should handle timeout and downstream errors gracefully, persist the appropriate processing state, and retry only where the operation is safely retryable. Persistent failures can go to manual review."




---

Q5. What if Azure returns partial data?

> "We validate mandatory fields after normalization. If mandatory information is missing or unreliable, we don't allow the onboarding workflow to proceed blindly."




---

Q6. Why normalization before validation?

> "Because validation should operate on a consistent internal representation. Otherwise every validator would need to understand multiple external formats."




---

Q7. Where would you store document-processing status?

Example:

document_id
application_id
status
attempt_count
confidence
error_code
created_at
updated_at

Possible states:

UPLOADED
PROCESSING
PROCESSED
VALIDATION_FAILED
FAILED
MANUAL_REVIEW


---

Q8. How do you troubleshoot OCR issues?

Correlation ID
      ↓
Application ID
      ↓
Document Processing Status
      ↓
Azure Request/Response metadata
      ↓
Confidence
      ↓
Normalization
      ↓
Validation
      ↓
DB State

Don't log the actual sensitive document unnecessarily.


---

Q9. What if downstream response comes after timeout?

🔥 Trap.

Don't immediately process it again.

> "We need idempotent processing and state validation so that a late response doesn't cause duplicate state transitions or duplicate customer/document records."




---

Q10. What is your contribution specifically?

इसका answer बहुत carefully देना:

> "My contribution was primarily on the backend implementation: developing and enhancing REST APIs, implementing business validations and state transitions, integrating downstream services, implementing the document-processing flow, handling Azure OCR responses, normalizing extracted data, and implementing failure and manual-review handling."



Don't say:
"I designed the entire banking platform."

Instead say:

> "I contributed to these specific modules within the existing platform."




---

🚨 Nomura Interview Traps

Nomura की current roles में REST, Spring Boot, SQL/database, testing और troubleshooting explicitly relevant हैं, इसलिए इन modules को सिर्फ business story की तरह मत बताना। 

Trap 1

"OCR means data is correct."

❌ No.

OCR extraction ≠ Data validation


---

Trap 2

"If Azure fails, retry indefinitely."

❌ No.

Controlled retry + timeout + failure state.


---

Trap 3

"Validation is only @Valid."

❌ No.

Bean Validation
+
Business Validation


---

Trap 4

"Status is just for UI."

❌ No.

Status drives workflow, recovery and operational troubleshooting.


---

Trap 5

"Downstream timeout means downstream failed."

❌ Dangerous assumption.

It may have processed successfully but response was lost.

Therefore:

Idempotency + state check.


---

Trap 6

Log the OCR response for debugging.

❌ Sensitive data risk.

Log:

applicationId
documentId
correlationId
status
errorCode
confidence

but avoid unnecessary PII/document contents.


---

⚡ FINAL 2-MINUTE REVISION

इसे interview से पहले पढ़ना:

CUSTOMER ONBOARDING

My contribution:
REST APIs
Business validations
State management
Downstream integrations
Error handling

Flow:

Customer
 ↓
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
Customer/Product Creation
 ↓
COMPLETED


State is important because:
- onboarding has multiple steps
- every step can succeed/fail
- prevents invalid transitions
- enables retry/recovery


DOCUMENT PROCESSING

Document
 ↓
Upload
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


Important distinction:

OCR failure
→ extraction/technical problem

Validation failure
→ extracted data doesn't satisfy business rules


My practical example:

Azure returned date in one format
but backend expected another format.

I introduced normalization before validation
so the rest of the application worked with
one consistent format.


OCR confidence:

Confidence >= threshold
→ normalize
→ validate

Low confidence
→ re-upload/retry

Repeated failure
→ manual review


Failure handling:

Timeout ≠ necessarily failure.

Use:
Timeout
Retry where safe
Idempotency
State validation
Manual review


Golden interview line:

"I separated external document-processing concerns
from our internal validation model by introducing
a normalization layer, and I used explicit processing
states to handle success, failure, retry and manual review."


MOST IMPORTANT TRAPS:

OCR ≠ validation

@Valid ≠ business validation

Timeout ≠ confirmed downstream failure

Retry ≠ execute everything again

Status ≠ just UI information

Don't log sensitive document data

Don't claim ownership of the entire platform;
clearly explain your specific contribution.

Nomura angle: इन दोनों modules को explain करते समय हमेशा Java/Spring Boot + REST + SQL/state + downstream integration + failure handling + security + troubleshooting से connect करना। यही combination Nomura की current Mumbai Java roles में repeatedly दिखाई देता है। 