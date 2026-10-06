Here’s the **quick interview-revision version**, keeping only the points you need. The core idea is: SQL Injection happens when user input becomes part of SQL syntax; the solution is to separate SQL structure from data. Pasted markdown

## SQL Injection — 2-Minute Revision

### 1. What is the problem?

❌ **Bad: String concatenation**

```java
String sql =
    "SELECT * FROM users WHERE username = '" + username + "'";
```

If attacker sends:

```text
' OR '1'='1
```

the input can change the **SQL logic**.

### Root cause

```text
User Input
    ↓
String Concatenation
    ↓
SQL
    ↓
Input becomes SQL syntax
```

---

## 2. JDBC → PreparedStatement

❌ Bad:

```java
String sql =
    "SELECT * FROM users WHERE username = '" + username + "'";

Statement stmt = connection.createStatement();
```

✅ Good:

```java
String sql =
    "SELECT * FROM users WHERE username = ?";

PreparedStatement ps =
    connection.prepareStatement(sql);

ps.setString(1, username);

ResultSet rs = ps.executeQuery();
```

**Interview point:**

> PreparedStatement separates SQL structure from user data, so malicious input is treated as a value, not executable SQL. Pasted markdown

---

## 3. Hibernate → Named Parameters

❌ Bad:

```java
String hql =
    "FROM User u WHERE u.username = '" + username + "'";
```

✅ Good:

```java
String hql =
    "FROM User u WHERE u.username = :username";

Query<User> query =
    session.createQuery(hql, User.class);

query.setParameter("username", username);
```

**Remember:**

```text
Hibernate → :username + setParameter()
```

Hibernate doesn't automatically make string-concatenated queries safe. Pasted markdown

---

## 4. JPA → Parameter Binding

❌ Bad:

```java
String jpql =
    "SELECT u FROM User u WHERE u.username = '" + username + "'";
```

✅ Good:

```java
String jpql =
    "SELECT u FROM User u WHERE u.username = :username";

List<User> users =
    entityManager
        .createQuery(jpql, User.class)
        .setParameter("username", username)
        .getResultList();
```

**Remember:**

```text
JPA → :username + setParameter()
```

Pasted markdown

---

## 5. Spring Data JPA

Prefer:

```java
Optional<User> findByUsername(String username);
```

Or:

```java
@Query("""
    SELECT u FROM User u
    WHERE u.username = :username
""")
Optional<User> findUser(
    @Param("username") String username
);
```

Spring Data handles parameter binding. Pasted markdown

---

# 🔥 Remember This Table

| Technology | Prevention |
|---|---|
| **JDBC** | `PreparedStatement` + `?` |
| **Hibernate** | Named parameter `:username` + `setParameter()` |
| **JPA** | `:username` + `setParameter()` |
| **Spring Data JPA** | Repository methods / `@Query` + parameters |

### One-line answer for Nomura

> **“SQL Injection occurs when untrusted input is concatenated with SQL and becomes part of the SQL syntax. I prevent it using parameterized queries: PreparedStatement in JDBC, named parameters in Hibernate/JPA, and repository methods or parameterized @Query in Spring Data JPA.”**

### One extra point they may ask

For dynamic things like:

```java
"ORDER BY " + sortBy
```

you **cannot simply parameterize a column name**, so use a **whitelist** of allowed column names instead. Pasted markdown

**Mental shortcut:**

```text
SQL Injection
     ↓
String Concatenation ❌
     ↓
Parameter Binding ✅

JDBC      → PreparedStatement
Hibernate → setParameter()
JPA       → setParameter()
Spring JPA→ Repository / @Query
```