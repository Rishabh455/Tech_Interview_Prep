# Java Interview Brush-Up: Classes, Objects & Constructors

## 1. Class

### Interview Definition

> **A class is a blueprint or template that defines the state and behavior of objects. State is represented by fields/variables, and behavior is represented by methods.**

### Example

```java
class Employee {
    String name;
    int salary;

    void work() {
        System.out.println("Employee is working");
    }
}
```

Here:

* `name`, `salary` → state
* `work()` → behavior

### Remember

* Class is a **blueprint**
* Class itself is **not an object**
* Objects are created from a class
* A class can contain **fields, methods, constructors, blocks, nested classes/interfaces**

---

# 2. Object

### Interview Definition

> **An object is a runtime instance of a class. It has its own state and can access the behavior defined by the class.**

### Example

```java
Employee emp = new Employee();
emp.name = "Rishabh";
emp.salary = 80000;
emp.work();
```

Here:

```text
Employee      → class
emp           → reference variable
new Employee  → object creation
```

### Important

```java
Employee emp1 = new Employee();
Employee emp2 = new Employee();
```

`emp1` and `emp2` refer to **two different objects**, and each object has its own instance variables.

---

# 3. Constructor

### Interview Definition

> **A constructor is a special member of a class used to initialize an object. It has the same name as the class and does not have a return type. It is automatically invoked when an object is created.**

### Example

```java
class Employee {

    String name;
    int salary;

    Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }
}

Employee emp = new Employee("Rishabh", 80000);
```

Constructor runs during:

```java
new Employee("Rishabh", 80000);
```

---

# 4. Constructor vs Method

| Constructor                   | Method                    |
| ----------------------------- | ------------------------- |
| Initializes object            | Performs an operation     |
| Same name as class            | Can have any valid name   |
| No return type                | Has return type or `void` |
| Called during object creation | Called explicitly         |
| Cannot be inherited           | Methods can be inherited  |
| Cannot be overridden          | Methods can be overridden |

---

# 5. Types of Constructors

## No-Argument Constructor

```java
Employee() {
    System.out.println("Constructor called");
}
```

## Parameterized Constructor

```java
Employee(String name, int salary) {
    this.name = name;
    this.salary = salary;
}
```

## Default Constructor

If you don't write **any constructor**, Java compiler provides a default constructor.

```java
class Employee {
}
```

Conceptually:

```java
Employee() {
    super();
}
```

Important:

> **The compiler-generated default constructor is provided only when no constructor is explicitly written.**

---

# 6. Constructor Overloading

Multiple constructors with different parameter lists are allowed.

```java
class Employee {

    Employee() {
    }

    Employee(String name) {
    }

    Employee(String name, int salary) {
    }
}
```

This is **constructor overloading**.

---

# 7. Constructor Chaining

A constructor can call another constructor.

### `this()`

Calls another constructor of the same class.

```java
class Employee {

    Employee() {
        this("Unknown");
    }

    Employee(String name) {
        System.out.println(name);
    }
}
```

### `super()`

Calls the parent class constructor.

```java
class Employee extends Person {

    Employee() {
        super();
    }
}
```

### Important Rule

`this()` or `super()` must be the **first statement** inside a constructor.

---

# 8. Important Interview Questions

## Q1. What is a class?

> A class is a blueprint that defines the state and behavior of objects.

---

## Q2. What is an object?

> An object is a runtime instance of a class with its own state.

---

## Q3. Difference between class and object?

> A class is a blueprint, while an object is an actual runtime instance created from that blueprint.

---

## Q4. Is a class an object?

> No. A class is a type/blueprint. An object is an instance of that class.

---

## Q5. What is a constructor?

> A constructor is used to initialize an object. It has the same name as the class and no return type.

---

## Q6. Can a constructor have a return type?

> No. Not even `void`. If you add a return type, it becomes a method.

---

## Q7. Can constructors be overloaded?

> Yes. Constructors can be overloaded by changing the parameter list.

---

## Q8. Can constructors be overridden?

> No. Constructors are not inherited, so they cannot be overridden.

---

## Q9. Can a constructor be private?

> Yes. A private constructor can be used to control object creation, for example in Singleton-style designs or utility classes.

```java
class Utility {
    private Utility() {
    }
}
```

---

## Q10. Can a constructor be static?

> No. A constructor belongs to object creation, while `static` belongs to the class.

---

## Q11. Can a constructor be final or abstract?

> No. Constructors cannot be `final`, `static`, or `abstract`.

---

## Q12. What happens if we don't create a constructor?

> If no constructor is explicitly defined, the compiler provides a default no-argument constructor.

---

## Q13. When is a constructor called?

> A constructor is called automatically when an object is created using `new`.

```java
Employee e = new Employee();
```

---

## Q14. What is `this()` in a constructor?

> `this()` is used to call another constructor of the same class.

---

## Q15. What is `super()` in a constructor?

> `super()` is used to call the constructor of the parent class.

---

## Q16. Can `this()` and `super()` be used together in the same constructor?

> Not as direct constructor calls, because both must be the first statement.

---

## Q17. What is the difference between `Employee e` and `new Employee()`?

```java
Employee e = new Employee();
```

* `Employee e` → reference variable
* `new Employee()` → creates the object
* `e` stores the reference to that object

---

# 9. One-Minute Example

```java
class Employee {

    String name;
    int salary;

    Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    void display() {
        System.out.println(name + " " + salary);
    }
}

public class Main {
    public static void main(String[] args) {

        Employee e = new Employee("Rishabh", 80000);

        e.display();
    }
}
```

Understand this flow:

```text
Class
  ↓
Employee

Constructor
  ↓
Employee("Rishabh", 80000)

Object creation
  ↓
new Employee(...)

Reference
  ↓
e

Method call
  ↓
e.display()
```

# 10. 30-Second Interview Answer

> **Class is a blueprint that defines the state and behavior of objects. An object is a runtime instance of a class. A constructor is a special member used to initialize an object; it has the same name as the class and no return type. Constructors can be overloaded, and constructor chaining can be done using `this()` and `super()`.**

# Final Things to Remember

```text
Class       → Blueprint
Object      → Instance
Constructor → Initialization

new         → Creates object
this        → Current object / same-class constructor
super       → Parent class / parent constructor

Constructor:
✓ Same name as class
✓ No return type
✓ Can be overloaded
✓ Can be private
✗ Cannot be overridden
✗ Cannot be static
✗ Cannot be final
✗ Cannot be abstract
```
