# Java Collections, Equality & Immutability
## Interview Notes — Java Developer (3.6 Years Experience)

**How to use these notes:** Remember the main explanation and the **bold keywords**. In an interview, start with the definition, explain the internal working, and finish with a practical use case or trade-off.

---

## 1. Java Collections Framework

### COLL-001 — Explain the Java Collections Framework

The **Java Collections Framework (JCF)** is a set of **interfaces, implementations, and utility algorithms** used to store and process groups of objects. The main interfaces are **List**, **Set**, and **Queue**. A **List** maintains order and allows duplicates, a **Set** stores unique elements, and a **Queue** is used to process elements in a particular order. **Map** stores key-value pairs, but it does **not** extend the `Collection` interface. The framework provides reusable data structures such as `ArrayList`, `HashSet`, and `HashMap`, reducing the need to implement data storage from scratch.

**Keywords:** `List`, `Set`, `Queue`, `Map`, interfaces, implementations, reusable data structures.

---

## 2. HashMap Internals

### COLL-002 — How does HashMap work internally?

`HashMap` stores data as **key-value pairs** in an internal **array of buckets**. When we insert a key, Java calculates its **hash**, uses it to identify a bucket, and stores the entry there. During lookup, the hash helps find the bucket and **`equals()`** identifies the matching key. This gives `HashMap` an average **O(1)** time complexity for `put()` and `get()`, assuming hashes are distributed well. In cases of collisions, multiple entries can occupy the same bucket.

**Keywords:** hash, bucket array, `hashCode()`, `equals()`, key-value pair, average O(1).

### COLL-003 — How are HashMap collisions handled?

A **collision** occurs when different keys map to the same bucket. `HashMap` stores those entries in the same bucket and checks the hash and then **`equals()`** to identify the correct key. In modern Java, a heavily populated bucket can change from a linked structure into a **Red-Black tree**, improving lookup performance when many keys collide. Therefore, a collision does not mean that keys are equal; it only means they share a bucket.

**Keywords:** collision, same bucket, `equals()`, linked structure, Red-Black tree.

### COLL-004 — When does a HashMap bucket become a Red-Black tree?

In common OpenJDK implementations, a bucket is considered for **treeification** when it reaches around **8 entries**. Treeification generally requires the table capacity to be at least **64**; if the table is smaller, `HashMap` may resize instead. Tree bins help reduce lookup time in collision-heavy buckets. These thresholds are **implementation details**, so describe them as the behavior of modern Java implementations rather than an unchangeable API guarantee.

**Keywords:** treeification, threshold 8, capacity 64, resize, implementation detail.

### COLL-005 — What is the HashMap load factor?

The **load factor** controls how full a `HashMap` can become before it resizes. The default load factor is **0.75**. For example, a table with capacity **16** normally has a resize threshold of **12** entries. A lower load factor can reduce collisions but uses more memory, while a higher load factor saves table space but can increase collisions. The default is a practical balance between memory usage and lookup performance.

**Keywords:** load factor, default 0.75, capacity, threshold, memory, collisions.

### COLL-006 — What happens when a HashMap resizes?

When the number of entries crosses the resize **threshold**, `HashMap` increases the table capacity—normally by doubling it—and redistributes entries into the new table. Resizing requires additional processing and can temporarily make an insertion more expensive. If the expected number of entries is known, choosing a suitable initial capacity can reduce repeated resizing. Resizing improves the table's ability to handle growth while maintaining efficient average operations.

**Keywords:** threshold, capacity growth, redistribution, resizing cost, initial capacity.

### COLL-007 — Why does HashMap allow one null key?

`HashMap` explicitly supports **one null key** and multiple null values. It handles the null key as a special case, so a hash calculation is not needed for that key. This is a feature of `HashMap`, not a universal rule for all map implementations. For example, `ConcurrentHashMap` does not permit null keys or null values.

**Keywords:** one null key, null values, special case, implementation difference.

### COLL-008 — HashMap vs ConcurrentHashMap

`HashMap` is **not thread-safe** when multiple threads modify it concurrently without external coordination. `ConcurrentHashMap` is designed for **concurrent access**, allowing multiple threads to work with the map more safely and efficiently. It does not allow null keys or values and offers atomic methods such as **`putIfAbsent()`** and **`computeIfAbsent()`**. Use `HashMap` for ordinary non-concurrent access and `ConcurrentHashMap` when shared map access needs concurrency support.

**Keywords:** thread safety, concurrency, atomic methods, null restriction, `putIfAbsent()`.

### COLL-009 — Why does ConcurrentHashMap not allow null keys or values?

`ConcurrentHashMap` rejects null keys and values partly to keep lookup results unambiguous during concurrent operations. If `get(key)` returns **null**, the caller can interpret it as meaning that no mapping was found. If null values were permitted, it could be difficult to distinguish an absent mapping from a mapping whose value is null while other threads are changing the map.

**Keywords:** null ambiguity, concurrent lookup, absent mapping, predictable behavior.

### COLL-010 — How does ConcurrentHashMap achieve thread safety?

Modern `ConcurrentHashMap` implementations use techniques such as **CAS (Compare-And-Swap)** and **synchronization at individual bins** when required. This avoids locking the entire map for every operation and allows better concurrency than a single global lock. The exact internal implementation varies by Java version, but the main idea is to coordinate updates at a smaller scope while supporting concurrent access.

**Keywords:** CAS, bin-level locking, concurrency, no single global lock, thread safety.

### COLL-011 — ConcurrentHashMap in Java 7 vs Java 8+

In Java 7, `ConcurrentHashMap` used **segments**, each with its own locking strategy. In Java 8 and later, the implementation moved away from Segment-based storage and uses a table with **CAS**, synchronization at individual bins, and tree bins for heavy collisions. The goal is to improve concurrency and reduce contention. In an interview, focus on the design change rather than memorizing every internal detail.

**Keywords:** Java 7 segments, Java 8+, CAS, bin-level synchronization, reduced contention.

### COLL-012 — HashMap vs Hashtable vs ConcurrentHashMap

`HashMap` is not synchronized and allows one null key and null values. `Hashtable` is a **legacy synchronized** map and rejects null keys and values. `ConcurrentHashMap` is designed for modern concurrent use and also rejects nulls, but supports better concurrency than synchronizing the entire map for every operation. For most new code, choose `HashMap` for non-concurrent use and `ConcurrentHashMap` for concurrent access; use `Hashtable` mainly when maintaining legacy code.

**Keywords:** legacy, synchronized, null support, concurrency, modern choice.

---

## 3. List Implementations

### COLL-013 — ArrayList vs LinkedList

`ArrayList` uses a **resizable array**, so indexed reads are typically **O(1)**. Inserting or removing an element in the middle may require shifting elements, which takes **O(n)** time. `LinkedList` uses a **doubly linked list**; accessing an element by index takes **O(n)** because it must traverse nodes. Insertion or removal is efficient once the relevant node is reached, but finding that node may still take time. In general, `ArrayList` is the default choice for most everyday list use because it has lower overhead and good cache locality.

**Keywords:** resizable array, doubly linked list, O(1) indexed read, O(n) traversal, shifting, cache locality.

### COLL-014 — When would you prefer LinkedList over ArrayList?

I would consider `LinkedList` when the application frequently inserts or removes elements at the ends, or when it already has a reference to the node or iterator position where changes are needed. However, `LinkedList` is not automatically faster for insertion because finding a position by index takes **O(n)** time. For most general-purpose lists and frequent indexed reads, `ArrayList` is usually the better option.

**Keywords:** end operations, known node, iterator, traversal cost, practical trade-off.

### COLL-015 — ArrayList vs Vector

Both `ArrayList` and `Vector` use resizable arrays. The main difference is that `Vector` synchronizes its individual methods, while `ArrayList` does not. `Vector` is considered a **legacy** collection, and `ArrayList` is normally preferred in new code. Method-level synchronization also does not automatically make a sequence of multiple operations atomic; thread-safety requirements should be considered at the operation level.

**Keywords:** resizable array, synchronized methods, legacy, `ArrayList`, atomic operation.

### COLL-016 — HashSet vs LinkedHashSet vs TreeSet

`HashSet` stores unique elements without guaranteeing iteration order. `LinkedHashSet` also stores unique elements but **preserves insertion order**. `TreeSet` stores unique elements in **sorted order**, using natural ordering or a supplied `Comparator`; its basic operations are typically **O(log n)**. Choose based on whether you need uniqueness alone, insertion order, or sorted order.

**Keywords:** uniqueness, insertion order, sorted order, `Comparator`, O(log n).

### COLL-017 — HashMap vs LinkedHashMap vs TreeMap

`HashMap` stores key-value pairs without guaranteeing iteration order. `LinkedHashMap` maintains **insertion order** by default and can also be configured for access order, which is useful in patterns such as an LRU cache. `TreeMap` sorts keys using their natural ordering or a `Comparator`, with basic operations typically **O(log n)**. The right choice depends on whether ordering is required.

**Keywords:** no guaranteed order, insertion/access order, sorted keys, LRU, O(log n).

### COLL-018 — What is TreeSet?

`TreeSet` is an implementation of `Set` that stores **unique elements in sorted order**. It uses natural ordering or a supplied `Comparator` to compare elements. Its basic add, remove, and search operations are typically **O(log n)**. It is useful when the application needs a set of unique values that can be iterated in sorted order.

**Keywords:** `Set`, unique, sorted, natural ordering, `Comparator`, O(log n).

### COLL-019 — List vs LinkedList

`List` is an **interface** that represents an ordered collection and allows duplicate elements. `LinkedList` is a **concrete implementation** of that interface, backed by a doubly linked list; it also implements `Deque`. Other `List` implementations, such as `ArrayList`, use different internal structures. The key difference is that `List` defines behavior, while `LinkedList` provides a specific implementation.

**Keywords:** interface, implementation, ordered, duplicates, doubly linked list, `Deque`.

---

## 4. Equality and Hashing

### COLL-020 — `==` vs `equals()`

For primitive values, `==` compares the actual values. For object references, `==` checks whether both references point to the **same object**. The `equals()` method checks **logical equality** when the class overrides it; otherwise, the default implementation from `Object` behaves like identity comparison. For example, two different `String` objects may have equal content, so `equals()` can return true while `==` returns false.

**Keywords:** primitive value, reference identity, logical equality, `Object.equals()`.

### COLL-021 — Why override `hashCode()` when `equals()` is overridden?

The Java contract requires that **equal objects must have equal hash codes**. Hash-based collections use `hashCode()` to locate a bucket and `equals()` to identify the matching key. If a class overrides `equals()` but leaves an incompatible `hashCode()`, logically equal objects may be placed in different buckets, causing unexpected behavior in `HashMap` or `HashSet`. Therefore, override both methods consistently.

**Keywords:** contract, equal objects, same hash code, `HashMap`, `HashSet`.

### COLL-022 — What is the `equals()` and `hashCode()` contract?

The `equals()` contract requires equality to be **reflexive, symmetric, transitive, consistent**, and false when compared with null. The `hashCode()` contract says that if two objects are equal according to `equals()`, they must return the same hash code. Unequal objects are allowed to have the same hash code, which is called a collision. Hash codes should remain consistent while the information used by equality remains unchanged.

**Keywords:** reflexive, symmetric, transitive, consistent, null, hash contract, collision.

### COLL-023 — How does HashMap use `hashCode()` and `equals()`?

When a key is inserted or searched, `HashMap` uses the key's hash to determine the bucket. It then compares candidate entries using the hash and **`equals()`** to find the matching key. If an equal key already exists, the associated value is updated; otherwise, a new entry is stored. This is why custom key classes must implement `equals()` and `hashCode()` correctly.

**Keywords:** bucket selection, hash comparison, `equals()`, key lookup, custom key.

### COLL-024 — What happens when two objects have the same hash code?

When two objects have the same hash code, a **hash collision** may occur. `HashMap` can store both keys in the same bucket and uses `equals()` to distinguish them. Having the same hash code does **not** mean that two objects are equal. A good hash function reduces collisions, but a collection must still handle them correctly.

**Keywords:** collision, same bucket, `equals()`, hash quality, not necessarily equal.

### COLL-025 — What if two equal objects have different hash codes?

That violates the `equals()`/`hashCode()` contract. A hash-based collection may put the objects in different buckets and fail to find an entry when looked up using an equal object. For example, a `HashSet` may appear to contain duplicate logical values. The fix is to ensure that both methods use consistent fields to determine equality and hash codes.

**Keywords:** contract violation, different buckets, failed lookup, consistent fields.

---

## 5. Immutability and Keywords

### COLL-026 — How do you create an immutable class?

An **immutable class** does not allow its observable state to change after construction. A common design is to make the class `final`, keep fields `private final`, initialize them in the constructor, and provide no setters. If a field refers to a mutable object, make a **defensive copy** when accepting it and when returning it. This prevents callers from changing the object's internal state through shared references.

**Keywords:** immutable state, `private final`, constructor, no setters, defensive copy.

### COLL-027 — Why should an immutable class generally be `final`?

Making a class `final` prevents subclassing that could add mutable fields or override methods in a way that breaks the immutability guarantee. It is a simple and common safeguard for immutable value objects. `final` alone does not make a class immutable, though; the fields, constructor, and references to mutable objects must also be handled correctly.

**Keywords:** subclassing, mutable state, guarantee, `final` is not sufficient alone.

### COLL-028 — What is a defensive copy, and why is it needed?

A **defensive copy** creates a separate copy of mutable data so that external code cannot modify an object's internal state through a shared reference. For example, an immutable class receiving a `Date` or a mutable list should copy it rather than store or return the original reference directly. If the object contains nested mutable objects, a **deep copy** may be required; a shallow copy only duplicates the outer container.

**Keywords:** defensive copy, shared reference, mutable object, shallow copy, deep copy.

### COLL-029 — What is the `final` keyword used for?

The meaning of `final` depends on where it is used. A **final variable** can be assigned only once; a **final method** cannot be overridden; and a **final class** cannot be subclassed. For object references, `final` prevents reassignment of the reference, but it does not prevent the referenced object's internal state from changing. For example, a final `List` reference may still point to a list whose elements can be modified.

**Keywords:** variable, method, class, reassignment, reference vs object state.

### COLL-030 — What is the `transient` keyword?

In Java, the `transient` modifier marks an instance field to be excluded from **default Java serialization**. It is useful when a field should not be included in the serialized form, such as a temporary or derived value. It does not automatically exclude a field from JSON serialization, database persistence, or every other serialization framework; those mechanisms have their own rules.

**Keywords:** `transient`, default Java serialization, instance field, framework-specific behavior.

### COLL-031 — Java `transient` vs JPA `@Transient`

Java's `transient` keyword affects **default Java serialization**. JPA's `@Transient` annotation tells the persistence provider that a field or property should **not be persisted** to the database as part of the entity mapping. They solve different problems: one concerns Java serialization, while the other concerns ORM persistence. Use the one that matches the behavior you need; they are not interchangeable.

**Keywords:** Java serialization, JPA, ORM, persistence, annotation vs keyword.

### COLL-032 — `final` vs `finally` vs `finalize()`

`final` is a keyword used to restrict reassignment, overriding, or inheritance. `finally` is a block used with exception handling to run cleanup code in normal control-flow cases. `finalize()` was a method associated with garbage-collection cleanup, but it is **deprecated for removal** and should not be used for resource management. Prefer **try-with-resources** for resources such as streams and database connections.

**Keywords:** `final`, `finally`, `finalize()`, deprecated, try-with-resources.

---

# Rapid Revision Guide

## One-line reminders

- **Collections:** `List` = ordered, duplicates; `Set` = unique; `Queue` = processing order; `Map` = key-value pairs and is not a subtype of `Collection`.
- **HashMap:** hash selects a bucket; `equals()` identifies the key; average `get()`/`put()` is **O(1)**.
- **Collision:** different keys can share a bucket; equal hash codes do not imply equal objects.
- **Treeification:** modern OpenJDK commonly considers treeification around **8 entries** when capacity is at least **64**.
- **Load factor:** default **0.75**; capacity 16 normally gives threshold 12.
- **Concurrency:** `HashMap` is not thread-safe for concurrent mutation; `ConcurrentHashMap` supports concurrent access and rejects nulls.
- **List choice:** `ArrayList` is usually the general-purpose default; `LinkedList` is useful for specific node/end operations.
- **Set choice:** `HashSet` = no guaranteed order; `LinkedHashSet` = insertion order; `TreeSet` = sorted.
- **Map choice:** `HashMap` = no guaranteed order; `LinkedHashMap` = insertion/access order; `TreeMap` = sorted keys.
- **Equality:** equal objects must have equal hash codes; same hash code does not imply equality.
- **Immutability:** private final fields, no setters, defensive copies, safe construction.
- **`final`:** variable cannot be reassigned; method cannot be overridden; class cannot be subclassed.
- **`transient` vs `@Transient`:** Java serialization vs JPA persistence.
- **`finalize()`:** deprecated for removal; use try-with-resources for resource cleanup.

## Interview answer structure

For any question, remember this sequence:

1. **Definition** — Explain what it is.
2. **Working** — Explain how it works internally.
3. **Trade-off** — Mention performance, safety, or limitations.
4. **Use case** — Say when you would choose it in a real application.

## High-priority topics to explain without memorizing word-for-word

1. `HashMap` internal working, collisions, resizing, and treeification.
2. `HashMap` vs `ConcurrentHashMap`.
3. `ArrayList` vs `LinkedList`.
4. `equals()` and `hashCode()` contract.
5. Designing an immutable class with defensive copies.
6. `final` vs `finally` vs `finalize()`.
