# 7-Minute Interview Master Module — Current Work: Financial Product Onboarding

## 0. Positioning — What I am currently working on

**Core interview line:**

> I am currently working on extending an existing digital banking onboarding platform to support additional financial-product journeys such as credit cards and loans. My contribution is focused on extending the existing onboarding capability while reusing common identity, KYC and compliance capabilities and keeping product-specific eligibility and validation separate.

The important positioning is:

**I am extending an existing platform — I am not claiming that I designed the entire banking architecture.**

---

# 1. 7-Minute Story Structure

Use this sequence if the interviewer says:

**“Tell me about your current project / what are you working on?”**

### Minute 1 — Business problem

The existing platform handles the common customer onboarding journey:

```text
Customer
   ↓
Identity Verification
   ↓
KYC
   ↓
AML / Compliance
   ↓
Customer Creation
```

The business now wants to support multiple financial products such as:

```text
                 ┌── Credit Card
Customer → Onboarding Platform ── Loan
                 └── Other Products
```

The key problem is that we should **not duplicate the complete onboarding journey for every financial product**.

---

### Minute 2 — What is common vs product-specific?

### Common capabilities

```text
Identity Verification
KYC
AML / Compliance
Document Processing
Customer Verification
```

### Product-specific capabilities

```text
Eligibility
Validation
Risk / Business Rules
Product Decision
Product Creation
```

The key design principle:

> **Reuse common onboarding capabilities while isolating product-specific business rules.**

This allows a new product to be added without rewriting the complete onboarding platform.

---

# 2. Architecture Explanation

Conceptually:

```text
                    Customer / React
                           |
                           v
                 +---------------------+
                 | Onboarding Service  |
                 | / Orchestrator      |
                 +----------+----------+
                            |
             +--------------+--------------+
             |              |              |
             v              v              v
        Identity       KYC / Document     AML
         Service          Services       Service
             \              |              /
              \-------------+-------------/
                            |
                            v
                  Product Decision Layer
                            |
                 +----------+----------+
                 |                     |
                 v                     v
          Credit Card Rules       Loan Rules
                 |                     |
                 +----------+----------+
                            |
                            v
                   Customer / Product
                       Creation
```

### Important separation

The Identity Service should answer:

> “Is this customer's identity successfully verified?”

It should **not** know:

> “This customer wants a credit card.”

Similarly, KYC should answer:

> “Is KYC completed/approved?”

The product layer decides:

> “Is this customer eligible for this particular product?”

---

# 3. My Current Contribution

When asked:

### “What exactly are YOU working on?”

Use:

> I am working on the implementation and extension of the existing onboarding platform, particularly around product-specific onboarding capabilities. The focus is to reuse the existing identity, KYC and compliance capabilities and introduce product-specific validation and eligibility logic without coupling those rules into the common onboarding flow.

Then explain your contribution in three areas:

### A. Common onboarding integration

I work with the existing onboarding lifecycle and make sure the product journey can reuse:

- Identity verification
- KYC
- Compliance checks
- Document processing
- Customer verification

### B. Product-specific decisioning

The product journey needs its own:

- Eligibility rules
- Validation
- Product-specific business rules
- Risk / decision logic
- Product creation flow

A Strategy-based design is appropriate here:

```text
Product Type
     ↓
Strategy / Registry
     ↓
 ┌───────────────┬────────────────────┐
 ↓               ↓                    ↓
Loan          Credit Card        Future Product
Strategy      Strategy             Strategy
```

### C. Reliability and failure handling

I also focus on:

- State management
- Idempotency
- Controlled retries
- Failure states
- Avoiding duplicate customer/product creation
- Secure handling of financial/customer data
- Troubleshooting through correlation IDs

---

# 4. New Customer vs Existing Customer

## Existing customer

```text
Existing Customer Information
          ↓
Required Verification
          ↓
Product Eligibility
          ↓
Product Relationship
```

## New-to-bank customer

```text
No Existing Customer
          ↓
Identity Verification
          ↓
KYC
          ↓
AML / Compliance
          ↓
Business Decision
          ↓
Customer Creation
          ↓
Product Onboarding
```

### Interview answer

> We don't assume that the applicant already exists as a customer. For a new-to-bank applicant, the platform first establishes identity and completes the required KYC and compliance checks. Once the business decision allows the journey to proceed, customer creation can take place before establishing the relevant product relationship.

### Trap

Do NOT say:

> “If the customer doesn't exist, we immediately create the customer.”

The correct sequence is:

**Identity → KYC → Compliance → Business Decision → Customer Creation**

---

# 5. KYC vs Product Eligibility

This is one of the most important cross-questions.

### KYC

Answers:

> **“Who is this customer?”**

### Eligibility

Answers:

> **“Is this customer eligible for this particular product?”**

Example:

```text
KYC = PASS
     ↓
Loan Eligibility
     ↓
Age
Income
Employment
Existing Obligations
Risk Decision
Product Criteria
     ↓
Eligible / Not Eligible
```

### Interview answer

> KYC completion is a prerequisite for onboarding, but it is not the same as product eligibility. KYC establishes and verifies the customer's identity and required information, whereas eligibility determines whether that verified customer satisfies the business and product-specific criteria for a particular financial product.

---

# 6. Why Strategy Pattern?

### Bad design

```java
if (product.equals("LOAN")) {
    // hundreds of lines
} else if (product.equals("CARD")) {
    // hundreds of lines
}
```

This becomes difficult to maintain as products increase.

### Better approach

```java
public interface ProductEligibilityStrategy {

    EligibilityResult evaluate(
        Customer customer,
        ProductApplication application
    );
}
```

Loan:

```java
@Component
public class LoanEligibilityStrategy
        implements ProductEligibilityStrategy {

    @Override
    public EligibilityResult evaluate(
            Customer customer,
            ProductApplication application) {

        // loan-specific rules
        return result;
    }
}
```

Credit Card:

```java
@Component
public class CreditCardEligibilityStrategy
        implements ProductEligibilityStrategy {

    @Override
    public EligibilityResult evaluate(
            Customer customer,
            ProductApplication application) {

        // card-specific rules
        return result;
    }
}
```

Then a factory/registry selects the strategy:

```text
Product Type
     ↓
Strategy Factory / Registry
     ↓
LOAN → LoanEligibilityStrategy
CARD → CreditCardEligibilityStrategy
```

### Why?

Because:

**Common onboarding flow + Product-specific strategy**

is cleaner than:

**One giant onboarding service + huge if/else blocks**

---

# 7. Why Not Put Everything Inside Onboarding Service?

### Interview answer

> Onboarding-service should orchestrate the common customer onboarding lifecycle rather than becoming tightly coupled to every financial product. Product-specific rules should remain behind separate strategies or modules. This gives us better separation of concerns and makes it easier to add another product without changing the core onboarding flow extensively.

### Responsibility separation

```text
Controller
   ↓
Service / Orchestrator
   ↓
Business Rules / Strategy
   ↓
Repository
```

Controllers handle transport concerns.

Repositories handle persistence.

Business/domain services handle business decisions.

---

# 8. Failure Scenarios

## Scenario 1 — KYC fails

```text
KYC
 ↓
FAILED
 ↓
Do NOT continue product flow
```

Interview answer:

> If KYC fails, the product-specific journey should not proceed to the next business stage. We persist the appropriate failure state and provide a controlled retry or manual-review path depending on the business rules.

---

## Scenario 2 — KYC passes but eligibility fails

```text
KYC = PASS
     ↓
Eligibility = FAIL
     ↓
Product Application Declined
```

Important:

**KYC result and product eligibility result are different states.**

Interview answer:

> The customer can remain KYC verified, but the particular product application can be marked as not eligible or declined. We should not overwrite the KYC status with the product decision.

---

## Scenario 3 — Customer creation succeeds but product creation fails

```text
Customer Created
       ↓
Product Creation Failed
       ↓
Retry Product Operation
```

Do NOT blindly create another customer.

Use:

- Idempotency
- Unique business identifiers
- State management
- Retry strategy
- Reconciliation where applicable

Interview answer:

> I would treat customer creation and product creation as separate state transitions and make the product operation retryable and idempotent. If customer creation has already succeeded, the retry should detect that state and continue from the appropriate point rather than creating a duplicate customer.

---

# 9. Idempotency — Very Important Financial-System Question

### Interviewer:

**“What if the user clicks Submit twice?”**

### Answer

> I would not rely only on frontend button disabling. The request should carry an idempotency key or unique business reference. The backend checks whether that operation has already been processed, and database-level unique constraints provide an additional protection against duplicate records.

Flow:

```text
Client
  ↓
Idempotency-Key
  ↓
Backend
  ↓
Check Existing Request
  ↓
Already Processed?
 ├── YES → Return Previous Result
 └── NO  → Process
```

Useful unique identifiers can include:

```text
application_id
idempotency_key
customer_reference
```

---

# 10. State Management

Do not keep everything as:

```text
status = FAILED
```

Instead, maintain meaningful states:

```text
INITIATED
    ↓
IDENTITY_VERIFIED
    ↓
KYC_COMPLETED
    ↓
ELIGIBILITY_PENDING
    ↓
ELIGIBLE
    ↓
CUSTOMER_CREATED
    ↓
PRODUCT_CREATED
    ↓
COMPLETED
```

Failure states:

```text
KYC_FAILED
ELIGIBILITY_FAILED
CUSTOMER_CREATION_FAILED
PRODUCT_CREATION_FAILED
MANUAL_REVIEW
```

### Why?

Because operationally we need to know:

> **Where exactly did the journey fail?**

This is also important for safe retry.

Example:

```text
KYC DONE
Customer CREATED
Product FAILED

Retry
  ↓
Do NOT redo KYC
Do NOT create customer again
Continue product operation
```

---

# 11. Security — Financial Domain

If asked:

### “How do you protect customer/financial data?”

Answer:

> We secure the application at multiple layers: TLS for data in transit, authentication and authorization through the organization's IAM/Spring Security mechanisms, least-privilege access, encryption at rest for sensitive data, secure secrets management, masking of sensitive information in logs, generic client-facing error messages, and audit trails for important customer-data operations.

Remember:

```text
TLS
 ↓
Authentication
 ↓
Authorization
 ↓
Least Privilege
 ↓
Encryption
 ↓
Secure Logging
 ↓
Audit Trail
 ↓
Secrets Management
```

### Never log:

- OTP
- Password
- Access token
- Full identity documents
- Unnecessary sensitive PII

### Prefer logging:

- applicationId
- correlationId
- productType
- workflow state
- operation
- timestamp
- result
- error code

---

# 12. Troubleshooting a Failed Onboarding Request

Use this exact sequence:

```text
Correlation ID
      ↓
API Logs
      ↓
Onboarding State
      ↓
Identity / KYC Response
      ↓
Product Eligibility
      ↓
Database State
      ↓
Downstream Service
      ↓
Failure Reason
```

### Interview answer

> I would trace the request end-to-end using the correlation ID, identify the last successful state transition, inspect the corresponding service logs and database state, and then determine whether the failure was caused by validation, a downstream dependency, persistence, or business rules.

---

# 13. Top Interview Questions + Ready Answers

## Q1. What exactly are you working on?

> I am currently working on extending an existing digital banking onboarding platform to support financial-product journeys such as credit cards and loans. My focus is on reusing the existing identity, KYC and compliance capabilities while keeping product-specific eligibility and validation logic isolated from the common onboarding flow.

## Q2. Why extend the existing platform?

> Identity verification, KYC, document processing and compliance are common capabilities across multiple products. Reusing them avoids duplication and provides a consistent onboarding journey, while product-specific rules remain isolated.

## Q3. What is the difference between KYC and eligibility?

> KYC establishes and verifies the customer's identity and required information. Eligibility determines whether that verified customer satisfies the business and product-specific criteria for a particular financial product.

## Q4. What if the applicant is not an existing customer?

> The journey can start without assuming an existing customer relationship. We first perform identity, KYC and compliance checks. Once the business decision allows the journey to proceed, customer creation can take place before establishing the product relationship.

## Q5. How do you avoid duplicate product logic?

> I would isolate product-specific behavior using a strategy or similar extensibility pattern. Common onboarding functionality remains shared, while each product provides its own eligibility and validation implementation.

## Q6. Why Strategy Pattern?

> The behavior varies by product while the calling flow remains consistent. Strategy allows us to add or modify product-specific rules without introducing large conditional blocks into the core onboarding service.

## Q7. What happens if KYC fails?

> The product-specific journey should not proceed. We persist the appropriate failure state and provide a controlled retry or manual-review path according to the business rules.

## Q8. What if KYC passes but loan eligibility fails?

> The customer can remain KYC verified, but the loan application is marked as not eligible or declined. These are separate business states.

## Q9. What if the user submits twice?

> We use backend idempotency using an idempotency key or unique business reference, with database constraints as an additional protection against duplicate records.

## Q10. What if customer creation succeeds but product creation fails?

> We persist the individual state transitions and make product creation retryable and idempotent. The retry detects that the customer already exists and continues from the appropriate state.

## Q11. How would you add a new product?

> I would reuse the existing identity, KYC and compliance capabilities and introduce a new product-specific strategy or module containing its validation, eligibility and product-specific workflow. Ideally, the core onboarding orchestration requires minimal or no modification.

## Q12. Where should business rules reside?

> Business rules should reside in the service or domain layer rather than controllers or repositories. Controllers handle transport concerns, repositories handle persistence, and the service/domain layer owns business decisions.

## Q13. How do you secure financial/customer information?

> TLS, authentication and authorization, least privilege, encryption at rest, secure secrets management, sensitive-data masking, generic error responses and audit trails.

## Q14. What would you log?

> I would log safe operational identifiers such as applicationId and correlationId, along with product type, workflow state, operation, timestamp, result and error code. I would avoid logging OTPs, passwords, access tokens, full documents and unnecessary PII.

## Q15. How would you troubleshoot a failed onboarding request?

> I would use the correlation ID to trace the request end-to-end, identify the last successful state transition, inspect service logs and database state, check downstream responses, and determine whether the issue is validation, persistence, dependency-related or a business-rule failure.

---

# 14. Cross-Question Drill — Interviewer Can Go Deeper

### “Why shouldn't KYC service contain loan eligibility?”

Because KYC is a reusable customer-verification capability. Putting loan-specific rules inside KYC couples a common service to one product and makes reuse harder.

### “Why not create the customer before KYC?”

Because customer creation should follow the required verification, compliance and business decision according to the journey. Creating records too early can create unnecessary or invalid customer records.

### “Does eligibility failure mean KYC should be rolled back?”

No. KYC and product eligibility represent different business states. If KYC is valid but a particular product is not eligible, the KYC result does not need to be undone merely because that product was declined.

### “If product creation fails, do you rollback customer creation?”

Do not automatically claim a distributed transaction. Treat them as explicit state transitions and use idempotent retry/recovery. If business requirements require compensation, a compensating action can be defined, but it should be based on the actual business semantics.

### “Does retry mean running the whole flow again?”

No. Retry from the appropriate persisted state.

```text
KYC DONE
Customer CREATED
Product FAILED
        ↓
Retry Product
```

Do not repeat successful operations unnecessarily.

### “How would you add Personal Loan?”

Reuse:

```text
Identity
KYC
AML / Compliance
Document Processing
Customer Management
```

Add:

```text
PersonalLoanEligibilityStrategy
PersonalLoanValidation
PersonalLoanFlow
```

Avoid rewriting the complete onboarding system.

---

# 15. 30-Second Closing Answer

If the interviewer asks:

**“Summarize your contribution.”**

Use:

> My current contribution is around extending an existing digital banking onboarding platform for product-specific financial journeys. The main focus is to reuse common identity, KYC and compliance capabilities while keeping product-specific eligibility and validation isolated. From an engineering perspective, I focus on clean separation of responsibilities, state management, idempotency, failure recovery and secure handling of customer data. The overall goal is to make the platform extensible so that additional financial products can be introduced without duplicating the complete onboarding flow.

---

# 16. The 6 Anchors — Memorize These

During cross-questioning, keep bringing your answers back to these six concepts:

```text
1. Common Capability
        ↓
2. Product-Specific Rules
        ↓
3. KYC ≠ Eligibility
        ↓
4. New vs Existing Customer
        ↓
5. State + Idempotency
        ↓
6. Failure + Recovery
```

## Golden line

> **Reuse common onboarding capabilities while isolating product-specific business rules.**

## Five traps

```text
KYC ≠ Eligibility

New customer ≠ immediately create customer

Retry ≠ repeat everything

Frontend duplicate prevention ≠ backend idempotency

Product-specific logic ≠ giant if/else
```

---

# 17. Important Interview Credibility Rule

Do not say:

> “I designed the entire banking architecture.”

Say:

> “I am working on the implementation and extension of the existing platform, particularly around product-specific onboarding capabilities.”

This keeps the answer technically strong while accurately representing your contribution.

---

# 18. Final 60-Second Revision

```text
CURRENT WORK
    ↓
Extend existing digital banking onboarding
    ↓
Support Credit Card / Loan journeys

COMMON
    Identity
    KYC
    AML / Compliance
    Document Processing

PRODUCT-SPECIFIC
    Eligibility
    Validation
    Risk / Business Rules
    Product Decision
    Product Creation

DESIGN
    Common Onboarding
          +
    Strategy / Product Module

NEW CUSTOMER
    Identity
      ↓
    KYC
      ↓
    Compliance
      ↓
    Business Decision
      ↓
    Customer Creation
      ↓
    Product

RELIABILITY
    State Management
    Idempotency
    Safe Retry
    Failure Recovery

SECURITY
    TLS
    Authorization
    Least Privilege
    Encryption
    Masking
    Audit

TROUBLESHOOTING
    Correlation ID
      ↓
    Logs
      ↓
    State
      ↓
    DB
      ↓
    Downstream
      ↓
    Root Cause
```

**Core message:**

> I am extending the existing onboarding platform, not rebuilding it. I reuse common onboarding capabilities and isolate product-specific business rules so the platform remains maintainable, extensible and reliable.
