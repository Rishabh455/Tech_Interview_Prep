# Infosys Interview — Complete Question Bank

> Combined question bank extracted from both interview transcripts. Questions are grouped by transcript part and topic; repeated prompts within each part have been consolidated. Questions only, no answers.

---

## Part 1 — Interview Transcript 1

> Source: `infosisinterviewpart1.txt` transcript. Questions are cleaned for readability and grouped by topic. Repeated/near-identical prompts are consolidated; coding tasks and follow-up questions are retained. This file contains questions only, not answers.

## 1. Introduction, Experience, and Work Profile

1. Tell me about yourself.
2. Where are you currently located, and which company are you working for?
3. Are you going to the office daily, or how are you managing your work arrangement?
4. You are a Full Stack Java Developer, right?
5. Which cloud platforms have you worked on?
6. How did you reduce the release cycle by 30%?
7. How good are you at programming logic? How would you rate yourself at writing logic?
8. How does HackerRank work, and what ranking/stars do you have on it?
9. Which Java version(s) have you worked on?
10. Have you used the latest Java versions/features?
11. Can you explain the current status of the Java and Angular migrations in your projects?

## 2. Java Coding: String Compression and Character Counts

12. **Coding task:** Given a string, count consecutive occurrences of each character in order and return the compressed string. Example: `aabcccccaaa` → `a2b1c5a3`.
13. What approach would you use before writing the string-compression code?
14. How would you handle a null or empty input string?
15. While iterating through the string, should you compare the current character with the next character or the previous character?
16. When should the counter stop increasing and the current character/count be appended?
17. Why is the Java string-compression code not compiling, and how can you fix it?
18. Why does the code give `cannot find symbol` for the `compress` method, and how do you fix it?
19. **Related coding prompt:** How would you write Java code to count the occurrences of each character/letter in a string while preserving the order?

### Code snippet mentioned in the transcript

```java
String s = "aabcccccaaa";
// Expected compressed output: a2b1c5a3
```

## 3. Authentication, JWT, and Security

20. Have you implemented authentication in your application? If so, how did you implement it, and which technologies did you use?
21. Where is a JWT token validated, and where is it stored in your application?
22. What is the difference between a session token and an ID token?
23. How do you secure a REST API in Spring Boot?

## 4. Spring Boot and Performance

24. What problems does Spring Boot solve compared with the traditional Spring framework?
25. What improvements did you get after implementing Spring Boot?
26. How can you optimize the performance of a Spring Boot application?
27. How do you implement caching?
28. What is Redis?
29. In what form does Redis store data?
30. Is Redis a database? Where and how is its data physically stored?
31. What database-query optimizations have you used?
32. How do you implement asynchronous execution for a method in Spring Boot?
33. If you use `@Async` on a method, what else needs to be configured or used?
34. What other annotation/keyword do you use with `@Async`?
35. Give a real-life scenario in which you would use asynchronous execution.
36. What is the difference between `RestTemplate` and `WebClient`?
37. When would you use `WebClient` instead of `RestTemplate`?
38. What is a circuit breaker, and why do we use it?
39. How do you implement a circuit breaker?
40. Which annotation/keyword is used for a circuit-breaker fallback?
41. What is Spring Boot Actuator?
42. Which Spring Boot Actuator endpoints have you used, and why?
43. What is the difference between JPA and Spring Data JPA in terms of data handling?
44. What is Spring Data JPA?

## 5. Closing / Candidate Questions

45. Do you have any questions for us?
46. Are you comfortable with the Hyderabad-based opportunity and the stated work-from-office arrangement?
47. Are you willing to relocate to Hyderabad?

## Transcript notes

- The transcript contains duplicate versions of several questions; those have been combined above.
- Greetings, screen-sharing instructions, and other non-interview chatter were not treated as technical interview questions.
- Questions are extracted from the transcript; no outside questions have been added.


---

## Part 2 — Interview Transcript 2

> Source: `infosisinterviewpart2.txt` transcript. Questions are cleaned for readability and grouped by topic. Repeated/near-identical prompts are consolidated; coding tasks and follow-up questions are retained. This file contains questions only, not answers.

## 1. Introduction, Experience, and Role

1. What is microservices?
2. How much experience do you have in Java?
3. Can you explain microservices architecture?
4. What is LDAP? (The transcript’s speech recognition renders this once as “LDAB”.)
5. Are you primarily a front-end developer or a back-end developer?
6. Have you worked on microservices?
7. How many years of total experience do you have?
8. How much experience do you have with Angular?
9. Which Angular versions have you worked with?

## 2. Core Java

10. What is an immutable object in Java, specifically an immutable `String`?
11. How many objects will be created in the given Java code?

    ```java
    String s = "Java Technology";
    String s1 = new String("Java Technology");
    String s2 = "Java Technology";
    ```

12. What are the ways to create threads in Java multithreading?
13. What are the `transient` and `volatile` keywords in Java?
14. Which features were introduced in Java 8?
15. What is the L in the SOLID principles? Explain the Liskov Substitution Principle.
16. Do you know any design patterns? Which ones have you used or can you explain?

## 3. Java Coding Tasks

17. **Coding task:** Find the unique values in an array in Java.
18. **Coding task:** Given a list of integers, use the Java 8 Stream API to find the occurrence/frequency of each number.
19. Can you complete the code to find and print the occurrences of each number in a list using the Java Stream API?
20. How would you print the frequency-map entries, for example using `Map.entrySet()`?

## 4. Angular Migration and Fundamentals

21. Can you explain your experience migrating Angular versions, especially standalone components and newer libraries such as Signals?
22. What is an Angular lifecycle hook?
23. What is Angular Router, and why is it powerful?
24. Can you explain Angular Router and how you use it in your application?
25. Can you share your screen and demonstrate how you use Angular concepts in your project?
26. What is NgRx in Angular?
27. How do you define and initialize state management using NgRx?
28. What is the role of the Store in Angular/NgRx?
29. What is an Auth Guard in Angular?
30. What is the difference between `Subject` and `BehaviorSubject` in RxJS?
31. Can you pass a value/parameter to a `BehaviorSubject`? How?

## 5. Spring Boot

32. How do you read values from `application.properties` into a Java class in Spring Boot?
33. What is the purpose of the `@Qualifier` annotation in Spring Boot?

## 6. Microservices and Resilience

34. How do two microservices communicate with each other?
35. Do you know about rate limiting and retries in microservices?
36. Have you worked on microservices, and can you describe that experience?

## 7. Azure

37. What are the types of storage accounts in Azure?

## 8. Closing / Candidate Questions

38. Do you have any questions for us?
39. Is there anything else you would like to know about the role, project, or next steps?

## Transcript notes

- Repeated questions about microservices, thread creation, Angular Router, NgRx, and Java Stream frequency counting have been consolidated.
- The transcript contains some AI-generated “no question provided” entries and meeting/screen-sharing chatter; these were excluded because they are not interview questions.
- The speech-to-text transcript has occasional recognition errors. Wording has been normalized where the intended technical term is clear.
- Questions are extracted from the transcript; no outside questions have been added.
