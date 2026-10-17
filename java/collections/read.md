# Java Collections, Equality & Immutability — Interview Notes

**Target level:** Java Developer, 3.6 years of experience  
**Purpose:** Conceptual explanations that can be spoken naturally in an interview, plus a rapid-revision guide at the end.

## How to use these notes

For a conceptual question, use this speaking order: **definition → how it works → when/why it matters → trade-off or caveat**. Avoid reciting implementation details unless the interviewer asks for them. For scenario questions, connect the concept to a real application: correctness, concurrency, performance, or maintainability.

---

# 1. Java Collections Framework

## COLL-001 — Explain the Java Collections Framework.

The **Java Collections Framework (JCF)** is a set of interfaces, implementations, and algorithms for storing and manipulating groups of objects consistently. It saves us from implementing common data structures from scratch and lets us choose a structure based on the operations we need.

### Main interfaces and implementations

- **`Collection`** is the root interface for groups of elements. Its major subinterfaces are:
  - **`List`** — ordered, index-based, allows duplicates. Examples: `ArrayList`, `LinkedList`.
  - **`Set`** — does not allow duplicate elements. Examples: `HashSet`, `LinkedHashSet`, `TreeSet`.
  - **`Queue` / `Deque`** — designed for processing elements, often in FIFO order or at both ends. Examples: `PriorityQueue`, `ArrayDeque`, `LinkedList`.
- **`Map`** stores key-value pairs. It is part of the framework but **does not extend `Collection`**. Examples: `HashMap`, `LinkedHashMap`, `TreeMap`, `Hashtable`, `ConcurrentHashMap`.
- **`Collections`** is a utility class with methods such as sorting, reversing, and creating wrappers; it is different from the `Collection` interface.
- The framework also provides algorithms and utilities such as `Collections.sort`, `Collections.binarySearch`, and methods for unmodifiable or synchronized views.

### How I would choose a collection

- Need fast index-based reads and mostly append operations: `ArrayList`.
- Need unique elements with no ordering requirement: `HashSet`.
- Need predictable insertion order: `LinkedHashSet` or `LinkedHashMap`.
- Need sorted keys/elements or range operations: `TreeMap` or `TreeSet`.
- Need key-value lookups: `HashMap`.
- Need concurrent access by multiple threads: choose a suitable concurrent collection, such as `ConcurrentHashMap`, rather than assuming ordinary collections are thread-safe.

**Interview-ready answer:** “The Java Collections Framework provides standard interfaces such as List, Set, Queue and Map, along with implementations like ArrayList, HashSet and HashMap. I choose an implementation based on ordering, uniqueness, lookup patterns, performance and thread-safety requirements. One important distinction is that Map belongs to the framework but does not extend Collection.”

---

# 2. HashMap Internals

## COLL-002 — How does HashMap work internally?

`HashMap` stores key-value pairs in a hash table. Internally, it uses an array of buckets. Each key's `hashCode()` helps determine a bucket index, and `equals()` is used to distinguish keys that land in the same bucket.

### What happens during `put(key, value)`?

1. It computes a hash from the key's `hashCode()`; modern JDK implementations also spread high hash bits into lower bits.
2. It uses the table capacity to calculate a bucket index, typically `(n - 1) & hash` when the capacity is a power of two.
3. If the bucket is empty, it inserts a node there.
4. If that bucket already contains an entry, it compares hashes and then key identity / `equals()` to determine whether the key already exists. If it does, the value is replaced. Otherwise, the new entry is added to the bucket's collision structure.
5. If the number of entries crosses the resize threshold, the table may grow.

### What happens during `get(key)`?

HashMap calculates the key's hash and bucket index, checks the bucket, and compares candidates using hash and key equality. With well-distributed hashes, `get` and `put` are **expected O(1)**. Heavy collisions can make operations slower; tree bins help manage long collision chains.

### Important details

- Default load factor is **0.75**.
- The default initial capacity is commonly described as **16**, but the internal table is allocated lazily on the first insertion in modern implementations.
- HashMap allows one `null` key and multiple `null` values.
- It is **not thread-safe**. Concurrent writes without synchronization can produce incorrect behavior.
- Keys should have stable `equals()` and `hashCode()` results while stored in the map. Mutating fields used by those methods can make an entry effectively unreachable through a later lookup.

**Interview-ready answer:** “HashMap uses an array of buckets. It calculates a hash from the key, locates a bucket, and uses `equals()` to identify the exact key within that bucket. Collisions are stored in a bucket structure, and sufficiently large collision bins can become red-black trees in modern implementations. Average lookup and insertion are O(1), assuming good hash distribution.”

## COLL-003 — How are HashMap collisions handled?

A **collision** occurs when two different keys are assigned to the same bucket index. It does not necessarily mean the keys have identical `hashCode()` values; different hash values can still map to the same bucket.

In modern Java implementations, a bucket can contain:

1. **A linked list of nodes** when there are only a few collisions.
2. **A red-black tree** when a bucket becomes sufficiently large and the table is sufficiently large. This improves search behavior for long collision bins.

When inserting into a bucket, HashMap compares hashes and then checks key identity or `equals()`. If an equal key is already present, its value is replaced rather than adding a duplicate key.

**Example:** Two unequal employee objects can have the same hash. Both can exist as separate keys in a `HashMap`, because `equals()` says they are different.

**Interview-ready answer:** “HashMap handles collisions by keeping multiple entries in the same bucket. In current JDK implementations, the bucket starts as a linked list and can become a red-black tree when the collision count and table capacity meet the treeification conditions. Hash and `equals()` together determine whether a key already exists.”

## COLL-004 — When does a HashMap bucket become a Red-Black tree?

In standard OpenJDK HashMap implementations, the important constants are:

- `TREEIFY_THRESHOLD = 8`
- `UNTREEIFY_THRESHOLD = 6`
- `MIN_TREEIFY_CAPACITY = 64`

A bucket with around **8 or more nodes** can trigger treeification, but the table generally must have a capacity of at least **64**. If the table is smaller, HashMap prefers to resize the table instead of converting the bucket to a tree, because a resize may spread the colliding entries across different buckets.

During resizing, a tree bin may be converted back into a linked list when its split groups become small enough; the implementation uses the untreeification threshold in that process.

These are **implementation details of common OpenJDK versions**, not abstract guarantees that every possible Java implementation must follow. For an interview, state the common thresholds and explain the purpose rather than implying they are part of the `Map` interface contract.

**Why it matters:** A linked-list lookup can take O(n) within a bucket. Tree bins can provide O(log n) search behavior under suitable conditions, though the exact behavior also depends on key comparison and implementation details.

## COLL-005 — What is the HashMap load factor?

The **load factor** controls how full the hash table can become before HashMap resizes it. The resize threshold is approximately:

`threshold = capacity × loadFactor`

With the usual default load factor of `0.75` and a table capacity of 16, the threshold is 12 entries. When the number of entries exceeds the threshold, the table grows and the entries are redistributed.

- A **lower** load factor may reduce collisions but uses more memory and can resize sooner.
- A **higher** load factor may save table space but can increase collisions and lookup cost.
- `0.75` is a practical compromise for general use.

If the expected number of entries is known and large, choosing an appropriate initial capacity can reduce repeated resizes. Do not confuse the load factor with a hard maximum number of entries: it is a threshold for resizing, not a limit on map size.

**Interview-ready answer:** “Load factor controls how full HashMap is allowed to get before resizing. With capacity 16 and load factor 0.75, the threshold is 12. A lower factor trades extra memory for fewer collisions; a higher factor trades memory savings for potentially more collisions.”

## COLL-006 — What happens when a HashMap resizes?

When the number of mappings crosses the resize threshold, HashMap increases the table capacity—commonly doubling it—and redistributes entries into the new table.

In modern OpenJDK implementations, the capacity normally doubles. Since the capacity is a power of two, the implementation can often decide whether a node stays at its old index or moves by the old capacity, using a bit test on its stored hash. It generally does not need to call every key's `hashCode()` again because the node stores its hash value.

**Why resizing matters:** It reduces the average density of entries per bucket and helps maintain expected constant-time operations. But resizing costs time and memory temporarily. Repeated unnecessary resizing can hurt performance, so a sensible initial capacity is useful when the expected size is known.

**Interview-ready answer:** “During resize, HashMap grows its bucket array, usually by doubling capacity, and redistributes existing nodes according to the new capacity. It is an O(n)-scale operation for the table, although the implementation optimizes redistribution. Proper initial sizing can reduce resize overhead.”

## COLL-007 — Why does HashMap allow one null key?

HashMap explicitly supports one `null` key and multiple `null` values. There can be only one mapping for a `null` key because a map cannot contain duplicate keys under its equality semantics. Adding another `null` key replaces the value associated with the existing `null` key.

In common implementations, a `null` key is handled as a special case and assigned a hash value of zero, allowing it to be stored in a bucket. Multiple entries can have `null` values because values do not need to be unique.

Do not overstate the design rationale as a strict specification. The important interview point is the behavior and the contrast with `ConcurrentHashMap`, which rejects null keys and values.

```java
Map<String, String> map = new HashMap<>();
map.put(null, "first");
map.put(null, "second"); // replaces the prior mapping for null
map.put("A", null);
map.put("B", null);      // multiple null values are allowed
```

---

# 3. HashMap vs ConcurrentHashMap

## COLL-008 — What is the difference between HashMap and ConcurrentHashMap?

| Aspect | `HashMap` | `ConcurrentHashMap` |
|---|---|---|
| Thread safety | Not thread-safe | Designed for concurrent access |
| Null keys/values | Allows one null key and null values | Does not allow null keys or values |
| Concurrent updates | Requires external coordination if shared between threads | Provides thread-safe operations and atomic methods such as `putIfAbsent` and `compute` |
| Reads | No concurrency guarantees for unsynchronized sharing | Usually highly concurrent; retrievals generally do not require locking the entire map |
| Iterators | Fail-fast on a best-effort basis | Weakly consistent; may reflect some concurrent updates and do not normally throw `ConcurrentModificationException` just because another thread updates the map |
| Typical use | Local or externally synchronized map | Shared map updated/read by multiple threads |

**Important:** Thread-safe map operations do not automatically make a multi-step business operation atomic. For example, `if (!map.containsKey(k)) { map.put(k, v); }` is still a race when performed as two separate calls. Use an atomic operation such as `putIfAbsent`, `computeIfAbsent`, or suitable explicit synchronization.

**Interview-ready answer:** “I use HashMap when the map is not concurrently shared or when the caller provides synchronization. I use ConcurrentHashMap for shared concurrent access because its individual operations and certain compound methods are thread-safe. It also disallows nulls, so a `null` result from `get` can unambiguously mean no mapping was found.”

## COLL-009 — Why does ConcurrentHashMap not allow null keys or values?

The core reason is to keep concurrent retrieval and atomic operations **unambiguous**. In ConcurrentHashMap, `get(key) == null` means that no mapping was found. If null values were allowed, a caller could not distinguish “key absent” from “key present with a null value” using `get()` alone, especially while other threads may update the map concurrently.

Rejecting null keys also simplifies the concurrent map's operation semantics. A null key or null value passed to operations that prohibit them results in a `NullPointerException`.

**Interview-ready answer:** “ConcurrentHashMap rejects nulls so that null from `get` consistently means there is no mapping. That matters when reads and writes happen concurrently, and it keeps methods such as `putIfAbsent` and computation methods easier to reason about.”

## COLL-010 — How does ConcurrentHashMap achieve thread safety?

In modern Java implementations, ConcurrentHashMap achieves concurrency using a combination of techniques rather than synchronizing the entire map for every operation:

- **Volatile reads/writes and memory-ordering guarantees** help threads observe published updates correctly.
- **CAS (compare-and-set)** is used for certain updates, particularly where a bucket is initially empty.
- **Synchronization on a bucket/bin head** is used for certain updates involving an existing bin, rather than locking the whole map.
- **Tree bins** can help manage heavily-collided buckets.
- **Cooperative resizing** lets multiple threads help transfer buckets during table growth.
- Its atomic methods—such as `putIfAbsent`, `replace`, `compute`, and `merge`—support common update patterns without a separate external lock around the full operation.

The exact implementation differs by Java version. Explain the principles rather than promising that every method is entirely lock-free. ConcurrentHashMap offers high expected concurrency, not a guarantee that contention is impossible.

**Practical example:** To create a value only if a key has no mapping, use:

```java
cache.computeIfAbsent(key, k -> loadValue(k));
```

The mapping function should be short and should not recursively update the same map in problematic ways. For expensive I/O, consider whether doing the work inside the mapping function creates avoidable contention or needs a separate loading strategy.

## COLL-011 — What changed in ConcurrentHashMap between Java 7 and Java 8+?

The major change is the internal concurrency design:

- **Java 7:** used a segmented architecture. The map was divided into segments, each with its own locking, allowing multiple segments to be updated concurrently.
- **Java 8 and later:** moved away from the fixed segment-based design. It uses a bucket-array approach, CAS for some updates, synchronization around affected bins for others, tree bins for heavy collisions, and cooperative resizing.

The newer architecture can provide more flexible concurrency and simpler retrieval paths. The old constructor's concurrency-level parameter was important to the segment-oriented design; in later implementations, it is not a promise of a fixed number of lock segments.

**Interview-ready answer:** “Java 7 ConcurrentHashMap used segments with separate locks. Java 8 replaced that model with a bucket-oriented design using CAS, synchronization at bin level, tree bins and cooperative resizing. The goal was to reduce unnecessary contention and improve scalability.”

## COLL-012 — What is the difference between HashMap, Hashtable and ConcurrentHashMap?

| Feature | `HashMap` | `Hashtable` | `ConcurrentHashMap` |
|---|---|---|---|
| Thread safety | No | Synchronized methods; coarse-grained locking | Designed for high-concurrency access |
| Null key/value | One null key and null values allowed | Null keys/values not allowed | Null keys/values not allowed |
| Legacy status | Modern general-purpose map | Legacy class | Modern concurrent map |
| Locking / concurrency | No built-in coordination | Typically locks the whole table operation | Fine-grained / bucket-level coordination and atomic methods in modern implementations |
| Iterator behavior | Fail-fast, best effort | Legacy enumerations and fail-fast collection views | Weakly consistent iterators |

**Which would I choose?** For ordinary non-concurrent use, `HashMap`. For a shared concurrent map, normally `ConcurrentHashMap`. `Hashtable` is largely a legacy option; synchronizing every method does not automatically make a sequence of multiple operations atomic or provide the concurrency characteristics of ConcurrentHashMap.

---

# 4. List Implementations

## COLL-013 — What is the difference between ArrayList and LinkedList?

| Aspect | `ArrayList` | `LinkedList` |
|---|---|---|
| Internal structure | Resizable array | Doubly linked list |
| `get(index)` | O(1) typical | O(n), because it traverses nodes |
| Append | Amortized O(1); occasional resize | O(1) at the end |
| Insert/remove in middle by index | O(n) due to shifting elements | O(n) to find the position; O(1) to relink once the node is reached |
| Memory | Usually lower per element | Higher per element because each node stores links |
| Cache locality | Better in many workloads because elements are stored in an array | Usually poorer because nodes may be distributed in memory |
| Interfaces | `List`, `RandomAccess` | `List`, `Deque` |

**Practical choice:** `ArrayList` is the default choice for most list use cases because it offers fast indexed reads and good memory locality. Use `LinkedList` only when its deque/list behavior genuinely fits the workload; for a queue/deque, `ArrayDeque` is often a better option.

**Important nuance:** Saying “LinkedList insertion is O(1)” without mentioning how the insertion position is found is incomplete. Inserting through a known list iterator/node can relink in O(1), but locating an arbitrary index is O(n).

## COLL-014 — When would you prefer LinkedList over ArrayList?

I would consider `LinkedList` when the application needs frequent insertions/removals at the beginning or end and I already have an iterator positioned at the required location, or when I specifically need the `Deque` operations it provides.

However, for normal application work, `ArrayList` is often better due to lower memory overhead and better cache locality. If the requirement is simply queue/deque operations at both ends, `ArrayDeque` is usually worth considering before `LinkedList`.

**Interview-ready answer:** “I would not choose LinkedList simply because a workload has insertions. For an insertion by index, it still takes O(n) to find the node. It is useful when nodes can be manipulated through an existing iterator or when its deque behavior suits the use case. For most general-purpose lists, I prefer ArrayList.”

## COLL-015 — What is the difference between ArrayList and Vector?

Both are resizable-array implementations of `List`, but their concurrency model differs:

- **ArrayList:** not synchronized by default; better suited to ordinary single-threaded use or cases where synchronization is managed externally.
- **Vector:** a legacy class whose individual methods are synchronized. This adds synchronization overhead and does not automatically make a multi-step sequence atomic.
- Both support indexed access and grow when necessary. Their exact capacity-growth policies differ by implementation and should not be the primary reason for choosing one.

For new code, `ArrayList` is usually preferred. If multiple threads must safely update a list, choose an appropriate concurrent design or synchronize the necessary compound operations; do not select Vector solely because every method is synchronized.

---

# 5. Set and Map Variants

## COLL-016 — What is the difference between HashSet, LinkedHashSet and TreeSet?

| Feature | `HashSet` | `LinkedHashSet` | `TreeSet` |
|---|---|---|---|
| Duplicate elements | Not allowed | Not allowed | Not allowed according to set/comparator equality semantics |
| Ordering | No guaranteed iteration order | Maintains insertion order | Sorted order based on natural ordering or a comparator |
| Typical operation cost | Expected O(1) | Expected O(1), with some ordering overhead | O(log n) for basic operations |
| Internal idea | Backed by a HashMap | Backed by a linked hash table / map | Backed by a TreeMap (a balanced tree) |
| Best use | Uniqueness and fast membership checks | Uniqueness plus predictable insertion-order iteration | Uniqueness plus sorted iteration and range operations |

**Example:** Use a `HashSet` to check whether an ID has already been seen; `LinkedHashSet` when output order should follow the order items were first encountered; `TreeSet` when the data must remain sorted.

**Caveat:** A TreeSet determines whether two elements occupy the same set position using comparison (`compareTo` or a `Comparator`). The comparison should normally be consistent with `equals()`, otherwise its behavior can be surprising compared with other Set implementations.

## COLL-017 — What is the difference between HashMap, LinkedHashMap and TreeMap?

| Feature | `HashMap` | `LinkedHashMap` | `TreeMap` |
|---|---|---|---|
| Key iteration order | No guaranteed order | Insertion order by default; can also be configured for access order | Sorted by key using natural ordering or comparator |
| Typical `get` / `put` cost | Expected O(1) | Expected O(1), with linked-order bookkeeping | O(log n) |
| Null keys | Allows one | Usually allows one | Natural ordering typically rejects null keys; behavior can depend on the comparator |
| Common use | General key-value lookup | Predictable iteration order, or access-order structures useful for LRU designs | Sorted keys and range queries |

Use `HashMap` when order does not matter. Use `LinkedHashMap` when order matters or you need access-order iteration as part of a cache design. Use `TreeMap` when you need sorted keys or navigational operations such as `firstKey`, `floorKey`, `ceilingKey`, or range views.

## COLL-018 — What is TreeSet?

`TreeSet` is a `NavigableSet` implementation that stores unique elements in sorted order. It is backed by a `TreeMap`, which uses a balanced tree, so basic operations such as `add`, `remove`, and `contains` generally take O(log n).

Elements are ordered either by their natural ordering (`Comparable`) or by a supplied `Comparator`. It also supports navigation and range operations, such as finding the least element greater than or equal to a value or selecting a subset over a range.

**Important:** For TreeSet, comparison result `0` means the elements are considered the same position for set purposes. If the comparator says two objects compare as zero but their `equals()` method says they are different, TreeSet will still treat them as duplicates. Ideally, comparison should be consistent with `equals()`.

## COLL-019 — What is the difference between List and LinkedList?

`List` is an **interface** that defines an ordered collection with positional access and normally permits duplicates. `LinkedList` is a **concrete implementation** of that interface using a doubly linked list; it also implements `Deque`.

This is an interface-versus-implementation question, not a comparison between two equivalent classes. In code, programming to the interface improves flexibility:

```java
List<String> names = new ArrayList<>();
names.add("Asha");
names.add("Asha"); // List permits duplicates
```

The implementation can be changed to `LinkedList` when its specific characteristics are justified, without changing code that only depends on the `List` interface.

---

# 6. Equality and Hashing

## COLL-020 — What is the difference between `==` and `equals()`?

For primitive values, `==` compares the actual primitive values. For object references, `==` checks whether both references point to the **same object**. `equals()` is a method whose implementation defines logical equality; unless a class overrides it, `Object.equals()` behaves like reference equality.

```java
String a = new String("java");
String b = new String("java");

System.out.println(a == b);      // false: two objects
System.out.println(a.equals(b)); // true: same String content
```

The `String` class overrides `equals()` to compare character content. Your own domain class should override `equals()` when logical equality is meaningful—for example, two `EmployeeId` value objects with the same identifier might be considered equal.

**Interview caveat:** Arrays do not override `equals()` for element-wise comparison; `array1.equals(array2)` is reference equality. Use `Arrays.equals` or `Arrays.deepEquals` when content comparison is required.

## COLL-021 — Why should `hashCode()` be overridden when `equals()` is overridden?

Hash-based collections such as `HashMap` and `HashSet` use `hashCode()` to find a bucket and `equals()` to determine whether a matching key/element is already present. The contract says that if two objects are equal according to `equals()`, they **must return the same hash code**.

If you override only `equals()` but keep the default identity-based `hashCode()`, two logically equal objects can be directed to different buckets. A HashSet might then retain both, or a HashMap lookup using an equal-but-distinct key might fail.

```java
final class EmployeeId {
    private final String value;

    EmployeeId(String value) { this.value = value; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EmployeeId other)) return false;
        return java.util.Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value);
    }
}
```

**Interview-ready answer:** “Whenever I override equals, I override hashCode as well. Equal objects must have equal hash codes; otherwise hash-based collections may fail to find logically equal keys or may store unexpected duplicates.”

## COLL-022 — What is the `equals()` and `hashCode()` contract?

### `equals()` contract

For a well-behaved equality implementation, it should be:

1. **Reflexive:** `x.equals(x)` is true.
2. **Symmetric:** if `x.equals(y)` is true, `y.equals(x)` is true.
3. **Transitive:** if `x.equals(y)` and `y.equals(z)` are true, `x.equals(z)` is true.
4. **Consistent:** repeated calls return the same result if the equality-relevant state has not changed.
5. **Non-null:** `x.equals(null)` is false.

### `hashCode()` contract

- If two objects are equal according to `equals()`, they must have the same hash code.
- Unequal objects **may** have the same hash code; collisions are allowed.
- The hash code should remain consistent while fields used by equality remain unchanged during an execution.

**Memory aid:** **Equal ⇒ same hash. Same hash ⇏ equal.**

## COLL-023 — How does HashMap use `hashCode()` and `equals()`?

HashMap first uses the key's hash to choose a bucket. It then examines entries in that bucket and uses the stored hash plus key identity or `equals()` to identify a matching key.

- `hashCode()` helps narrow down where the entry should be.
- `equals()` confirms whether a candidate key is logically the same key.
- If an equal key is already present, `put` replaces its value rather than adding a second mapping for that equal key.

A correct `equals`/`hashCode` implementation is therefore essential. Also avoid changing the equality-relevant fields of a key after insertion. If the key's hash changes, a later lookup may search a different bucket than the one containing the entry.

## COLL-024 — What happens when two objects have the same hash code?

The objects have a **hash collision**, but they can still be different keys. HashMap stores them in the same bucket and uses `equals()` to distinguish them. If they are not equal, both can be stored as separate mappings.

For example, two unequal `Employee` objects may return the same integer from `hashCode()`. This is allowed by the Java contract. It can affect performance if too many keys collide, but it does not by itself break correctness when equality and hashing are implemented properly.

**Keep this distinction clear:** same hash code does not imply that objects are equal.

## COLL-025 — What happens if two equal objects have different hash codes?

That violates the `equals()`/`hashCode()` contract. A hash-based collection may place the objects in different buckets and may not recognize them as the same logical key.

For example, inserting one object into a HashSet and then checking `contains` with another object that is equal but has a different hash code can return `false`. A HashMap can also fail to locate the mapping through the equal object. It may appear to retain duplicate logical keys.

**How to prevent this:** Implement `equals()` and `hashCode()` using the same equality-relevant fields, and test them together. IDE generation and `Objects.equals` / `Objects.hash` can help, but the field choices still need to be correct.

---

# 7. Immutability

## COLL-026 — How do you create an immutable class?

An **immutable object** cannot have its observable state changed after construction. Immutability simplifies reasoning, makes objects easier to share between threads, and reduces the risk of accidental changes to values used as map keys.

### Practical checklist

1. Make the class `final` unless you have a carefully designed alternative that prevents unsafe subclassing.
2. Make fields `private final` where possible.
3. Initialize all state in the constructor; do not provide setters.
4. Validate constructor inputs.
5. Do not expose mutable internal objects directly. Make defensive copies on input and, where needed, on output.
6. Ensure referenced objects are immutable too, or protect/copy their mutable state. A final reference alone does not make the referred-to object immutable.

Example with a mutable `Date` field:

```java
public final class EmployeeRecord {
    private final String name;
    private final java.util.Date joiningDate;

    public EmployeeRecord(String name, java.util.Date joiningDate) {
        this.name = java.util.Objects.requireNonNull(name);
        this.joiningDate = new java.util.Date(
                java.util.Objects.requireNonNull(joiningDate).getTime());
    }

    public String getName() {
        return name;
    }

    public java.util.Date getJoiningDate() {
        return new java.util.Date(joiningDate.getTime());
    }
}
```

The constructor copies the mutable date coming in; the getter returns another copy so callers cannot mutate the internal date. In modern code, using an immutable type such as `LocalDate` is often simpler.

**Interview-ready answer:** “I use private final fields, initialize them once, avoid setters, and never expose mutable internal objects. I defensively copy mutable inputs and outputs. I also check the whole object graph, because a final field that refers to a mutable list does not make the list immutable.”

## COLL-027 — Why should an immutable class generally be `final`?

Making an immutable class `final` prevents subclasses from overriding methods or adding behavior/state that undermines the intended immutability contract. Without that restriction, a subclass could introduce mutable state or override an accessor in a way that returns changing data, making code that trusts the base type's immutability unsafe.

However, `final` is not the only theoretical way to design an immutable abstraction, and a class being `final` is **not sufficient on its own** to make it immutable. Its fields and reachable object graph must also be protected from mutation.

**Interview-ready answer:** “I generally make immutable value classes final to prevent a subclass from weakening their guarantees. But final is only one part of immutability; the constructor, fields, and mutable references must also be handled correctly.”

## COLL-028 — What is a defensive copy, and why is it needed?

A **defensive copy** is a separate copy of a mutable object made to prevent outside code from changing the class's internal state—or to prevent the caller from changing state after passing it into the constructor.

There are two common directions:

- **Copy on input:** Copy a mutable argument in the constructor or setter so the caller retains no mutable handle to the object's internal state.
- **Copy on output:** Return a copy from a getter rather than exposing the internal mutable object.

```java
public final class Settings {
    private final java.util.List<String> roles;

    public Settings(java.util.List<String> roles) {
        this.roles = new java.util.ArrayList<>(roles); // copy on input
    }

    public java.util.List<String> getRoles() {
        return new java.util.ArrayList<>(roles);       // copy on output
    }
}
```

In this example, the list structure is copied. That is a **shallow copy**: if the list contains mutable objects, those elements are still shared unless copied or made immutable as well. `List.copyOf(...)` can create an unmodifiable snapshot of the list structure, but it is also shallow; it does not make mutable elements deeply immutable.

**Why interviewers care:** Defensive copies protect encapsulation and are a key tool for building robust immutable objects.

## COLL-029 — What is the `final` keyword used for?

`final` has different meanings depending on where it is applied:

- **Final variable:** Can be assigned only once. For a reference variable, the reference cannot be reassigned, but the object it points to may still be mutable.
- **Final method:** Cannot be overridden by a subclass.
- **Final class:** Cannot be subclassed.
- **Final parameter or local variable:** Cannot be reassigned after initialization/assignment under the language rules. This is useful for expressing intent and can be required for captured local variables in lambdas (effectively-final variables may also be captured).

Example:

```java
final List<String> names = new ArrayList<>();
names.add("Asha");               // allowed: object is mutable
// names = new ArrayList<>();     // compile-time error: reference reassignment
```

**Common trap:** `final` does not mean “the object is immutable.” It means the variable, method, or class has restrictions based on where the keyword appears.

## COLL-030 — What is the `transient` keyword?

`transient` is a Java language modifier used for instance fields to say that the field should be skipped by the **default Java serialization mechanism**. It can be useful for values that should not be serialized, such as a cached or derived field, or a field that should not be persisted in a serialized representation for security or lifecycle reasons.

```java
class UserSession implements java.io.Serializable {
    private String username;
    private transient String temporaryToken;
}
```

When the default serialization mechanism serializes this object, `temporaryToken` is not included in the default serialized field data. A transient field is not necessarily reset to `null` for every possible serialization framework; custom serialization and frameworks may have their own behavior.

**Important distinctions:**

- `transient` is related to Java's built-in serialization mechanism.
- `static` fields are associated with the class rather than each instance and are not serialized as ordinary instance fields by default.
- `transient` does not mean that a field cannot be read or used while the application is running.

## COLL-031 — What is the difference between Java `transient` and JPA `@Transient`?

They serve different purposes:

| Aspect | Java `transient` modifier | JPA `@Transient` annotation |
|---|---|---|
| Purpose | Excludes a field from default Java object serialization | Marks an entity field/property as not persistent, so the persistence provider should not map it to database state |
| Package / language | Java language modifier | `jakarta.persistence.Transient` (or legacy `javax.persistence.Transient`) annotation |
| Typical use | A temporary token, runtime cache or non-serializable runtime state | A calculated entity property that should not be stored in a database column |
| Main concern | Object serialization | ORM persistence mapping |

Example:

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
class Product {
    @Id
    private Long id;
    private double price;
    private double taxRate;

    @Transient
    private double displayPrice; // not persisted by JPA
}
```

The exact annotation package depends on whether the project uses Jakarta Persistence or an older JPA version. In persistence mapping, the Java `transient` modifier also has persistence-related significance under JPA field access rules, but `@Transient` expresses the ORM-specific intent explicitly and can also be used on a property. Do not say that the two terms are identical: Java serialization and ORM persistence are separate mechanisms.

## COLL-032 — What is the difference between `final`, `finally` and `finalize()`?

These look similar but serve completely different purposes:

- **`final`** is a keyword used for variables, methods and classes. Depending on where it appears, it restricts reassignment, overriding or inheritance.
- **`finally`** is a block used with `try` / `catch` that is normally executed whether the try block completes normally or throws an exception. It is commonly used to release resources in older code. Prefer **try-with-resources** for `AutoCloseable` resources such as streams and JDBC resources. A finally block is not guaranteed to execute if the JVM or process terminates abruptly, for example through `System.exit` or a crash.
- **`finalize()`** is a legacy method that the garbage-collection/finalization mechanism could call before reclaiming an unreachable object's memory. Execution timing was unpredictable, it could be skipped in some circumstances, and it introduced reliability and security problems. It has been deprecated for removal since Java 9 and is not recommended. Use explicit resource management (`close()` / `AutoCloseable` with try-with-resources), or other appropriate cleanup mechanisms, rather than relying on finalization.

Example of preferred resource management:

```java
try (java.io.BufferedReader reader =
         java.nio.file.Files.newBufferedReader(java.nio.file.Path.of("input.txt"))) {
    System.out.println(reader.readLine());
} // reader is closed automatically
```

**Interview-ready answer:** “Final controls reassignment, overriding or inheritance; finally is a control-flow block for cleanup; finalize was an unreliable GC-related cleanup hook and is deprecated for removal. In modern Java, I prefer try-with-resources and explicit lifecycle management over finalize.”

---

# 8. Rapid Revision Guide

Use this section in the last 5–10 minutes before an interview. The first sentence under each ID is the reminder to say out loud; if the interviewer asks “why?” or “how?”, use the detailed answer above.

| ID | Rapid recall |
|---|---|
| **COLL-001** | JCF = standard interfaces, implementations and algorithms. `List`, `Set`, `Queue` extend `Collection`; `Map` is part of the framework but does **not** extend `Collection`. |
| **COLL-002** | HashMap = bucket array + hash + equality check; average `get` / `put` O(1) with good distribution; not thread-safe. |
| **COLL-003** | Collision = multiple keys map to one bucket; list in small bins, tree bin in sufficiently large bins; `equals()` distinguishes keys. |
| **COLL-004** | Common OpenJDK thresholds: treeify around 8 nodes, capacity at least 64; below that, resize may be preferred. Implementation detail, not Map API guarantee. |
| **COLL-005** | Default load factor 0.75; resize threshold ≈ capacity × load factor. Lower = more memory/fewer collisions; higher = less table space/more collisions. |
| **COLL-006** | Resize normally doubles capacity and redistributes nodes; may be costly. Good initial sizing can reduce resizes. |
| **COLL-007** | HashMap supports one null key and multiple null values; a second null-key insertion replaces the existing mapping. |
| **COLL-008** | HashMap: non-concurrent. ConcurrentHashMap: concurrent-safe operations, atomic compound methods, no nulls, weakly consistent iterators. |
| **COLL-009** | ConcurrentHashMap disallows null to keep `get() == null` unambiguously meaning “no mapping.” |
| **COLL-010** | Modern CHM uses volatile access, CAS, bin-level synchronization and cooperative resize—not one global lock for every operation. |
| **COLL-011** | Java 7 CHM = segments; Java 8+ = bucket-oriented design with CAS/bin locking/tree bins/cooperative resize. |
| **COLL-012** | HashMap = not thread-safe; Hashtable = legacy synchronized map; ConcurrentHashMap = modern concurrent map optimized for concurrent access. |
| **COLL-013** | ArrayList: O(1) indexed read, compact, shifts on middle insert/delete. LinkedList: O(n) indexed access, node overhead; relinking is O(1) once positioned. |
| **COLL-014** | Prefer LinkedList only when its deque/iterator-positioned insertion behavior helps; ArrayList is often the better default; consider ArrayDeque for a deque. |
| **COLL-015** | ArrayList is unsynchronized; Vector is legacy with synchronized individual methods. Method-level synchronization does not make a multi-step sequence atomic. |
| **COLL-016** | HashSet = no order guarantee; LinkedHashSet = insertion order; TreeSet = sorted order, usually O(log n). |
| **COLL-017** | HashMap = no guaranteed order; LinkedHashMap = insertion/access order; TreeMap = sorted keys and range operations. |
| **COLL-018** | TreeSet is backed by TreeMap; uses natural order/comparator; `compare(a,b)==0` means duplicate for set purposes. |
| **COLL-019** | List is an interface; LinkedList is one concrete implementation and also implements Deque. |
| **COLL-020** | `==` on references means same object; `equals()` means logical equality if overridden. String compares content; arrays need `Arrays.equals`. |
| **COLL-021** | Override `hashCode()` whenever overriding `equals()`, so equal objects have equal hashes and hash collections can find them. |
| **COLL-022** | `equals`: reflexive, symmetric, transitive, consistent, non-null. Equal ⇒ same hash; same hash ⇏ equal. |
| **COLL-023** | HashMap uses hash to locate the bucket and identity/`equals()` to find the exact key. Keep keys' equality/hash fields stable. |
| **COLL-024** | Same hash means collision, not equality. Unequal objects with the same hash can both be keys in HashMap. |
| **COLL-025** | Equal objects with different hashes violate the contract; lookups may fail and hash collections may behave incorrectly. |
| **COLL-026** | Immutable class: private final fields, constructor-only initialization, no setters, validation, defensive copies, immutable/restricted object graph. |
| **COLL-027** | `final` class blocks subclassing that could weaken guarantees, but `final` alone does not make objects immutable. |
| **COLL-028** | Defensive copies on input and output prevent callers from mutating internal state; watch for shallow copies of mutable elements. |
| **COLL-029** | `final` variable = assign once; final method = cannot override; final class = cannot subclass. Final reference does not freeze the object. |
| **COLL-030** | Java `transient` excludes a field from default Java serialization; it does not mean the field is unusable at runtime. |
| **COLL-031** | Java `transient` concerns Java serialization; JPA `@Transient` concerns ORM persistence mapping. Different mechanisms. |
| **COLL-032** | `final` = modifier; `finally` = cleanup/control-flow block; `finalize()` = legacy GC hook, deprecated for removal. Prefer try-with-resources. |

## Five high-value interview traps

1. **“Same hash code means equal objects.”** Incorrect. Different objects can share a hash code; `equals()` determines logical equality.
2. **“A final object is immutable.”** Incorrect. A final reference cannot be reassigned; its referenced object may still change.
3. **“LinkedList insertion is always O(1).”** Incomplete. Finding an arbitrary index is O(n); relinking is O(1) once positioned.
4. **“ConcurrentHashMap makes every multi-call workflow atomic.”** Incorrect. Use atomic map methods or appropriate synchronization for compound workflows.
5. **“Use finalize to release resources.”** Outdated. Use try-with-resources / `AutoCloseable` and explicit lifecycle management.

## A 30-second closing summary

“The main thing I focus on with collections is choosing the right structure for ordering, uniqueness, performance and concurrency. For hash-based collections, the `equals()`/`hashCode()` contract is critical. For immutable classes, I protect the full object state—not just the references—using private final fields and defensive copies. And for concurrency, I use concurrent collections and their atomic methods rather than assuming ordinary collections or a sequence of calls is thread-safe.”

---

# Official references for verification

These notes describe common Java SE / OpenJDK behavior. Exact implementation details can differ across Java versions, so use API specifications for contracts and implementation source / release documentation for details such as HashMap treeification thresholds.

- [Java SE 21 — HashMap API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/HashMap.html)
- [Java SE 21 — ConcurrentHashMap API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)
- [Java SE 21 — Java Collections Framework overview](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/doc-files/coll-reference.html)
- [Java SE 21 — Object API (`equals`, `hashCode`, `finalize`)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html)
- [Java SE 21 — RandomAccess marker interface](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/RandomAccess.html)
