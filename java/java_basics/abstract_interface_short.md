# Java Interview Quick Revision — Abstract Class & Interface

> **Target: 10–15 min revision**

---

# 1. Abstract Class

### Interview Definition

> **An abstract class is a class declared with the `abstract` keyword that cannot be instantiated directly. It can contain both abstract and concrete methods and is mainly used for sharing common state and behavior among related classes.**

### Easy Memory

```text
Abstract Class = Common Base + Partial Implementation
```

Example:

```java
abstract class Animal {

    String name;

    Animal(String name) {
        this.name = name;
    }

    abstract void sound();

    void sleep() {
        System.out.println("Sleeping");
    }
}
```

---

## 2. Abstract Method

> **An abstract method is a method declaration without a body that must be implemented by the first concrete subclass.**

```java
abstract void sound();
```

### Important Rules

```text
abstract method
✓ no body
✓ must be implemented by concrete subclass

✗ cannot be private
✗ cannot be final
✗ cannot be static
```

**Why?**

Because an abstract method must be overridden, while `private`, `final`, and `static` methods cannot participate in normal overriding.

---

# 3. Most Important Abstract Class Traps

### Can abstract class have concrete methods?

**YES**

```java
abstract class A {

    abstract void test();

    void print() {
        System.out.println("Hello");
    }
}
```

---

### Can abstract class have constructor?

**YES**

```java
abstract class A {

    A() {
        System.out.println("A constructor");
    }
}
```

You cannot do:

```java
new A(); // ❌
```

But the constructor runs when a child object is created.

```java
class B extends A {
}

B obj = new B();
```

```text
A constructor
↓
B constructor
```

### Interview Answer

> **An abstract class can have a constructor because its constructor is used to initialize the parent part of a concrete subclass object.**

---

### Can abstract class have zero abstract methods?

**YES**

```java
abstract class A {

    void test() {
    }
}
```

It may simply be abstract to prevent direct instantiation.

---

### Can abstract class be final?

**NO**

```text
abstract → should be extended
final    → cannot be extended
```

---

### Can abstract class have static/final/private methods?

**YES**

```text
static  → Yes
final   → Yes
private → Yes
```

---

# 4. Interface

### Interview Definition

> **An interface defines a contract that implementing classes agree to follow. A class can implement multiple interfaces.**

### Easy Memory

```text
Interface = Contract / Capability
```

Example:

```java
interface Payment {
    void pay();
}

class UPI implements Payment {

    public void pay() {
        System.out.println("UPI Payment");
    }
}
```

---

# 5. Interface Fields

Any variable declared inside an interface is automatically:

```java
public static final
```

Example:

```java
interface Config {
    int MAX_RETRY = 3;
}
```

Actually:

```java
public static final int MAX_RETRY = 3;
```

Therefore:

```java
Config.MAX_RETRY = 5; // ❌
```

---

# 6. Interface Methods — Only What You Need

Modern interfaces can contain:

```text
abstract methods
default methods
static methods
private methods
```

### Default Method

Java 8:

```java
default void print() {
}
```

### Static Method

```java
static void validate() {
}
```

Call:

```java
Payment.validate();
```

**Not through implementation class.**

### Private Method

Java 9+:

```java
private void log() {
}
```

Used internally inside the interface.

---

# 7. Interface Constructor?

**NO**

```java
interface A {

    A() { } // ❌
}
```

Interface cannot be instantiated, so it doesn't have constructors.

---

# 8. Multiple Inheritance

### Class

```text
class
  ↓
extends ONE class
implements MULTIPLE interfaces
```

Example:

```java
class Phone extends Device implements Camera, GPS {
}
```

### Interface

```text
interface
  ↓
can extend MULTIPLE interfaces
```

```java
interface C extends A, B {
}
```

### Important

```java
class C extends A, B { } // ❌
```

Java does not support multiple class inheritance.

---

# 9. Abstract Class vs Interface

| Abstract Class                        | Interface                                  |
| ------------------------------------- | ------------------------------------------ |
| Common base                           | Contract / capability                      |
| Can have instance state               | No normal instance state                   |
| Can have constructors                 | No constructor                             |
| Abstract + concrete methods           | Abstract + default/static/private methods  |
| Can have static/final/private methods | Interface fields are `public static final` |
| `extends` one class                   | Class can implement multiple               |
| Useful for closely related classes    | Useful for common contract/capability      |

### One-Line Difference

```text
Abstract Class → What common thing are we?
Interface      → What can we do?
```

---

# 10. Most Important Interview Traps

## Trap 1

**"Abstract class cannot have implementation."**

❌ Wrong.

It can have concrete methods.

---

## Trap 2

**"Interface cannot have implementation."**

❌ Incomplete.

Modern interfaces can have:

```text
default
static
private
```

methods with implementation.

---

## Trap 3

**"Abstract class cannot have constructor."**

❌ Wrong.

It can have constructors.

---

## Trap 4

**"Interface variables are normal variables."**

❌ Wrong.

They are:

```text
public static final
```

---

## Trap 5

**"Interface static method is inherited by implementing class."**

❌ No.

```java
InterfaceName.method();
```

---

## Trap 6

**"Abstract class cannot have static/final methods."**

❌ Wrong.

It can.

---

# 11. Default Method Conflict

Suppose:

```java
interface A {
    default void test() {
        System.out.println("A");
    }
}

interface B {
    default void test() {
        System.out.println("B");
    }
}
```

And:

```java
class C implements A, B {
}
```

Compilation error.

Why?

Because Java doesn't know which default method to use.

So `C` must override:

```java
class C implements A, B {

    @Override
    public void test() {
        System.out.println("C");
    }
}
```

You can also explicitly call:

```java
A.super.test();
```

---

# 12. Class Method vs Interface Default Method

If a parent class has a method and interface has a default method with the same signature:

```text
Class method
     ↓
takes precedence
     ↓
Interface default method
```

Example:

```java
class Parent {
    public void test() {
        System.out.println("Parent");
    }
}

interface A {
    default void test() {
        System.out.println("Interface");
    }
}

class Child extends Parent implements A {
}
```

```java
new Child().test();
```

Output:

```text
Parent
```

---

# 13. Polymorphism

Both abstract classes and interfaces can be used as reference types.

### Abstract Class

```java
Animal a = new Dog();
```

### Interface

```java
Payment p = new UPI();
```

Remember:

```text
Reference Type → abstraction
Actual Object  → concrete implementation
```

---

# 14. Abstract Class Implementing Interface

Yes.

```java
interface Payment {
    void pay();
}

abstract class AbstractPayment implements Payment {
}
```

This is valid because the class itself is abstract.

The concrete child must implement the missing methods.

---

# 15. Functional Interface — Only Remember This

You already cover this in Java 8.

> **A functional interface has exactly one abstract method.**

Example:

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}
```

That's enough for this topic.

---

# 16. When to Use What?

### Abstract Class

Use when you need:

```text
shared state
+
shared implementation
+
constructor
+
closely related classes
```

Example:

```text
Employee
 ├── Developer
 ├── Tester
 └── Manager
```

### Interface

Use when you need:

```text
contract
+
capability
+
multiple implementations
+
multiple interfaces
```

Example:

```text
Flyable
Payable
Comparable
Serializable
```

---

# 17. Top Interview Questions

### Q1. What is an abstract class?

> A class that cannot be instantiated directly and can contain both abstract and concrete methods.

### Q2. What is an interface?

> An interface defines a contract that classes implement.

### Q3. Can abstract class have constructor?

> Yes.

### Q4. Why?

> To initialize the parent part of a subclass object.

### Q5. Can abstract class have no abstract method?

> Yes.

### Q6. Can abstract class be final?

> No, because abstract requires inheritance while final prevents inheritance.

### Q7. Can abstract method be private?

> No.

### Q8. Can abstract method be final?

> No.

### Q9. Can abstract class have static/final/private methods?

> Yes.

### Q10. Can interface have constructor?

> No.

### Q11. Can interface have variables?

> Yes, but they are implicitly `public static final`.

### Q12. Can interface have default/static/private methods?

> Yes.

### Q13. Can class implement multiple interfaces?

> Yes.

### Q14. Can interface extend multiple interfaces?

> Yes.

### Q15. Can class extend multiple classes?

> No.

### Q16. What happens if two interfaces have same default method?

> Implementing class must override and resolve the conflict.

### Q17. Which takes precedence: class method or interface default method?

> Class method.

### Q18. Can abstract class implement interface?

> Yes.

---

# 18. 30-Second Interview Answer

### "Explain Abstract Class and Interface"

> **An abstract class is a partially implemented base class that cannot be instantiated directly. It can contain abstract methods, concrete methods, instance state, and constructors, so it is useful when closely related classes need to share common state and behavior.**
>
> **An interface primarily defines a contract or capability that classes implement. A class can implement multiple interfaces. Modern Java interfaces can also have default, static, and private methods, while interface fields are implicitly public, static, and final.**

---

# 19. Final Memory Sheet

```text
ABSTRACT CLASS
================

✓ Cannot instantiate directly
✓ Can have constructor
✓ Can have abstract methods
✓ Can have concrete methods
✓ Can have instance variables
✓ Can have static methods
✓ Can have final methods
✓ Can have private methods

✗ Cannot be final
✗ Abstract method cannot be private
✗ Abstract method cannot be final
✗ Abstract method cannot be static


INTERFACE
================

✓ Contract / Capability
✓ No constructor
✓ Fields → public static final
✓ Abstract methods
✓ default methods
✓ static methods
✓ private methods (Java 9+)
✓ Class can implement multiple
✓ Interface can extend multiple


MOST IMPORTANT
================

Class
→ extends ONE class
→ implements MULTIPLE interfaces

Abstract Class
→ Common State + Common Behavior

Interface
→ Contract + Capability

Class Method
→ wins over Interface Default Method

Two Default Methods
→ Child must resolve conflict
```

# 20. 1-Minute Self Test

Without looking, answer:

1. Why can't abstract class be instantiated?
2. Can abstract class have constructor?
3. Can abstract class have zero abstract methods?
4. Can abstract method be final/private/static?
5. Can interface have constructor?
6. What are interface variables?
7. Can interface have implementation?
8. Why can a class implement multiple interfaces?
9. What happens with two conflicting default methods?
10. Abstract class vs interface — when would you choose each?
11. What is the difference between `extends` and `implements`?
12. Why does class method win over interface default method?

If you can answer these **in your own words**, this topic is sufficiently revised for a typical Java interview.
