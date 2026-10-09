# Diamond Problem in Java — Interview Revision (5 Minutes)

## 1. What Is the Diamond Problem?

The **Diamond Problem** occurs when a class inherits the same method from two different parent classes, creating ambiguity about which implementation should execute.

Imagine a diamond-shaped inheritance structure:

```text
        A
       / \
      B   C
       \ /
        D
```

Both `B` and `C` inherit from `A`. If `B` and `C` override the same method, class `D` may face ambiguity about which implementation to inherit.

## 2. Why Doesn't Java Support Multiple Inheritance of Classes?

Java does not allow a class to extend multiple classes because it can create ambiguity.

### Example

```java
class A {
    void show() {
        System.out.println("A");
    }
}

class B extends A {
    @Override
    void show() {
        System.out.println("B");
    }
}

class C extends A {
    @Override
    void show() {
        System.out.println("C");
    }
}

// Compilation error
class D extends B, C {
}
```

**Problem:** If we create an object of `D` and call `show()`, should Java execute `B.show()` or `C.show()`?

Java avoids this ambiguity by not supporting multiple inheritance of classes.

**Interview point:** Java supports single inheritance of classes but multiple inheritance through interfaces.

---

## 3. Diamond Problem with Interfaces (Java 8+)

Since Java 8, interfaces can have `default` methods containing implementations. Therefore, a similar ambiguity can occur when a class implements two interfaces with the same default method.

### Example

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

class Test implements A, B {
    @Override
    public void show() {
        System.out.println("Test");
    }
}

public class Main {
    public static void main(String[] args) {
        Test obj = new Test();
        obj.show();
    }
}
```

**Output:**
```text
Test
```

**Explanation:**

- Interface `A` provides a default implementation of `show()`.
- Interface `B` provides another implementation of `show()`.
- Class `Test` implements both interfaces.
- Java requires `Test` to override `show()` and resolve the conflict explicitly.

Without the override, the compiler reports an error because it cannot choose between the two unrelated default methods.

---

## 4. How Can We Call Both Interface Implementations?

Java provides special syntax to call a specific interface's default method using `InterfaceName.super.methodName()`.

```java
class Test implements A, B {
    @Override
    public void show() {
        A.super.show();
        B.super.show();
    }
}
```

**Output:**
```text
A
B
```

Here, `A.super.show()` calls the default implementation from interface `A`, while `B.super.show()` calls the one from interface `B`.

Note: This syntax is for explicitly invoking an eligible direct superinterface's default method.

---

## 5. Important Rule: What If One Interface Extends Another?

Java follows a clear rule: **the more specific interface's default method takes precedence over the less specific one.**

```java
interface A {
    default void show() {
        System.out.println("A");
    }
}

interface B extends A {
    @Override
    default void show() {
        System.out.println("B");
    }
}

class Test implements A, B {
}
```

**Output:**
```text
B
```

**Why?** `B` extends `A` and overrides its method. Therefore, `B` provides the more specific implementation, and no override is required in `Test`.

### Method-resolution rules to remember

1. **Class wins:** An implementation inherited from a class takes precedence over an interface's default method.
2. **More specific interface wins:** If one interface extends another and overrides its default method, the more specific method wins.
3. **Unrelated conflicting interfaces:** The implementing class must override the method to resolve the ambiguity.

---

## 6. Interview Answer — 30–45 Seconds

"The Diamond Problem occurs when a class inherits the same method through multiple parents, creating ambiguity about which implementation should execute.

Java avoids this problem with classes because it doesn't support multiple inheritance of classes. However, since Java 8, interfaces support default methods, so a similar conflict can occur when a class implements two interfaces with the same default method.

In that case, Java requires the implementing class to override the conflicting method. If needed, we can explicitly invoke an interface's implementation using `InterfaceName.super.methodName()`. Java also gives precedence to class implementations and to more specific interface defaults."

---

## 7. Quick Revision

| Question | Answer |
|---|---|
| What is the Diamond Problem? | Ambiguity caused by inheriting the same method from multiple parents. |
| Does Java support multiple inheritance of classes? | No. |
| Can interfaces cause a similar problem? | Yes, through conflicting default methods. |
| How do we resolve the conflict? | Override the method in the implementing class. |
| How do we call a particular interface's default method? | `A.super.show()` |
| Which wins: a class method or an interface default? | The class implementation, subject to Java's method-resolution rules. |
| What if one interface extends another? | The more specific interface's default implementation takes precedence. |

**Remember:** Java does not eliminate all multiple-inheritance ambiguity by banning multiple interfaces. Instead, it defines rules to resolve default-method conflicts explicitly and predictably.