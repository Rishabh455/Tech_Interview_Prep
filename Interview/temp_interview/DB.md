Absolutely. For a **Nomura Java Backend in-person interview**, I would prepare these as **spoken answers**, not textbook definitions. The answers below are designed to be delivered naturally in 30–60 seconds, with a short project connection and the likely follow-up.

# Nomura — JDBC + SQL 15–17 Minute Revision

## 29. Are you comfortable with JDBC? Because I see you have used JPA.

### Answer you should give

> “Yes, I am comfortable with JDBC fundamentals. In my recent projects, I primarily worked with JPA and Hibernate because they simplify ORM and database interaction, but I understand how JDBC works underneath.
>
> With JDBC, we typically use a Connection, PreparedStatement, execute the SQL, process the ResultSet, and properly close resources.
>
> One important advantage of PreparedStatement is that it supports parameterized queries and helps prevent SQL injection.
>
> In my onboarding project, most CRUD and transactional database operations were handled through JPA repositories, while understanding JDBC is useful when we need more control over SQL or database-specific operations.”

### Project follow-up

**“Why did you prefer JPA in your project?”**

> “Because our application had domain entities and relationships, and JPA reduced boilerplate code for CRUD operations while allowing us to work with entities and transactions cleanly.”

### Very likely cross-question

**“JPA vs JDBC?”**

> “JDBC is lower-level and gives direct control over SQL and database interaction. JPA is an ORM specification that maps Java objects to relational tables and reduces boilerplate. JPA is more convenient for typical enterprise CRUD, while JDBC can be preferred when very fine-grained SQL control or specific database operations are required.”

---

# 30. Difference between TRUNCATE, DELETE and DROP?

### Answer

> “DELETE, TRUNCATE and DROP are different in scope.
>
> `DELETE` removes rows from a table and can use a `WHERE` clause.
>
> `TRUNCATE` removes all rows from the table much more directly and does not support a `WHERE` clause.
>
> `DROP` removes the database object itself, including its structure.
>
> So, DELETE is for removing selected data, TRUNCATE is for clearing the table, and DROP is for removing the table itself.”

### Nomura/Oracle follow-up

**“Can you rollback DELETE and TRUNCATE?”**

Say:

> “DELETE is transactional and can generally be rolled back before commit. TRUNCATE is treated as DDL in Oracle and cannot be rolled back in the normal way.”

### Remember this table

| Command | Removes | WHERE | Object remains? |
|---|---|---|---|
| DELETE | Selected/all rows | Yes | Yes |
| TRUNCATE | All rows | No | Yes |
| DROP | Table + data | No | No |

---

# 31. Have you used Views? Have you worked with views?

### Answer

> “Yes, I understand database views. A view is a virtual table based on a SQL query. Instead of storing the result as independent data, the database stores the query definition and presents the result like a table.
>
> Views are useful for simplifying complex queries, exposing only required columns, and creating a controlled read layer for reporting or consumers.
>
> In an onboarding system, for example, a view could expose application status, customer reference and verification status without directly exposing all underlying tables and sensitive columns.”

### Follow-up

**“Why use a view instead of writing the query every time?”**

> “It centralizes the query logic and provides a reusable abstraction. It can also restrict which columns or rows are exposed.”

### Another follow-up

**“Is a view physically storing data?”**

> “A normal view generally does not store the result data physically; it stores the query definition. Materialized views are different because they persist the result.”

---

# 32. Difference between MINUS and INTERSECT?

### Answer

> “Both are set operators.
>
> `INTERSECT` returns rows that are common to both query results.
>
> `MINUS` returns rows that are present in the first query but not present in the second query.
>
> For example, if I have customers in system A and system B, INTERSECT gives customers present in both, while MINUS can give customers present in A but missing from B.”

### Important Nomura follow-up

**“Is MINUS available in PostgreSQL?”**

> “In Oracle, the operator is `MINUS`. In PostgreSQL, the equivalent set-difference operator is `EXCEPT`.”

### Easy memory trick

**INTERSECT = common**

**MINUS = first − second**

---

# 33. Can you tell me some constraints?

### Answer

> “The main SQL constraints are:
>
> `PRIMARY KEY` for uniquely identifying a row,
>
> `FOREIGN KEY` for maintaining relationships between tables,
>
> `UNIQUE` for preventing duplicate values,
>
> `NOT NULL` for preventing null values,
>
> and `CHECK` for enforcing a business condition.
>
> We use these constraints to maintain data integrity at the database level rather than relying only on application validation.”

### Project-specific angle

This is a very good line for your onboarding project:

> “For example, in an onboarding application, database constraints are useful for preventing duplicate application or verification records even if two requests arrive concurrently.”

### Follow-up

**“Why database constraint if Java validation already exists?”**

> “Application validation improves user experience, but database constraints provide the final integrity guarantee, especially during concurrent requests or when multiple services access the database.”

---

# 34–35. Can primary key be NULL? Can primary key be composite?

### Answer

> “A primary key cannot contain NULL values because its purpose is to uniquely identify every row.
>
> Yes, a primary key can be composite. A composite primary key consists of two or more columns whose combination uniquely identifies the row.”

### Example

```sql
PRIMARY KEY (customer_id, application_id)
```

> “Here, neither combination can be duplicated, and together the two columns identify the record.”

### Follow-up

**“Can a table have two primary keys?”**

> “No. A table can have only one primary key constraint, but that primary key can contain multiple columns.”

### Another follow-up

**“Primary key vs unique key?”**

> “A table has one primary key constraint, and primary-key columns cannot be NULL. A table can have multiple unique constraints, and null behavior for UNIQUE depends on the database.”

---

# 36. Difference between Functions and Procedures?

This one is particularly important because the interviewer may test whether you can work with Oracle-style database programming.

### Answer

> “A function is generally designed to return a value, whereas a procedure is mainly used to perform an operation.
>
> A function can take input parameters and return a result. A procedure can also take input parameters and can use OUT parameters to return values.
>
> Functions are commonly used when we need a calculated value, while procedures are suitable for performing a sequence of database operations.”

### Simple example

> “For example, a function could calculate an eligibility score or return a derived value, while a procedure could perform multiple updates related to an onboarding operation.”

### Follow-up

**“Can a function have OUT parameters?”**

For an Oracle interview, safest answer:

> “Functions normally communicate their main result through the RETURN value, although parameter modes and database-specific capabilities can vary. In practice, I use functions primarily when a value needs to be returned and procedures when an operation needs to be performed.”

### Very likely follow-up

**“Function or procedure — which one would you use for transaction-like processing?”**

> “For a multi-step database operation, I would generally prefer a procedure because the purpose is to execute an operation rather than simply calculate and return a value.”

---

# 37. What about Cursors and Triggers?

## Cursor

> “A cursor is used to process a query result row by row.
>
> There are implicit cursors managed automatically by the database and explicit cursors where we control the processing.
>
> Cursors are useful when each row requires procedural processing, but they should not be overused because set-based SQL operations are generally more efficient for bulk processing.”

### Cross-question

**“Why not use a cursor for everything?”**

> “Because row-by-row processing can be slower than a set-based SQL operation, especially for large datasets.”

---

## Trigger

> “A trigger is database code that executes automatically when a specified event occurs, such as INSERT, UPDATE or DELETE.
>
> For example, a trigger can automatically maintain an audit record when a sensitive table is updated.
>
> Triggers can be useful for enforcing certain database-level behavior, but I would avoid putting too much business logic in triggers because they can make application behavior harder to understand and debug.”

### Very likely follow-up

**“Would you use a trigger for onboarding business logic?”**

Say:

> “I would prefer keeping core onboarding business logic in the application/service layer because it is easier to test, version and maintain. I would use database triggers only for specific database-level concerns such as audit or integrity-related behavior where appropriate.”

That is a **very good enterprise answer**.

---

# 38–40. Are you comfortable debugging procedures? Do you have prior experience?

This is where you should **not overclaim**.

### Answer

> “I am comfortable reading and understanding stored procedures, tracing their SQL logic, checking parameters, joins, conditions and exception handling, and identifying where the issue is occurring.
>
> My recent project work has been more application-layer focused, with JPA/Hibernate handling most database interaction, so stored procedures were not my primary development area.
>
> However, I am comfortable working with procedures when required and understanding how they interact with the application.”

This is much better than saying:

> “Yes, I develop complex procedures every day.”

when you don't.

### Follow-up: “How would you debug a procedure?”

> “First I would reproduce the issue with the same input parameters. Then I would inspect the procedure step by step, verify the input and output values, check joins and conditions, inspect any exception handling, and execute the underlying SQL independently where required.
>
> In Oracle, depending on the case, tools such as `DBMS_OUTPUT`, SQL Developer debugging and query execution plans can help trace the issue.”

---

# The Project-Specific Questions They Can Derive From These

These are the **extra questions I would prepare**, because Nomura can easily take the SQL questions and push them into your Saudi banking project.

## 41. “Where did JPA fit into your onboarding project?”

> “JPA was used as the persistence layer between our Java services and the relational database. We mapped domain entities to tables, used repositories for CRUD operations and used transactional boundaries for operations that needed atomicity.”

---

## 42. “Did you use transactions?”

> “Yes. For operations where multiple database changes represented one business operation, we used transaction boundaries so either all required changes were committed or the operation was rolled back.”

### Follow-up

**“What happens if an external service call happens inside a database transaction and the external service fails?”**

> “I would avoid unnecessarily keeping database transactions open across long external calls. We separate transactional persistence from external communication where possible and use appropriate retry, idempotency and state-management mechanisms.”

This answer will impress a backend interviewer more than simply saying `@Transactional`.

---

# 43. “How can database constraints help your onboarding application?”

> “They protect the data even under concurrent requests.
>
> For example, if an application or verification record must be unique, I can enforce that uniqueness at the database level. Even if two requests arrive simultaneously, the database prevents duplicate records.”

---

# 44. “Would you use a stored procedure or JPA for onboarding business logic?”

### Best answer

> “For core domain and workflow logic, I would prefer Java service-layer logic with JPA because it keeps business rules close to the application code and makes testing and maintenance easier.
>
> Stored procedures can still be useful for database-heavy operations, legacy integrations or specific performance-sensitive operations, depending on the organization's architecture.”

---

# 45. “How do you decide whether to use JPQL, native SQL or JDBC?”

### Answer

> “For normal entity-based operations, I prefer JPA/JPQL.
>
> If I need a database-specific query or a query that is difficult to express efficiently through JPA, I can use a native SQL query.
>
> If I need very low-level control over JDBC operations, I can use JDBC directly.”

### Simple decision tree

**JPA → normal ORM/business CRUD**

**Native SQL → complex/database-specific query**

**JDBC → low-level database control**

---

# 15-Minute Final Revision Order

Don't read all 17 answers repeatedly. For your last revision, memorize these exact blocks:

### First 4 minutes
**JDBC/JPA**
- JDBC fundamentals
- JPA vs JDBC
- Why JPA in your project

### Next 4 minutes
**Core SQL**
- DELETE vs TRUNCATE vs DROP
- Constraints
- Primary key + composite key
- MINUS vs INTERSECT

### Next 4 minutes
**Database programming**
- View
- Function vs procedure
- Cursor
- Trigger

### Final 3–5 minutes
**Your project angle**
- JPA in onboarding
- Transactions
- Idempotency + DB constraints
- Why business logic stays in Java instead of procedures
- How you would read/debug a stored procedure

---

# The 8 “Danger Follow-ups” to Be Ready For

These are the ones I would expect an interviewer to use to go deeper:

**1. Why JPA over JDBC?**

> “Less boilerplate, entity mapping, repository abstraction and transaction integration.”

**2. Why database constraint if application validates?**

> “Database constraint is the final integrity guarantee, especially under concurrency.”

**3. Can composite PK exist?**

> “Yes, multiple columns together can form one primary key.”

**4. Why not use cursors everywhere?**

> “Set-based SQL is generally more efficient than row-by-row processing.”

**5. Why avoid too many triggers?**

> “Hidden side effects and harder debugging/maintenance.”

**6. Would you use stored procedures in your project?**

> “Possible for specific database-heavy operations, but core onboarding business logic is better maintained in the Java service layer.”

**7. What if two requests create the same record?**

> “Idempotency + unique database constraint + atomic transaction/concurrency control.”

**8. How do you debug a procedure you didn't write?**

> “Understand inputs → reproduce → trace SQL/conditions → inspect exceptions/output → validate underlying queries → test the corrected behavior.”

## One-line memory map

**JDBC = low level**  
**JPA = ORM**  
**DELETE = rows**  
**TRUNCATE = all rows**  
**DROP = object**  
**VIEW = virtual table**  
**MINUS = first minus second**  
**INTERSECT = common rows**  
**PK = unique + not null**  
**Composite PK = multiple columns**  
**FUNCTION = returns value**  
**PROCEDURE = performs operation**  
**CURSOR = row-by-row**  
**TRIGGER = automatic DB event**

For **Nomura**, keep bringing the answers back to **data integrity, transactions, concurrency, security and maintainability**—that makes your answers sound like a Java backend engineer rather than someone reciting SQL definitions.