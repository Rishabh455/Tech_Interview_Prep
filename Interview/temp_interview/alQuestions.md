# All Interview Questions Organized by Topic (Across All Three Transcripts)

Below is a complete consolidated list of every question asked across the three interview transcripts, reorganized **by topic** rather than by transcript. This makes it easier to prepare topic-wise.

---

## 1. JAVA CORE

### 1.1 Java Basics & OOP

1. **Can you tell me the difference between function overloading and overriding?** (Transcript 3)
2. **How do you achieve abstraction in Java?** (Transcript 3)
3. **What is the difference between interface and abstract classes?** (Transcript 3)
4. **Can you tell me which inheritance Java does not support? And the reason?** (Transcript 3)
5. **Why?** (Transcript 3)
6. **What is exactly the diamond problem?** (Transcript 3)
7. **Can you elaborate on that?** (Transcript 3)

### 1.2 Multithreading

8. **Explain about multithreading in Java.** (Transcript 3 — AI practice round)
9. **What are the ways to define multithreading in Java?** (Transcript 3)

### 1.3 String Handling

10. **What is string constant pool in Java?** (Transcript 2)
11. **What is the difference between using the new keyword for creating a new string versus a string literal?** (Transcript 2)
12. **What is the difference between StringBuffer and StringBuilder?** (Transcript 2)

### 1.4 Java Streams / Reactive

13. **Difference between flatMap and map?** (Transcript 3)
14. **Can you just show me an example of flatMap? Can you share your screen on a notepad and just show me how do you write a flatMap?** (Transcript 3)
15. **Can you do the same for list of list?** (Transcript 3)
16. **What is the difference between observables and promises?** (Transcript 3)

---

## 2. SPRING BOOT / SPRING CORE

17. **Why do we choose Spring Boot over Spring MVC?** (Transcript 2)
18. **Would you choose JDBC or Hibernate? Which one would you prefer?** (Transcript 2)
19. **How are beans created in Spring Boot?** (Transcript 2)
20. **What is the difference between @Component, @Service, and @Repository?** (Transcript 2)
21. **Do you know what a singleton design pattern is?** (Transcript 2)
22. **Can you explain how we can create a singleton design pattern?** (Transcript 2)
23. **Give a small example.** (Transcript 2)
24. **Will the constructor be private or public?** (Transcript 2)
25. **Have you worked on Spring Security?** (Transcript 3)
26. **Have you ever implemented transaction management as well?** (Transcript 1)

### 2.1 Spring AOP

27. **So you say you have used AOP as well, right?** (Transcript 3)
28. **Do you know about Spring AOP — Aspect Oriented Programming?** (Transcript 3)
29. **What are the advices? Like how many advices, different types of advices we have in AOP?** (Transcript 3)
30. **Can you explain around advice?** (Transcript 3)
31. **Around and after throwing advice?** (Transcript 3)

### 2.2 JDBC & JPA

32. **Are you comfortable with JDBC? Because I see you have used JPA.** (Transcript 3)

---

## 3. MICROSERVICES / ARCHITECTURE

33. **Can you just provide me a walkthrough architecture of your Visitor Management System? How did you design the microservices layer, and what are the Spring Boot components you have used here?** (Transcript 1)
34. **How are those services communicating with each other? What are the protocols?** (Transcript 1)
35. **Have you ever built any kind of REST API?** (Transcript 1)
36. **How do you handle performance optimization if there is a heavy load? Or how do you identify bottlenecks?** (Transcript 1)

---

## 4. REST API CONCEPTS

37. **You've used REST APIs, right?** (Transcript 3)
38. **Can you tell me what are idempotent methods in REST API?** (Transcript 3)
39. **In REST, what is the difference between a query param and a path variable?** (Transcript 3)

---

## 5. DATABASE / SQL

### 5.1 General SQL

40. **What about your database expertise?** (Transcript 1)
41. **You have worked with PostgreSQL, right?** (Transcript 1)
42. **Have you worked on PL/SQL?** (Transcript 1)
43. **What database technologies have you worked on?** (Transcript 2)
44. **Are you aware of ODS, which is provided by Oracle, or RDS Oracle — RESTful database service?** (Transcript 2)
45. **Can you tell me the difference between truncate, delete, and drop?** (Transcript 3)
46. **Have you used views? Have you worked with views?** (Transcript 3)
47. **What is the difference between minus and intersect?** (Transcript 3)
48. **Can you tell me some constraints?** (Transcript 3)
49. **Can primary key be null and can primary key be composite?** (Transcript 3)
50. **So can a primary key be a composite key is what I'm asking?** (Transcript 3)

### 5.2 Indexing

51. **You mentioned about clustered and non-clustered index. What is the difference between these two?** (Transcript 1)
52. **Why do we need a non-clustered index if it is slow? Why can't we create just a clustered index? Why do we need non-clustered index?** (Transcript 1)
53. **Can you tell me the difference between clustered and non-clustered index?** (Transcript 2)

### 5.3 Procedures, Functions, Cursors, Triggers

54. **Can you tell me the difference between procedures and functions?** (Transcript 2)
55. **How do you get an output from a procedure?** (Transcript 2)
56. **What are cursors?** (Transcript 2)
57. **Can you tell me the difference between functions and procedures?** (Transcript 3)
58. **What about cursors and triggers?** (Transcript 3)
59. **Are you comfortable with debugging procedures and all?** (Transcript 3)
60. **Just want to know whether you can work on procedures or not — like do you have prior experience or you don't?** (Transcript 3)
61. **Not exactly debug — I just — you should be able to read the procedures and understand?** (Transcript 3)

---

## 6. PRODUCTION ISSUES / TROUBLESHOOTING

62. **Can you just provide me an incident from the past in which there was a production issue you investigated? How you investigated it?** (Transcript 1)
63. **How did you conduct and ensure security compliance? Is there any tool available within your firm, or how have you done this?** (Transcript 1)
64. **What about vulnerabilities within the third-party libraries which you are using?** (Transcript 1)
65. **Hypothetical scenario: One of our user research analysts calls you or drops an email that they need to upload financial data to GEARS, but they are not able to do so. They are getting some error in their Excel interface. On the backend, when you check servers, you don't see any error logs. How would you troubleshoot that?** (Transcript 1)
66. **What if the user or the RST member with whom we check this is also facing the same issue?** (Transcript 1)

---

## 7. CI/CD & DEVOPS

67. **You mentioned you reduced release cycle by 30% with CI/CD. Can you just provide or walk me through how you handle database migrations and deployment? Or maybe rollback?** (Transcript 1)
68. **You have written that you have built a CI/CD pipeline that reduces release cycle time by 30%. Can you explain what you have done here?** (Transcript 2)
69. **Have you worked on Groovy files or Gradle?** (Transcript 2)
70. **Have you used Jenkins pipeline or GitLab CI?** (Transcript 3)
71. **If there comes a situation like you have to make changes for vulnerabilities or you need to fix pipelines or all — so are you able to do that task or not?** (Transcript 3)

---

## 8. TESTING

72. **There is one more thing that I have written — automated regression validation using JUnit and Mockito, reducing manual efforts. So what manual efforts were reduced over here?** (Transcript 2)
73. **Was this automation developed by you?** (Transcript 2)
74. **Have you worked on writing unit test cases?** (Transcript 2)
75. **Can you explain what is TDD approach?** (Transcript 2)
76. **Have you ever used it in your development activity?** (Transcript 2)
77. **Have you used testing? JUnit and all?** (Transcript 3)

---

## 9. ANGULAR (FRONTEND)

78. **So you've worked with Java and Angular basically?** (Transcript 3)
79. **Can you tell me the decorators in Angular? Types of decorators?** (Transcript 3)
80. **What are annotations?** (Transcript 3)
81. **What is the function of pipes? Purpose of pipes?** (Transcript 3)
82. **Are you comfortable with writing pipes, right?** (Transcript 3)
83. **What's the difference between eager and lazy loading?** (Transcript 3)
84. **Eager and lazy loading — in Angular or Java?** (Transcript 3 — clarification)
85. **We have two types of compilation, right? Can you name them?** (Transcript 3)
86. **And what's the difference?** (Transcript 3)
87. **What is the use of a module? What all can be a part of module?** (Transcript 3)
88. **If I want to make a service available throughout the project — a service is a part of a module — how do you make it available throughout the project?** (Transcript 3)
89. **What is transpiling in Angular?** (Transcript 3)
90. **Can you just brief me about components, services, and everything? Like what are all the components? The structure of Angular?** (Transcript 3)
91. **I have an application running. But I don't — when we do route, the path changes in the link, right? The address. So if I don't want that, what should we do?** (Transcript 3)

---

## 10. LOGGING

92. **Have you used logging loggers in your projects?** (Transcript 3)
93. **There are multiple logging levels. Can you name them?** (Transcript 3)
94. **And order them by their — rank them, like which is the most critical logging and which is the least critical?** (Transcript 3)
95. **Rank them in descending order.** (Transcript 3)

---

## 11. PROJECT / HARDWARE INTEGRATION

96. **The project where the face recognition and thing was — how was the hardware development? How was the integration done with hardware and the software?** (Transcript 2)
97. **How was the data trained?** (Transcript 2)
98. **When you say you had installed Tomcat on the servers — was this project not using Spring Boot?** (Transcript 2)
99. **Why didn't you choose to use the embedded Tomcat provided by Spring Boot?** (Transcript 2)
100. **Once we package a Spring Boot application, we can directly run it as a jar file, right? Without using Tomcat as well. So why was that approach not used? What was the reasoning behind that?** (Transcript 2)
101. **Why container not used — containerization?** (Transcript 2)

---

## 12. AGILE / PROCESS

102. **You mentioned in your resume as well, you've worked on Agile, right? So what all things you are aware about Agile or what you actually implemented?** (Transcript 1)
103. **How long is your sprint?** (Transcript 1)
104. **After two weeks, do you go to production, or is it like you go after a longer time?** (Transcript 1)
105. **Once the sprint is completed, do you merge your changes and that gets released to production immediately, or it happens after, let's say, one month, two months?** (Transcript 1)

---

## 13. COMMUNICATION / STAKEHOLDER MANAGEMENT

106. **You mentioned that you have delivered 25+ demos to the stakeholders, right? So how do you communicate technical concepts to a non-technical user or maybe business users?** (Transcript 1)
107. **Have you worked in cross-region as well? Our development team is split between Japan and India. So how do you work cross-region?** (Transcript 1)
108. **In your day-to-day activity, do you work independently, or do you need a manager to guide you or tell you what needs to be done?** (Transcript 1)

---

## 14. BEHAVIORAL / EXPERIENCE

109. **Tell me one particular change that you have done in your three years of experience that you have learned a lot and you are somewhat proud of?** (Transcript 3)
110. **What would you call — what would be such a change or something?** (Transcript 3)
111. **So you have experience of working on new things and adapting?** (Transcript 3)
112. **Are you comfortable to stretch ten minutes if it takes?** (Transcript 3 — about interview time)

---

## 15. LEARNING & ADAPTABILITY

113. **You mentioned about learning new things. If you have to work on, say, Excel plugin or VSTO, would you be able to, would you be comfortable working on that?** (Transcript 1)
114. **The project that we are working on is mainly dependent on Oracle database, and we are primarily using RDS. Then we have Java Spring Boot on the backend, and we have an Excel add-in which is developed in VS C#. This is the tech stack that we are looking for. How open are you to learning all these things?** (Transcript 2)

---

## 16. AI TOOLS

115. **Are you currently using AI tools and all for development?** (Transcript 3)

---

## 17. INTRODUCTION / BACKGROUND

116. **Can you please explain about yourself?** (Transcript 1 — AI practice round)
117. **Can you just brief me about yourself? Your project?** (Transcript 1)
118. **Please introduce yourself.** (Transcript 2 — AI practice round)
119. **Can you explain to me something about the last project that you were working on?** (Transcript 2)
120. **What was your role in these three projects?** (Transcript 2)
121. **How good are you, and how can you rate yourself for database and Java?** (Transcript 2)
122. **Can you please tell me about yourself?** (Transcript 3 — AI practice round)
123. **Can you tell me about yourself? A bit about your experience and what does your day-to-day responsibilities look like?** (Transcript 3)

---

## 18. TEAM / PROJECT MANAGEMENT

124. **How big is your team size and how frequently you release your changes to production currently?** (Transcript 1)
125. **In this last project that you mentioned, within team, what was your role? So how big the role was and what was your contribution in that?** (Transcript 1)
126. **For your change, how many users use the application?** (Transcript 1)
127. **Can you just tell me more about that particular project? What exactly was the project on?** (Transcript 1)
128. **Was it a multi-application or was it generally working on single thread?** (Transcript 1)
129. **Did you have any senior guiding you, or was it primarily you who was looking at all this design and architecture?** (Transcript 1)

---

## 19. CURRENT EMPLOYMENT & LOGISTICS

130. **In your current — sorry, we are working currently for which firm?** (Transcript 1)
131. **And your client also — do you work for client or you do work for Accenture?** (Transcript 1)
132. **In the current — how many days you have to go to office?** (Transcript 1)
133. **Are you okay to be available in office as well, if required?** (Transcript 1)

---

## 20. CLOSING / FEEDBACK

134. **Do you have any questions for us?** (Transcript 1)
135. **How do you rate me and what should I improve?** (Transcript 1)
136. **Any improvement?** (Transcript 1)
137. **Anything else?** (Transcript 1)
138. **That's it from my side. Do you have any questions for me?** (Transcript 2)
139. **If I'll be getting into the project, what will be my roles and all?** (Transcript 2)
140. **If you have to give any improvements or anything, you can please suggest so that I can know.** (Transcript 2)
141. **I'm done from my side. Do you have anything you want to ask?** (Transcript 3)
142. **Did I get the correct things? What did you want to get, or should there be any improvement from my side?** (Transcript 3)
143. **How was I able to comfortably answer — is it justifiable for my three years of experience or not?** (Transcript 3)
144. **Any other suggestion to change it? How should I progress in other fields?** (Transcript 3)
145. **Anything else?** (Transcript 3)

---

## SUMMARY BY TOPIC

| Topic | Number of Questions |
|-------|---------------------|
| Java Core (OOP, Multithreading, String, Streams) | 16 |
| Spring Boot / Spring Core / AOP / JDBC-JPA | 16 |
| Microservices / Architecture | 4 |
| REST API Concepts | 3 |
| Database / SQL | 22 |
| Production Issues / Troubleshooting | 5 |
| CI/CD & DevOps | 5 |
| Testing | 6 |
| Angular (Frontend) | 14 |
| Logging | 4 |
| Project / Hardware Integration | 6 |
| Agile / Process | 4 |
| Communication / Stakeholder Management | 3 |
| Behavioral / Experience | 4 |
| Learning & Adaptability | 2 |
| AI Tools | 1 |
| Introduction / Background | 8 |
| Team / Project Management | 6 |
| Current Employment & Logistics | 4 |
| Closing / Feedback | 12 |
| **TOTAL** | **145** |

---

**Note:** Some questions were repeated, rephrased, or were follow-up probes by the interviewers. Questions marked as "AI practice round" were part of the AI practice session before the actual interview began. The total count may differ slightly from the previous summary due to re-categorization and merging of duplicate/rephrased questions.