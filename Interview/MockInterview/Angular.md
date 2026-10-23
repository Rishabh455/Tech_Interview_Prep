In my current banking onboarding project, I worked on an Angular-based operations dashboard for the operations team.

The main purpose of this dashboard was to display customers whose identity documents required manual review or whose KYC verification was pending.

My primary responsibility was integrating the Angular frontend with Spring Boot REST APIs. The dashboard fetched customer records from the backend and allowed the operations team to view and filter applications based on their verification status.

One of the challenges we faced was slow data loading. Initially, the backend API took around one to two seconds to display the results. During investigation, we identified that the database query was taking approximately 200 to 300 milliseconds.

I analyzed the query execution plan using EXPLAIN and identified an opportunity to optimize the filtering conditions. We introduced a composite index on the review status and creation timestamp, then validated the execution plan and query performance again.

The database query execution time decreased to approximately 40 to 50 milliseconds in our tested environment. We also performed functional and regression testing to ensure that the dashboard displayed the correct customer records.

Although my main contribution was on the Java backend side, this feature gave me practical experience in Angular API integration, displaying workflow states, and troubleshooting performance issues across the frontend and backend.

The key outcome was a more responsive dashboard that helped the operations team access pending verification cases more efficiently.
Here are the most important Angular frontend follow-up questions for your banking onboarding dashboard. These answers are in direct, interview-ready English and based on your stated experience: your main responsibility was backend development and Angular API integration, not complex frontend development.

## 1. Angular frontend — rapid interview preparation

Spend around 5–8 minutes reading these answers aloud.

### Q1. What exactly did you develop in Angular?

I worked on an operations dashboard that displayed customer applications requiring manual review or pending KYC verification. My primary contribution was integrating the Angular frontend with Spring Boot REST APIs and ensuring the correct customer data and verification statuses were displayed.

### Q2. How did Angular communicate with the backend?

We used REST APIs to communicate between Angular and Spring Boot. Angular's `HttpClient` can send HTTP requests such as GET and POST, receive JSON responses, and display the returned data in the dashboard.

### Q3. What is an Angular service, and why is it used?

An Angular service encapsulates reusable business or data-access logic. For API integration, we can keep HTTP calls in a service and inject that service into a component, improving code reuse and separation of concerns.

### Q4. What is the difference between a component and a service?

A component manages a part of the UI and its interactions. A service handles reusable logic, such as fetching customer data from backend APIs. Components can use dependency injection to access services.

### Q5. How did you handle API errors?

API errors can be handled using RxJS `catchError` or component-level error handling. We can display a user-friendly message, log the error appropriately, and provide a retry option where suitable. I would describe the exact implementation based on the code I worked on.

### Q6. How did you display pending KYC and manual-review records?

The frontend fetched customer records from the backend API and displayed the relevant status fields. Filtering could be handled through API query parameters or frontend filtering, depending on the implementation. For large datasets, backend filtering and pagination are generally preferable.

### Q7. What is an Observable in Angular?

An Observable represents a stream of asynchronous data. Angular's `HttpClient` returns Observables, which we can subscribe to or process using RxJS operators to handle API responses and errors.

### Q8. What is dependency injection?

Dependency injection is a design pattern in which a class receives its dependencies instead of creating them itself. Angular uses dependency injection to provide services to components, making the application easier to maintain and test.

### Q9. What is an HTTP interceptor?

An HTTP interceptor intercepts outgoing HTTP requests and incoming responses. It can attach an authorization token, add common headers, or handle common errors. If JWT authentication was implemented in the project, the interceptor could attach the token to API requests.

### Q10. How did you improve dashboard performance?

We investigated the slow data-loading path and examined the database query execution plan. A composite index on the relevant review-status and creation-time columns was introduced to improve filtering performance. The reported query execution time decreased from approximately 200–300 milliseconds to 40–50 milliseconds in the tested environment. This was a database-query improvement, not a claim that the entire UI rendered in 40–50 milliseconds.

## 2. Technical follow-ups interviewers may ask

### Q11. What is data binding in Angular?

Data binding connects component data with the HTML template. Angular supports interpolation, property binding, event binding, and two-way binding.

### Q12. What is the difference between `*ngIf` and `*ngFor`?

`*ngIf` conditionally displays an element, while `*ngFor` iterates over a collection to render multiple elements. In newer Angular versions, equivalent control-flow syntax such as `@if` and `@for` is also available.

### Q13. How would you implement pagination for customer records?

I would pass page number and page size to the backend API. The backend would fetch only the required records and return pagination metadata. This avoids loading the entire dataset into the browser.

### Q14. What is the difference between client-side and server-side filtering?

Client-side filtering processes data already loaded in the browser. Server-side filtering sends filter criteria to the backend, which returns matching records. Server-side filtering is generally better for large datasets.

### Q15. How would you secure an Angular application?

Angular can attach authentication tokens to API requests and control the user interface based on permissions. However, frontend checks are not sufficient for security. The backend must validate authentication and authorization for every protected API.

### Q16. Where is a JWT validated?

The backend validates the JWT's signature, expiry, and relevant claims. In a Spring Boot application, Spring Security can perform this validation. An Angular interceptor may attach the token, but it should not be treated as the trusted security boundary.

### Q17. What happens if the backend API is slow?

I would check browser network timings, API response time, backend logs, and database query performance to identify the bottleneck. Then I would optimize the responsible layer rather than assuming the issue is in Angular.

### Q18. How would you test Angular API integration?

I would verify successful responses, empty results, error responses, and correct rendering of customer statuses. Angular services can also be unit-tested using HTTP testing utilities to mock backend responses.

### Q19. What is the difference between Angular and AngularJS?

AngularJS is the older JavaScript-based framework, while modern Angular is built around TypeScript and a component-based architecture. Modern Angular also provides stronger tooling and newer features for building and maintaining large applications.

### Q20. Was your frontend work complex?

My primary expertise is Java backend development. On the frontend, I mainly worked on REST API integration and dashboard data flow. My contribution was more focused on backend logic and performance troubleshooting than on advanced UI design.

## 3. Three questions about your database optimization story

These are particularly important because the interviewer may challenge the performance numbers.

Q21. How did you identify the slow query?

I investigated the dashboard's data-loading path and examined the SQL query and its execution plan using `EXPLAIN`. This helped identify an opportunity to optimize filtering on review status and creation time.

Q22. Why did you create a composite index?

The query filtered records using review status and creation time. A composite index on those columns can help the database locate matching records more efficiently, depending on the query and data distribution.

Q23. How did you verify the improvement?

I compared the query execution time and execution plan before and after adding the index in the tested environment. The query time decreased from approximately 200–300 milliseconds to 40–50 milliseconds. I would distinguish this measurement from the overall API response time.

## 4. Final 30-second revision

Remember these five points:

- Component: Handles the UI.
- Service: Handles reusable logic and API calls.
- HttpClient: Sends HTTP requests to the backend.
- Interceptor: Can attach tokens and handle common request/response logic.
- Performance: Check browser timings, API latency, database execution plans, and indexes separately.

Important: Don't claim you implemented advanced Angular features if you mainly handled API integration. A clear, honest answer about your actual contribution is stronger than an answer you cannot defend in follow-up questions.