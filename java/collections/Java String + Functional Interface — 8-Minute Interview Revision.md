# Java String + Interfaces — 8-Minute Interview Revision

## 21. Find duplicate characters in a String

Suppose:

```java
String name = "Rishabh";
```

### Interview Answer

> "I can use a HashMap to count the frequency of each character. Then I iterate through the map and print the characters whose frequency is greater than one."

### Code

```java
String name = "Rishabh";

Map<Character, Integer> freq = new HashMap<>();

for (char ch : name.toCharArray()) {
    freq.put(ch, freq.getOrDefault(ch, 0) + 1);
}

for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
    if (entry.getValue() > 1) {
        System.out.println(entry.getKey());
    }
}
```

### For `"Rishabh"`

```text
h
```

because `h` appears twice.

### Interview point

If they ask **"Why HashMap?"**

> "Because I need frequency counting, so a key-value structure where the character is the key and its count is the value is a natural choice."

---

# 22. String s and String s1 have same value

```java
String s = "Rishabh";
String s1 = "Rishabh";

System.out.println(s == s1);
System.out.println(s.equals(s1));
```

### Result

```text
true
true
```

### Why?

Both string literals use the **String Constant Pool**.

Conceptually:

```text
String Pool

"Rishabh"
    ↑   ↑
    s  s1
```

So both references point to the same pooled String object.

### Interview Answer

> "`==` compares references, while `equals()` compares String content. Since both are the same string literal and use the String pool, both references point to the same object, so both return true."

---

# 23. String s2 = new String("Rishabh")

```java
String s = "Rishabh";
String s1 = "Rishabh";
String s2 = new String("Rishabh");

System.out.println(s == s2);
System.out.println(s.equals(s2));
```

### Result

```text
false
true
```

### Why?

```text
String Pool
"Rishabh"
    ↑
    s
    ↑
    s1

Heap
"Rishabh"
    ↑
    s2
```

`new String()` creates a **new String object**, while `equals()` checks content.

### Remember

```text
==       → reference
equals() → content
```

---

# 24. If s1 = s, can we change s1?

Suppose:

```java
String s = "Rishabh";
String s1 = s;

s1 = "Yogesh";
```

### Important Correction

This does **NOT** throw an error.

There is **no compile-time error and no runtime error**.

What happens is:

```text
Before:

"Rishabh"
   ↑   ↑
   s  s1


After s1 = "Yogesh":

"Rishabh"     "Yogesh"
   ↑             ↑
   s            s1
```

We are **changing the reference stored in `s1`**, not modifying the original String object.

### Interview Answer

> "String is immutable, but assigning a new String to a reference variable is completely valid. Immutability means the existing String object cannot be modified; it does not mean the reference variable cannot point to another String."

This is a very important distinction.

---

# 25. If I change s = "Yogesh", compile-time or runtime error?

Suppose:

```java
String s = "Rishabh";

s = "Yogesh";
```

### Result

✅ Valid Java  
✅ No compile-time error  
✅ No runtime error

`String` is immutable, but the variable `s` can be reassigned.

```text
s
↓
"Rishabh"

s = "Yogesh"

s
↓
"Yogesh"
```

The old String object remains unchanged.

### Interview Answer

> "Neither. It is completely valid. String immutability prevents modification of the existing String object; it does not prevent reassignment of the reference."

---

# 26. Why is String immutable?

### Best Interview Answer

> "String is immutable mainly for security, String pool optimization, thread safety, and stable hashing."

### 1. Security

Strings are commonly used for:

- usernames
- file paths
- URLs
- class names
- database connection information

If Strings were mutable, their value could change unexpectedly after validation.

---

### 2. String Pool

Because Strings are immutable, Java can safely share the same literal object.

```java
String a = "Rishabh";
String b = "Rishabh";
```

Both can safely reference the same object.

---

### 3. HashMap / HashSet

Strings are frequently used as keys.

If a key's value changed after insertion, HashMap lookup could break.

```java
Map<String, Integer> map = new HashMap<>();
map.put("customerId", 100);
```

Immutability keeps the key stable.

---

### 4. Thread Safety

Immutable objects are naturally safer to share between threads because their state cannot change.

### One-line memory

**String = Security + Pool + Hashing + Thread Safety**

---

# 27. Why can't other classes be immutable? Is String the only immutable class?

### IMPORTANT: Interviewer's statement is incorrect.

**String is NOT the only immutable class.**

Examples:

```text
String
Integer
Long
Double
Boolean
BigInteger
BigDecimal
```

These are immutable types/classes.

### Interview Answer

> "String is not the only immutable class. Java also provides immutable wrapper classes such as Integer, Long, Double and Boolean, as well as BigInteger and BigDecimal. Any class can be designed as immutable; String simply has especially important use cases such as pooling, security and hashing."

### Very important distinction

```text
Immutable class ≠ only String
```

---

# 28. How do you create an immutable class?

### 5 Rules to Remember

1. Make class `final`
2. Make fields `private final`
3. Initialize fields through constructor
4. Don't provide setters
5. For mutable object fields, return defensive copies

### Example

```java
public final class Employee {

    private final String name;
    private final int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

No setter.

```java
employee.setName(...); // impossible
```

### If field itself is mutable

For example:

```java
private final List<String> skills;
```

Don't directly expose it.

Use:

```java
public List<String> getSkills() {
    return List.copyOf(skills);
}
```

### Interview Answer

> "To create an immutable class, I make the class final, keep fields private and final, initialize them through the constructor, provide no setters, and use defensive copies or immutable collections for mutable fields."

---

# 29. What is a Functional Interface?

### Interview Answer

> "A functional interface is an interface that has exactly one abstract method. It can have multiple default or static methods. It is primarily used with lambda expressions and method references."

### Example

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}
```

Usage:

```java
Calculator c = (a, b) -> a + b;
```

### Common examples

```text
Runnable
Comparator
Consumer
Supplier
Function
Predicate
```

### Key point

```text
1 abstract method
+
default/static methods allowed
```

---

# 30. Class implements two interfaces having same method

Example:

```java
interface A {
    void method1();
}

interface B {
    void method1();
}

class MyClass implements A, B {

    @Override
    public void method1() {
        System.out.println("Implementation");
    }
}
```

### What happens?

No problem.

The same implementation satisfies both interfaces.

### Interview Answer

> "If both interfaces declare the same abstract method with a compatible signature, the implementing class only needs to provide one implementation. That single implementation satisfies both interfaces."

### Think:

```text
Interface A ── method1()
                  \
                   → MyClass.method1()
                  /
Interface B ── method1()
```

---

# 31. What if both interfaces have the same DEFAULT method?

Now:

```java
interface A {
    default void show() {
        System.out.println("A");
    }
}

interface B {
    default void show() {
        System.out.println("B");
    }
}

class C implements A, B {
}
```

### Result

❌ Compile-time error.

Java cannot decide whether to use:

```text
A.show()
```

or

```text
B.show()
```

This is the **default method conflict / diamond problem**.

### Solution

Override it in the class:

```java
class C implements A, B {

    @Override
    public void show() {
        System.out.println("C");
    }
}
```

Or explicitly select one:

```java
class C implements A, B {

    @Override
    public void show() {
        A.super.show();
    }
}
```

### Interview Answer

> "If two interfaces provide the same default method, Java creates a conflict. The implementing class must override the method and resolve the ambiguity, because Java does not allow the class to inherit two conflicting default implementations."

---

# 32. How does Functional Interface help?

### Interview Answer

> "A functional interface allows us to represent behavior as a value using lambda expressions. This reduces boilerplate and is heavily used by the Stream API and functional-style programming."

Example:

```java
List<String> names = List.of("A", "B", "C");

names.forEach(name -> System.out.println(name));
```

Here `forEach()` accepts a functional interface:

```text
Consumer<T>
```

Other examples:

```text
Predicate<T> → true/false condition
Function<T,R> → converts one value to another
Consumer<T> → consumes value
Supplier<T> → supplies value
```

### In your interview, connect it to Java backend

> "In backend development, I use functional interfaces indirectly through Streams, for example filtering onboarding records, mapping DTOs, sorting data, and processing collections using lambda expressions."

---

# FINAL 8-MINUTE RAPID REVISION

## String

```text
==          → reference comparison
equals()    → content comparison

"Rishabh" + "Rishabh"
→ == true
→ equals true

new String("Rishabh")
→ == false
→ equals true
```

## String Immutability

```text
s = "Rishabh";
s = "Yogesh";
```

✅ Valid  
❌ Not an error

**Reassignment ≠ modification**

---

## Why String immutable?

```text
Security
String Pool
Hashing
Thread Safety
```

---

## Immutable Class

```text
final class
private final fields
constructor
no setters
defensive copy for mutable fields
```

---

## Functional Interface

```text
Exactly ONE abstract method
Default/static methods allowed
Used with Lambda
```

---

## Two Interfaces

### Same abstract method

```text
A.method()
B.method()

→ one implementation in class is enough
```

### Same default method

```text
A.defaultMethod()
B.defaultMethod()

→ conflict
→ class MUST override
```

---

# MOST IMPORTANT INTERVIEW TRAPS

### Trap 1
**"String immutable means `s = "newValue"` is an error."**

❌ Wrong

### Trap 2
**"String is the only immutable class."**

❌ Wrong

### Trap 3
**"`==` compares String content."**

❌ Wrong

### Trap 4
**"Two interfaces with the same abstract method create an ambiguity."**

❌ Usually wrong — one implementation can satisfy both if signatures are compatible.

### Trap 5
**"Two interfaces with the same default method are automatically resolved."**

❌ Wrong — class must resolve the conflict.

# ONE MASTER LINE TO REMEMBER

**"String cannot be modified, but its reference can be reassigned; `==` checks reference, `equals()` checks content; functional interfaces provide one abstract behavior, and default-method conflicts must be resolved by the implementing class."**