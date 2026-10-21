# My Current Work & Contribution

**Topic:** Extending a Digital Banking Onboarding Platform\
**Purpose:** Quick technical revision and clear explanation of current
responsibilities

> **Positioning:** I am extending an existing onboarding platform. I am
> not claiming that I designed the entire banking architecture. Explain
> implementation details as hands-on experience only where they match
> your actual work.

------------------------------------------------------------------------

## 1. 3-Minute Current Work Explanation

### 0:00--0:30 --- Business context

"I am currently working on extending an existing digital banking
onboarding platform to support financial-product journeys such as credit
cards and loans. The key objective is to reuse common onboarding
capabilities instead of building a separate end-to-end flow for every
product."

### 0:30--1:15 --- Common vs product-specific capabilities

"Identity verification, KYC, AML/compliance and document processing are
common capabilities. However, eligibility, validation, risk rules and
the final product decision differ between a loan and a credit card. The
design principle is to reuse common onboarding capabilities while
keeping product-specific business rules separate."

**Remember:**

-   **Common:** Identity, KYC, AML/compliance, document processing,
    customer verification.
-   **Product-specific:** Eligibility, validation, risk/business rules,
    product decision and product creation.

### 1:15--2:15 --- My technical contribution

"My current focus is on extending the existing onboarding flow for
product-specific journeys. The common onboarding service coordinates the
lifecycle, while product-specific logic can be separated into individual
strategies or modules. This avoids putting every product's rules into
one large service or a long chain of `if/else` statements.

I also focus on reliable workflow handling, including persisted state,
idempotency, controlled retries and failure recovery. These are
important because a customer or product creation request must not create
duplicate records if a request is submitted twice or a downstream call
times out."

### 2:15--3:00 --- Example and outcome

"For example, a customer may pass KYC but fail loan eligibility. In that
case, the KYC result remains valid, while the loan application is marked
as not eligible. These are separate business states.

Similarly, if customer creation succeeds but product creation fails, a
retry should continue from the product-creation step instead of creating
the customer again. The overall goal is to make the platform easier to
extend, maintain and recover when failures occur."

------------------------------------------------------------------------

## 2. Architecture at a Glance

``` text
Customer / Client
       |
       v
Onboarding Service / Orchestrator
       |
       +--> Identity Verification
       +--> KYC / Document Processing
       +--> AML / Compliance
       |
       v
Product Decision Layer
       |
       +--> Loan Eligibility / Validation
       +--> Credit Card Eligibility / Validation
       |
       v
Customer / Product Creation
```

**Core design principle:** Keep common onboarding capabilities reusable
and product-specific rules isolated.

### Who owns what?

  -----------------------------------------------------------------------
  Component                           Responsibility
  ----------------------------------- -----------------------------------
  Controller                          HTTP request/response handling

  Onboarding service/orchestrator     Coordinates the onboarding
                                      lifecycle

  Identity/KYC/compliance services    Common verification capabilities

  Product strategy/domain service     Product-specific eligibility and
                                      business rules

  Repository                          Database persistence
  -----------------------------------------------------------------------

------------------------------------------------------------------------

## 3. Key Concepts to Explain Clearly

### KYC vs Product Eligibility

-   **KYC:** "Who is this customer, and have the required identity
    checks been completed?"
-   **Eligibility:** "Does this verified customer satisfy the criteria
    for this particular product?"

**Interview line:** "KYC completion is not the same as product approval.
A customer can be KYC verified but not eligible for a specific loan or
credit card."

### Strategy Pattern --- Why is it useful?

Without separation, the onboarding service can accumulate large
`if/else` blocks for every product.

With a strategy-based design:

``` text
Product Type
     |
     v
Strategy / Registry
     |
     +--> LoanEligibilityStrategy
     +--> CreditCardEligibilityStrategy
     +--> FutureProductStrategy
```

**Interview line:** "Strategy lets each product implement its own
eligibility rules behind a common contract. We can add or change
product-specific behavior without extensively changing the core
onboarding flow."

Use this as a design explanation; do not claim a Strategy implementation
was deployed unless that reflects the actual code.

### New Customer vs Existing Customer

**New-to-bank customer:**

`Identity → KYC → Compliance → Business Decision → Customer Creation → Product Relationship`

Do not assume customer creation happens immediately when an applicant is
not found. Required verification and business checks must happen first,
according to the workflow.

### State Management

Possible successful lifecycle:

`INITIATED → IDENTITY_VERIFIED → KYC_COMPLETED → ELIGIBILITY_PENDING → ELIGIBLE → CUSTOMER_CREATED → PRODUCT_CREATED → COMPLETED`

Possible failure states:

`KYC_FAILED`, `ELIGIBILITY_FAILED`, `CUSTOMER_CREATION_FAILED`,
`PRODUCT_CREATION_FAILED`, `MANUAL_REVIEW`

**Why persist states?** To identify the last successful step, prevent
invalid transitions, support recovery and retry only the appropriate
operation.

### Idempotency

If a user clicks Submit twice, frontend button disabling alone is not
enough.

``` text
Request + Idempotency Key
          |
          v
Check Existing Operation
     /           \
Already done     New request
     |               |
Return result     Process once
```

**Interview line:** "Backend idempotency, a stable business reference
and database uniqueness constraints help prevent duplicate customer or
product records."

### Failure and Recovery Scenarios

  -----------------------------------------------------------------------
  Scenario                            Correct handling
  ----------------------------------- -----------------------------------
  KYC fails                           Do not continue the product
                                      journey; persist failure state and
                                      follow the defined
                                      retry/manual-review path

  KYC passes, eligibility fails       Keep KYC status separate; mark the
                                      specific product application as not
                                      eligible/declined

  Customer creation succeeds, product Retry the product operation; do not
  creation fails                      blindly create the customer again

  Downstream request times out        A timeout does not prove the
                                      operation failed; check state and
                                      use idempotency before retrying

  User submits twice                  Detect duplicate operation on the
                                      backend
  -----------------------------------------------------------------------

**Golden principle:** Retry from the appropriate persisted state; do not
blindly rerun the whole flow.

------------------------------------------------------------------------

## 4. Security and Troubleshooting

### Protecting customer and financial data

Key controls to understand:

-   TLS for data in transit.
-   Authentication and authorization.
-   Least-privilege access.
-   Encryption at rest where required.
-   Secure secrets management.
-   Masking sensitive information in logs.
-   Audit trails for important customer-data operations.

**Never log:** OTPs, passwords, access tokens, full identity documents
or unnecessary sensitive PII.

**Prefer safe operational metadata:** `applicationId`, `correlationId`,
`productType`, workflow state, operation, timestamp, result and error
code.

### Troubleshooting a failed request

``` text
Correlation ID
      ↓
API / Service Logs
      ↓
Last Successful Workflow State
      ↓
Downstream Response
      ↓
Product Eligibility / Validation
      ↓
Database State
      ↓
Root Cause
```

**Interview line:** "I trace the request using the correlation ID,
identify the last successful state transition, inspect service logs and
downstream responses, verify the database state, and determine whether
the issue is validation, persistence, a dependency or a business rule."

------------------------------------------------------------------------

## 5. Likely Follow-Up Questions --- Short Answers

### Q1. What are you currently working on?

"I am working on extending an existing digital banking onboarding
platform for product-specific journeys such as loans and credit cards.
The focus is on reusing identity, KYC and compliance capabilities while
keeping product-specific eligibility and validation separate."

### Q2. Why not put every product rule in the onboarding service?

"That would tightly couple the core onboarding flow to every product.
Keeping product-specific rules in separate strategies or modules
improves separation of concerns and makes future products easier to
add."

### Q3. What happens if KYC passes but loan eligibility fails?

"KYC remains completed, but the loan application can be marked as not
eligible. KYC and product eligibility represent different business
decisions."

### Q4. How would you add a new product?

"Reuse common onboarding capabilities and add a product-specific module
or strategy for its validation, eligibility and product workflow, with
minimal changes to the core orchestration."

### Q5. What if customer creation succeeds but product creation fails?

"Persist both state transitions separately and make product creation
safely retryable. On retry, detect the existing customer and continue
from product creation rather than creating a duplicate customer."

### Q6. How do you prevent duplicate submissions?

"Use backend idempotency with an idempotency key or stable business
reference, reinforced by database-level uniqueness constraints where
appropriate."

### Q7. Why not retry the entire workflow?

"Some steps may already have succeeded. We should use persisted state
and retry only the appropriate operation to avoid duplicate processing."

### Q8. How do you debug a production failure?

"I use the correlation ID to trace the request, find the last successful
state, check logs and downstream responses, inspect validation results
and verify the database state."

### Q9. How do you protect customer data?

"Use authentication, authorization, least privilege, encryption, secure
secret handling, sensitive-data masking and audit trails. Avoid logging
credentials, tokens or full identity documents."

### Q10. What is your contribution, specifically?

"My focus is on extending the existing onboarding capability for
product-specific journeys, reusing common verification capabilities and
keeping eligibility, validation, state management and failure recovery
well separated."

------------------------------------------------------------------------

## 6. Quick Writing Points for Revision

Write these headings on paper and recall one or two lines for each:

1.  **Business goal:** Extend existing onboarding for loans and credit
    cards.
2.  **Reuse:** Identity + KYC + AML/compliance + document processing.
3.  **Separate:** Product eligibility + validation + risk/business
    rules.
4.  **Design:** Common orchestration + product-specific strategy/module.
5.  **KYC ≠ Eligibility:** Verified customer can still fail product
    eligibility.
6.  **New customer:** Identity → KYC → Compliance → Decision → Customer
    creation.
7.  **State:** Persist each meaningful workflow transition.
8.  **Idempotency:** Prevent duplicate customer/product creation.
9.  **Recovery:** Retry only the failed operation from the correct
    state.
10. **Security:** Protect sensitive data and avoid unsafe logging.
11. **Debugging:** Correlation ID → logs → state → downstream →
    database.
12. **Outcome:** Extensible, maintainable and more reliable onboarding.

------------------------------------------------------------------------

## 7. 30-Second Summary

"My current work is focused on extending an existing digital banking
onboarding platform for financial-product journeys. The key design is to
reuse common identity, KYC and compliance capabilities while isolating
product-specific eligibility and validation. I also consider state
management, idempotency, safe retries, failure recovery and secure
handling of customer data so the platform can support new products
without duplicating the complete onboarding flow."

## Final Memory Hooks

-   **Reuse common capabilities.**
-   **Isolate product-specific rules.**
-   **KYC is not eligibility.**
-   **Persist workflow state.**
-   **Idempotency prevents duplicates.**
-   **Retry from the correct state.**
-   **Secure customer data.**
-   **Trace failures with correlation IDs.**
