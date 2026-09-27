# Java Interview Deep Dive: Abstract Class & Interface

> **Goal:** इस file को ~30 minutes में पढ़कर Abstract Class और Interface के concepts, differences, rules, common traps और interview follow-ups confidently answer करना.

---

# 1. Abstract Class

## 1.1 Interview Definition

> **An abstract class is a class declared with the `abstract` keyword that is designed to be used as a base class. It can contain both abstract methods, which provide no implementation, and concrete methods, which provide implementation. It cannot be instantiated directly.**

Simple language:

> **Abstract class = partially implemented parent class.**

It defines:

* What subclasses **must implement**
* What behavior can be **shared**
* What common **state** can be maintained

---

## 1.2 Basic Example

```java
abstract class Animal {

    String name;

    Animal(String name) {
        this.name = name;
    }

    abstract void sound();

    void sleep() {
        System.out.println("Animal is sleeping");
    }
}
```

Child class:

```java
class Dog extends Animal {

    Dog(String name) {
        super(name);
    }

    @Override
    void sound() {
        System.out.println("Dog barks");
    }
}
```

Usage:

```java
Dog dog = new Dog("Tommy");

dog.sound();
dog.sleep();
```

But this is NOT allowed:

```java
Animal animal = new Animal("Tommy");
```

because `Animal` is abstract.

---

# 2. Why Do We Need Abstract Classes?

Suppose every `Animal` must have a `sound()` method.

But the sound of every animal is different.

```text
Animal
 ├── Dog     → bark()
 ├── Cat     → meow()
 └── Cow     → moo()
```

So the parent can say:

```java
abstract void sound();
```

It defines the contract:

> Every concrete Animal must provide its own implementation of `sound()`.

At the same time, common behavior can be implemented once:

```java
void sleep() {
    System.out.println("Sleeping...");
}
```

This is one of the main reasons to use an abstract class.

---

# 3. Abstract Method

## Definition

> **An abstract method is a method declaration without a body that must be implemented by a concrete subclass.**

Example:

```java
abstract void sound();
```

Notice:

```java
abstract void sound();
```

NOT:

```java
abstract void sound() {
}
```

An abstract method has **no method body**.

---

# 4. Rules of Abstract Methods

An abstract method:

* Must be declared inside an abstract class or interface
* Has no body
* Must be implemented by the first concrete subclass
* Cannot be `private`
* Cannot be `final`
* Cannot be `static`

Why?

### `private`

A private method cannot be overridden.

But abstract methods require overriding.

### `final`

A final method cannot be overridden.

Again, abstract methods must be implemented/overridden.

### `static`

Static methods belong to the class, not an object, and are not overridden polymorphically.

Therefore:

```java
abstract static void test();
```

is invalid.

---

# 5. Can an Abstract Class Have Concrete Methods?

## YES.

This is one of the most important points.

```java
abstract class Vehicle {

    abstract void start();

    void stop() {
        System.out.println("Vehicle stopped");
    }
}
```

Here:

```text
start() → abstract
stop()  → concrete
```

So an abstract class can contain:

* Abstract methods
* Concrete methods
* Instance variables
* Static variables
* Constructors
* Static methods
* Final methods
* Nested classes/interfaces

---

# 6. Can an Abstract Class Have a Constructor?

## YES.

Very important interview trap.

```java
abstract class Animal {

    Animal() {
        System.out.println("Animal constructor");
    }
}
```

You cannot directly do:

```java
new Animal(); // ❌
```

But the constructor can still execute when a child object is created.

```java
class Dog extends Animal {

    Dog() {
        System.out.println("Dog constructor");
    }
}

Dog d = new Dog();
```

Output:

```text
Animal constructor
Dog constructor
```

Why?

Because creating a child object also initializes its parent portion.

---

# 7. Why Does an Abstract Class Need a Constructor if We Cannot Instantiate It?

This is a classic interview question.

Answer:

> **The abstract class itself cannot be instantiated directly, but its constructor is executed as part of creating a concrete subclass object. It can be used to initialize common state shared by all subclasses.**

Example:

```java
abstract class Employee {

    String company;

    Employee(String company) {
        this.company = company;
    }
}
```

Child:

```java
class Developer extends Employee {

    Developer() {
        super("TCS");
    }
}
```

The abstract class constructor initializes the common `company` state.

---

# 8. Can an Abstract Class Have No Abstract Methods?

## YES.

Example:

```java
abstract class Utility {

    void print() {
        System.out.println("Hello");
    }
}
```

This is completely valid.

Why make it abstract then?

Because you may want to:

* prevent direct instantiation
* force the class to be used only as a base class

---

# 9. Can a Class Be Abstract Without Having an Abstract Method?

## YES.

```java
abstract class Payment {

    void validate() {
        System.out.println("Validation");
    }
}
```

The class is abstract even though all methods are concrete.

---

# 10. Can an Abstract Class Be Final?

## NO.

This is a common trap.

```java
abstract final class A {
}
```

Invalid.

Why?

Because:

```text
abstract → designed to be extended
final    → cannot be extended
```

The two meanings conflict.

---

# 11. Abstract Class and Inheritance

A class can extend only **one class**.

```java
class Dog extends Animal {
}
```

But:

```java
class Dog extends Animal, LivingThing {
}
```

is invalid.

Java does not support multiple class inheritance.

---

# 12. Interface

## 12.1 Interview Definition

> **An interface is a reference type that defines a contract that implementing classes agree to follow. A class can implement one or multiple interfaces.**

Simple understanding:

> **Interface = contract / capability definition.**

Example:

```java
interface Payment {

    void pay();
}
```

Implementation:

```java
class CreditCardPayment implements Payment {

    @Override
    public void pay() {
        System.out.println("Paid using credit card");
    }
}
```

---

# 13. Why Do We Need Interfaces?

Imagine:

```text
Payment
 ├── CreditCard
 ├── UPI
 ├── PayPal
 └── NetBanking
```

All of them provide:

```java
pay()
```

But their implementations are different.

Interface allows us to define the contract:

```java
interface Payment {
    void pay();
}
```

Now different classes can implement it differently.

---

# 14. Interface Fields

Variables declared inside an interface are implicitly:

```text
public static final
```

Example:

```java
interface Config {

    int MAX_RETRY = 3;
}
```

Conceptually:

```java
public static final int MAX_RETRY = 3;
```

Therefore:

```java
Config.MAX_RETRY = 5;
```

is invalid.

Because it is `final`.

---

# 15. Interface Methods

Modern Java interfaces can contain different kinds of methods.

## 15.1 Abstract Method

```java
interface Payment {

    void pay();
}
```

It has no implementation.

---

## 15.2 Default Method

Introduced in Java 8.

```java
interface Payment {

    default void printReceipt() {
        System.out.println("Receipt generated");
    }
}
```

A default method has an implementation.

---

## 15.3 Static Method

```java
interface Payment {

    static void validate() {
        System.out.println("Validation");
    }
}
```

Called using the interface name:

```java
Payment.validate();
```

Important:

> Interface static methods are not inherited by implementing classes.

---

## 15.4 Private Method

Introduced in Java 9.

```java
interface Payment {

    private void log() {
        System.out.println("Logging");
    }
}
```

A private interface method is used internally by other interface methods.

It is not accessible from implementing classes.

---

# 16. Interface Cannot Have a Constructor

Very important.

```java
interface Payment {

    Payment() {   // ❌ invalid
    }
}
```

Why?

Because interfaces cannot be instantiated directly.

```java
new Payment(); // ❌
```

Therefore, they don't have constructors.

---

# 17. Can an Interface Have Instance Variables?

## NO.

Interfaces do not have normal instance variables.

Variables declared in an interface are automatically:

```text
public static final
```

Therefore:

```java
interface Test {
    int x = 10;
}
```

means:

```java
public static final int x = 10;
```

---

# 18. Can an Interface Have `main()`?

## YES.

Because `main()` is static.

```java
interface Test {

    static void main(String[] args) {
        System.out.println("Hello");
    }
}
```

This can technically be executed as a Java entry point.

Important distinction:

> An interface cannot have a normal object constructor, but it can have static methods including `main()`.

---

# 19. Multiple Interface Implementation

This is one of the biggest reasons interfaces are important.

A class can implement multiple interfaces.

```java
interface Camera {
    void click();
}

interface GPS {
    void navigate();
}

class Phone implements Camera, GPS {

    public void click() {
        System.out.println("Photo");
    }

    public void navigate() {
        System.out.println("Navigation");
    }
}
```

This is valid.

Therefore:

```text
Class → extends → one class

Class → implements → multiple interfaces
```

---

# 20. Interface Extending Interface

An interface can extend another interface.

```java
interface A {
    void methodA();
}

interface B extends A {
    void methodB();
}
```

A class implementing `B` must satisfy both contracts.

```java
class C implements B {

    public void methodA() {
    }

    public void methodB() {
    }
}
```

---

# 21. Can an Interface Extend Multiple Interfaces?

## YES.

```java
interface A {
    void a();
}

interface B {
    void b();
}

interface C extends A, B {
}
```

This is valid.

So:

```text
Class       → extends → one class

Interface   → extends → multiple interfaces

Class       → implements → multiple interfaces
```

---

# 22. Interface vs Abstract Class

This is one of the most important interview areas.

| Abstract Class                                                  | Interface                                        |
| --------------------------------------------------------------- | ------------------------------------------------ |
| Declared using `abstract class`                                 | Declared using `interface`                       |
| Can have abstract methods                                       | Can have abstract methods                        |
| Can have concrete methods                                       | Can have concrete/default/static/private methods |
| Can have instance variables                                     | Fields are `public static final`                 |
| Can have constructors                                           | Cannot have constructors                         |
| Can maintain object state                                       | Cannot maintain normal instance state            |
| A class can extend only one                                     | A class can implement multiple                   |
| Can have any access modifier for members, subject to Java rules | Abstract interface methods are public            |
| Represents common base/state/behavior                           | Represents contract/capability                   |
| Can have non-final mutable state                                | Interface fields are constants                   |

---

# 23. Most Important Conceptual Difference

Do NOT reduce the difference to:

> "Abstract class has implementation, interface doesn't."

That is outdated/incomplete.

Modern Java interfaces can have implementations:

```java
default void test() {
}
```

The better distinction is:

### Abstract Class

Focuses on:

```text
Common identity
Common state
Common behavior
Partial implementation
```

### Interface

Focuses on:

```text
Contract
Capability
Multiple implementations
Multiple inheritance of type
```

---

# 24. Example to Understand the Real Difference

Suppose:

```text
Animal
 ├── Dog
 └── Cat
```

Every Animal has:

* name
* age
* sleep()

This is a good candidate for an abstract class.

```java
abstract class Animal {

    String name;
    int age;

    void sleep() {
        System.out.println("Sleeping");
    }

    abstract void sound();
}
```

Now suppose different animals can also be:

```text
Flyable
Swimmable
Runnable
```

These are capabilities.

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}
```

A class can then do:

```java
class Duck extends Animal implements Flyable, Swimmable {

    @Override
    void sound() {
        System.out.println("Quack");
    }

    @Override
    public void fly() {
        System.out.println("Flying");
    }

    @Override
    public void swim() {
        System.out.println("Swimming");
    }
}
```

This gives:

```text
Animal      → common base/state

Flyable     → capability
Swimmable   → capability
```

This is a useful way to think about the design.

---

# 25. Interview Trap: Does an Abstract Class Have to Contain an Abstract Method?

> **No.**

Example:

```java
abstract class A {

    void test() {
    }
}
```

Valid.

---

# 26. Interview Trap: Does Every Method in an Abstract Class Have to Be Abstract?

> **No.**

It can contain both.

```java
abstract class A {

    abstract void test1();

    void test2() {
    }
}
```

---

# 27. Interview Trap: Can an Abstract Class Be Instantiated?

Directly:

```java
new Animal();
```

No.

But an abstract-class reference can point to a child object:

```java
Animal a = new Dog();
```

This is valid.

This distinction is extremely important.

```text
Reference type → Animal
Actual object  → Dog
```

---

# 28. Interview Trap: Can an Interface Reference Point to an Object?

## YES.

```java
Payment payment = new CreditCardPayment();
```

Here:

```text
Reference type → Payment
Actual object  → CreditCardPayment
```

This is polymorphism.

---

# 29. Abstract Class Reference vs Interface Reference

Both can be used as reference types.

```java
Animal a = new Dog();

Payment p = new CreditCardPayment();
```

They allow us to program against an abstraction rather than a concrete implementation.

---

# 30. Important Trap: Interface Method Access Modifier

Suppose:

```java
interface Payment {

    void pay();
}
```

The method is implicitly:

```java
public abstract void pay();
```

Therefore this is required:

```java
class UPI implements Payment {

    public void pay() {
    }
}
```

This is NOT valid:

```java
class UPI implements Payment {

    void pay() {
    }
}
```

Why?

Because you cannot reduce the visibility of an overridden method.

---

# 31. Default Method Conflict

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

Now:

```java
class C implements A, B {
}
```

This creates a conflict.

Java doesn't know whether to use:

```text
A.test()
```

or

```text
B.test()
```

Therefore the class must resolve it:

```java
class C implements A, B {

    @Override
    public void test() {
        System.out.println("C");
    }
}
```

---

# 32. Default Method Conflict: Calling a Specific Parent Interface

Inside the implementing class:

```java
class C implements A, B {

    @Override
    public void test() {
        A.super.test();
    }
}
```

This explicitly chooses the default implementation from `A`.

Important:

```java
A.super.test();
```

works for default interface methods.

---

# 33. What If One Interface Has Default and Another Has Abstract Method?

Example:

```java
interface A {

    default void test() {
        System.out.println("A");
    }
}

interface B {

    void test();
}
```

Then:

```java
class C implements A, B {
}
```

The class must provide:

```java
@Override
public void test() {
}
```

The class cannot simply rely on the default implementation.

---

# 34. Class Method vs Interface Default Method

This is a very important Java rule.

Suppose:

```java
class Parent {

    public void test() {
        System.out.println("Parent");
    }
}
```

Interface:

```java
interface A {

    default void test() {
        System.out.println("Interface");
    }
}
```

Now:

```java
class Child extends Parent implements A {
}
```

Which method runs?

```java
Child c = new Child();
c.test();
```

Output:

```text
Parent
```

Rule:

> **Class inheritance takes precedence over an interface default method.**

A commonly remembered rule is:

```text
Class method > Interface default method
```

---

# 35. What Happens If Two Interfaces Have the Same Abstract Method?

Example:

```java
interface A {
    void test();
}

interface B {
    void test();
}

class C implements A, B {

    public void test() {
        System.out.println("Implemented once");
    }
}
```

Only one implementation is required.

Why?

Because both interfaces require the same contract.

---

# 36. Abstract Class Implementing an Interface

An abstract class can implement an interface without implementing all methods.

Example:

```java
interface Payment {
    void pay();
}
```

Abstract class:

```java
abstract class AbstractPayment implements Payment {
}
```

This is valid.

But when a concrete class extends it:

```java
class CreditCardPayment extends AbstractPayment {

    public void pay() {
        System.out.println("Paid");
    }
}
```

the concrete class must provide the missing implementation.

---

# 37. Can an Abstract Class Implement Multiple Interfaces?

## YES.

```java
abstract class PaymentService
        implements Payment, Refundable {
}
```

Yes.

Because a class can implement multiple interfaces even when it is abstract.

---

# 38. Can an Interface Extend a Class?

## NO.

```java
interface A extends SomeClass { // ❌
}
```

An interface can:

```text
extend interface(s)
```

A class can:

```text
extend class
implement interface(s)
```

---

# 39. Can an Abstract Class Extend Another Abstract Class?

## YES.

```java
abstract class A {
    abstract void testA();
}

abstract class B extends A {
    abstract void testB();
}
```

`B` inherits `testA()` and adds `testB()`.

A further concrete class must implement all remaining abstract methods.

---

# 40. Can an Interface Have a Default Constructor?

No.

Interfaces don't have constructors.

If you see:

```java
interface A {
    A() { }
}
```

it is invalid.

---

# 41. Can an Abstract Class Have `static` Methods?

## YES.

```java
abstract class A {

    static void test() {
        System.out.println("Hello");
    }
}
```

This is valid.

---

# 42. Can an Abstract Class Have `final` Methods?

## YES.

```java
abstract class A {

    final void test() {
    }
}
```

This is valid.

The method cannot be overridden by subclasses.

---

# 43. Can an Abstract Class Have Private Methods?

## YES.

```java
abstract class A {

    private void helper() {
    }
}
```

This is valid.

But a private method cannot be overridden.

---

# 44. Can an Interface Have Private Methods?

## YES — since Java 9.

```java
interface A {

    private void helper() {
    }
}
```

These methods are for internal reuse inside the interface.

---

# 45. Can an Interface Have Static Methods?

## YES.

```java
interface A {

    static void test() {
    }
}
```

Call:

```java
A.test();
```

Not:

```java
SomeImplementation.test();
```

---

# 46. Important: Interface Static Methods Are Not Inherited

Suppose:

```java
interface A {

    static void test() {
        System.out.println("A");
    }
}

class B implements A {
}
```

You cannot normally do:

```java
B.test(); // ❌
```

Instead:

```java
A.test(); // ✅
```

---

# 47. Functional Interface — Only What You Need Here

You will cover Functional Interfaces separately with Java 8.

For this topic remember only:

> **A functional interface is an interface having exactly one abstract method.**

Example:

```java
@FunctionalInterface
interface Calculator {

    int add(int a, int b);
}
```

The detailed Lambda/Stream/SAM discussion belongs to the Java 8 section.

---

# 48. Marker Interface — Basic Awareness

A marker interface has no methods.

Example:

```java
interface Auditable {
}
```

Its purpose is usually to mark a class with some special semantic meaning.

Classic examples from Java include interfaces such as:

```text
Serializable
Cloneable
```

For interview purposes, remember the concept rather than memorizing implementations.

---

# 49. Sealed Types — Basic Awareness

Modern Java also allows restricting which classes/interfaces can extend or implement a type.

Example:

```java
public sealed interface Payment
        permits CardPayment, UPIPayment {
}
```

This is a Java 17+ concept.

The idea:

> Only the permitted classes can implement/extend the sealed type.

This is not usually part of the core Abstract Class vs Interface discussion, but knowing the term is useful for modern Java interviews.

---

# 50. Polymorphism with Abstract Class

```java
abstract class Animal {

    abstract void sound();
}

class Dog extends Animal {

    @Override
    void sound() {
        System.out.println("Bark");
    }
}
```

Now:

```java
Animal a = new Dog();

a.sound();
```

Output:

```text
Bark
```

The reference is:

```text
Animal
```

but actual object is:

```text
Dog
```

The overridden method is selected at runtime.

---

# 51. Polymorphism with Interface

```java
interface Payment {

    void pay();
}

class UPIPayment implements Payment {

    public void pay() {
        System.out.println("UPI");
    }
}
```

Then:

```java
Payment p = new UPIPayment();

p.pay();
```

Again:

```text
Reference type → Payment
Actual object  → UPIPayment
```

This allows loose coupling.

---

# 52. Why Interface Helps Loose Coupling

Suppose service code directly depends on:

```java
CreditCardPayment
```

Changing implementation becomes harder.

Instead:

```java
Payment payment;
```

Now the service depends on the abstraction.

Implementation can be:

```text
CreditCardPayment
UPIPayment
PayPalPayment
```

This is one of the practical reasons interfaces are heavily used in Spring applications.

---

# 53. Abstract Class vs Interface — When to Prefer Which?

Don't treat this as a strict rule. Think in terms of design intent.

## Abstract Class is useful when:

You have a strong parent-child relationship and want to share:

* State
* Constructors
* Common implementation
* Common protected/private helper logic
* Template-like common behavior

Example:

```text
Employee
 ├── Developer
 ├── Tester
 └── Manager
```

---

## Interface is useful when:

You want to define:

* A contract
* A capability
* A common API across unrelated classes
* Multiple contracts for one class

Example:

```text
Flyable
Payable
Serializable
Comparable
```

A class can implement several of these.

---

# 54. Interview Question: Which One Should I Use?

A good answer:

> **If I need shared state, constructors, and common implementation among closely related classes, I would consider an abstract class. If I primarily need to define a contract or capability that can be implemented by multiple unrelated classes, I would consider an interface. The final choice depends on the design and required coupling.**

Avoid saying:

> Interface is always better.

or:

> Abstract class is always better.

There is no universal rule.

---

# 55. Important Code-Based Interview Questions

## Question 1

What happens here?

```java
abstract class A {
    abstract void test();
}

class B extends A {
}
```

Answer:

`B` is also abstract implicitly? **No.**

This code is invalid because `B` is declared as a concrete class but hasn't implemented the abstract method.

To make it valid:

```java
abstract class B extends A {
}
```

or:

```java
class B extends A {

    void test() {
    }
}
```

---

# 56. Question 2

Is this valid?

```java
abstract class A {

    A() {
        System.out.println("A");
    }
}
```

Answer:

**Yes.**

Abstract classes can have constructors.

---

# 57. Question 3

Is this valid?

```java
interface A {

    int x = 10;
}
```

Answer:

**Yes.**

`x` is implicitly:

```java
public static final int x = 10;
```

---

# 58. Question 4

What happens?

```java
interface A {

    void test();
}

class B implements A {

    void test() {
    }
}
```

Answer:

**Compilation error.**

Why?

The interface method is implicitly public, so the implementation must also be public.

Correct:

```java
public void test() {
}
```

---

# 59. Question 5

Is this valid?

```java
abstract final class A {
}
```

Answer:

**No.**

`abstract` requires inheritance.

`final` prevents inheritance.

---

# 60. Question 6

Is this valid?

```java
abstract class A {

    final void test() {
    }
}
```

Answer:

**Yes.**

The class is abstract, but the method can be final.

---

# 61. Question 7

Is this valid?

```java
abstract class A {

    static void test() {
    }
}
```

Answer:

**Yes.**

An abstract class can contain static methods.

---

# 62. Question 8

Is this valid?

```java
interface A {

    static void test() {
    }
}
```

Answer:

**Yes.**

Call it using:

```java
A.test();
```

---

# 63. Question 9

What happens here?

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

class C implements A, B {
}
```

Answer:

Compilation error due to conflicting default methods.

`C` must override:

```java
@Override
public void test() {
}
```

---

# 64. Question 10

What happens here?

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

Then:

```java
new Child().test();
```

Output:

```text
Parent
```

Because:

> Class implementation takes precedence over interface default implementation.

---

# 65. Question 11

Can an abstract class implement an interface without implementing its methods?

Answer:

> **Yes, if the abstract class itself remains abstract.**

Example:

```java
abstract class A implements Payment {
}
```

A later concrete subclass must implement the required methods.

---

# 66. Question 12

Can a class extend an abstract class and implement interfaces simultaneously?

## YES.

```java
class Developer extends Employee
        implements Comparable<Developer>, Serializable {
}
```

This is perfectly valid.

---

# 67. Question 13

Can an interface extend multiple interfaces?

## YES.

```java
interface C extends A, B {
}
```

---

# 68. Question 14

Can a class extend multiple classes?

## NO.

```java
class C extends A, B { // ❌
}
```

Java does not allow multiple class inheritance.

---

# 69. Question 15

Why doesn't Java allow multiple inheritance through classes?

The common conceptual explanation involves ambiguity.

For example:

```text
        A
       / \
      B   C
       \ /
        D
```

If both `B` and `C` define the same method, `D` could face ambiguity about which implementation to inherit.

Java avoids this complexity by allowing:

```text
one parent class
+
multiple interfaces
```

---

# 70. Very Important Interview Trap:

# "Interface has no implementation"

This statement is outdated.

Modern Java interfaces can have implementations through:

```text
default methods
static methods
private methods
```

Therefore a better statement is:

> **Interfaces primarily define contracts, but modern Java allows certain kinds of method implementations inside interfaces.**

---

# 71. Very Important Interview Trap:

# "Abstract class cannot have implementation"

Wrong.

Abstract class can contain fully implemented methods.

```java
abstract class A {

    abstract void test1();

    void test2() {
        System.out.println("Implemented");
    }
}
```

---

# 72. Very Important Interview Trap:

# "Abstract class cannot have constructor"

Wrong.

It can have constructors, and those constructors execute during subclass object creation.

---

# 73. Very Important Interview Trap:

# "Interface variables are normal variables"

Wrong.

They are implicitly:

```java
public static final
```

---

# 74. Very Important Interview Trap:

# "Interface static method can be called through implementation class"

Wrong.

Example:

```java
interface A {

    static void test() {
    }
}

class B implements A {
}
```

Use:

```java
A.test();
```

not:

```java
B.test();
```

---

# 75. Common Rapid-Fire Questions

These are excellent questions to practice verbally.

### Q1. Can abstract class have constructor?

**Yes.**

### Q2. Can abstract class have no abstract method?

**Yes.**

### Q3. Can abstract class have static method?

**Yes.**

### Q4. Can abstract class have final method?

**Yes.**

### Q5. Can abstract method be private?

**No.**

### Q6. Can abstract method be final?

**No.**

### Q7. Can abstract method be static?

**No.**

### Q8. Can abstract class be final?

**No.**

### Q9. Can interface have constructor?

**No.**

### Q10. Can interface have fields?

**Yes, but they are implicitly `public static final`.**

### Q11. Can interface have default methods?

**Yes.**

### Q12. Can interface have static methods?

**Yes.**

### Q13. Can interface have private methods?

**Yes, since Java 9.**

### Q14. Can class implement multiple interfaces?

**Yes.**

### Q15. Can interface extend multiple interfaces?

**Yes.**

### Q16. Can class extend multiple classes?

**No.**

### Q17. Can interface extend a class?

**No.**

### Q18. Can abstract class implement interface?

**Yes.**

### Q19. Can abstract class extend another abstract class?

**Yes.**

### Q20. Can interface have a `main()` method?

**Yes, through a static method.**

---

# 76. 10 Most Important Interview Questions

For an actual interview, be particularly ready for these:

### 1. What is an abstract class and why do we use it?

Expected points:

```text
abstract class
↓
cannot instantiate
↓
can contain abstract + concrete methods
↓
can maintain state
↓
can have constructors
↓
used for shared base behavior
```

---

### 2. What is an interface?

Expected points:

```text
contract
↓
implemented by classes
↓
supports multiple interfaces
↓
abstract/default/static/private methods possible
↓
fields are public static final
```

---

### 3. Abstract class vs Interface?

Talk about:

```text
State
Constructor
Implementation
Multiple inheritance
Use case
Fields
Methods
```

---

### 4. Why can an abstract class have a constructor?

Answer:

> To initialize common state when a concrete subclass object is created.

---

### 5. Can an abstract class have no abstract methods?

Answer:

> Yes. It can be abstract simply to prevent direct instantiation and force subclassing.

---

### 6. Why doesn't Java support multiple class inheritance?

Explain ambiguity/conflicting implementations and Java's design choice of:

```text
one class + multiple interfaces
```

---

### 7. What are default methods in interfaces?

Answer:

> Default methods are interface methods with an implementation, introduced in Java 8, mainly enabling interfaces to evolve without forcing all existing implementations to immediately implement the new method.

---

### 8. What happens if two interfaces have the same default method?

Answer:

> The implementing class must resolve the conflict by overriding the method.

---

### 9. Can interface methods be private/static/default?

Answer:

> Yes, depending on the method type and Java version: default/static since Java 8, private since Java 9.

---

### 10. When would you choose abstract class over interface?

Answer:

> When I need shared state, constructors, and common implementation among closely related classes. For a contract or capability that multiple classes may implement, especially unrelated classes, an interface is generally more appropriate.

---

# 77. One Strong Real-World Example

Consider a banking application.

```text
                 Payment
                /       \
           UPIPayment   CardPayment
```

`Payment` can be an interface:

```java
interface Payment {

    void pay();
}
```

Now common implementation can be placed in an abstract class:

```java
abstract class AbstractPayment implements Payment {

    protected void logTransaction() {
        System.out.println("Transaction logged");
    }
}
```

Then:

```java
class UPIPayment extends AbstractPayment {

    @Override
    public void pay() {
        logTransaction();
        System.out.println("Paid using UPI");
    }
}
```

This demonstrates an important real-world pattern:

```text
Interface
   ↓
Contract

Abstract Class
   ↓
Shared implementation/state

Concrete Class
   ↓
Actual implementation
```

---

# 78. Mental Model

Remember this:

```text
ABSTRACT CLASS
│
├── "What common thing are we?"
├── Shared state
├── Shared implementation
├── Constructors
├── Abstract methods
└── Concrete methods


INTERFACE
│
├── "What can we do?"
├── Contract
├── Multiple interfaces
├── public static final fields
├── abstract methods
├── default methods
├── static methods
└── private helper methods
```

A useful mental shortcut:

```text
Abstract Class → Common Base
Interface      → Contract / Capability
```

---

# 79. Final Revision Table

| Concept                          | Abstract Class | Interface                                                                                           |
| -------------------------------- | -------------- | --------------------------------------------------------------------------------------------------- |
| Instantiation                    | ❌ Directly no  | ❌ No                                                                                                |
| Constructor                      | ✅              | ❌                                                                                                   |
| Instance variables               | ✅              | ❌                                                                                                   |
| Static variables                 | ✅              | ✅ (`public static final`)                                                                           |
| Abstract methods                 | ✅              | ✅                                                                                                   |
| Concrete methods                 | ✅              | ✅ (`default`, `static`, `private`)                                                                  |
| `static` methods                 | ✅              | ✅                                                                                                   |
| `final` methods                  | ✅              | Not applicable to abstract interface methods; interface methods are governed by their specific form |
| Private methods                  | ✅              | ✅ (Java 9+)                                                                                         |
| Multiple inheritance             | ❌ via classes  | ✅ interfaces can extend multiple interfaces                                                         |
| Multiple implementation          | N/A            | ✅ class can implement multiple interfaces                                                           |
| State                            | ✅              | No normal instance state                                                                            |
| Constructor-based initialization | ✅              | ❌                                                                                                   |

---

# 80. 30-Second Interview Answer

If the interviewer suddenly asks:

**"Explain abstract class and interface."**

Say:

> **An abstract class is a partially implemented base class that cannot be instantiated directly. It can contain both abstract and concrete methods, instance state, and constructors. It is useful when closely related classes need to share state and common behavior.**
>
> **An interface primarily defines a contract or capability that classes can implement. A class can implement multiple interfaces. Modern Java interfaces can also contain default, static, and private methods, while interface fields are implicitly public, static, and final.**

---

# 81. Final Memory Sheet

```text
ABSTRACT CLASS
================

abstract class A { }

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

interface A { }

✓ No constructor
✓ No normal instance variables
✓ Fields → public static final
✓ Abstract methods
✓ default methods
✓ static methods
✓ private methods (Java 9+)
✓ Class can implement multiple interfaces
✓ Interface can extend multiple interfaces
✗ Interface cannot extend a class


MOST IMPORTANT RULES
================

Class
→ extends ONE class
→ implements MULTIPLE interfaces

Interface
→ extends MULTIPLE interfaces

Abstract Class
→ common state + common behavior

Interface
→ contract + capability

Class method
→ takes precedence over interface default method

Two conflicting default methods
→ class must override and resolve conflict

Concrete subclass
→ must implement all inherited abstract methods
```

---

# 82. Final Self-Test

Before considering this topic complete, you should be able to answer these **without looking at the sheet**:

1. What is an abstract class?
2. Why can't we instantiate an abstract class?
3. Can abstract class have constructor?
4. Why does an abstract class need a constructor?
5. Can abstract class have zero abstract methods?
6. Can abstract class have static/final/private methods?
7. Why can't an abstract method be final?
8. Why can't an abstract method be private?
9. What is an interface?
10. Can interface have implementation?
11. What are default methods?
12. Why were default methods introduced?
13. Can interface have static methods?
14. Can interface have private methods?
15. Why can't interface have constructors?
16. What are interface fields implicitly?
17. Can a class implement multiple interfaces?
18. Can an interface extend multiple interfaces?
19. Can a class extend multiple classes?
20. Can an abstract class implement an interface?
21. What happens when two interfaces contain the same default method?
22. What happens when a superclass method and interface default method have the same signature?
23. Abstract class vs interface — when would you choose each?
24. How does polymorphism work with an abstract class?
25. How does polymorphism work with an interface?

If you can answer these **without memorizing the exact wording**, you have the conceptual understanding required for most Java interviews on Abstract Class and Interface.
