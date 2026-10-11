# Java Interview Revision Notes

**Topics:** Enums (`values()` vs `valueOf()`), marker interfaces, exception hierarchy, try-with-resources, and internal implementation of common Java Collections.

Use this as a quick interview-revision sheet. Complexity values are typical for standard JDK implementations; exact implementation details can vary by Java version.

---

## 1. Enum: `values()` vs `valueOf()`

Example:

```java
enum Day {
    MONDAY, TUESDAY, WEDNESDAY
}
```

### `values()`

- Returns an array containing **all constants** of that enum, in declaration order.
- Return type for this example: `Day[]`.

```java
for (Day day : Day.values()) {
    System.out.println(day);
}
// MONDAY, TUESDAY, WEDNESDAY
```

### `valueOf()`

- Returns **one enum constant** that matches the supplied name exactly.
- It is case-sensitive and does not ignore whitespace.
- An unknown name causes `IllegalArgumentException`; a `null` name causes `NullPointerException`.

```java
Day day = Day.valueOf("MONDAY"); // works
// Day.valueOf("monday");        // IllegalArgumentException
// Day.valueOf("FRIDAY");        // IllegalArgumentException
```

| Method | Purpose | Return type |
|---|---|---|
| `Day.values()` | Get every enum constant | `Day[]` |
| `Day.valueOf("MONDAY")` | Find one constant by exact name | `Day` |

**Memory trick:** `values()` = all constants; `valueOf(name)` = the constant with that name.

---

## 2. Marker Interfaces

A **marker interface** is an interface with no declared methods or fields that marks a class with a particular type/meaning. Java libraries or frameworks can check the type and act accordingly.

```java
interface Auditable {
    // No methods: marker interface
}

class Employee implements Auditable {
}
```

### Common examples

| Interface | What it marks/indicates |
|---|---|
| `Serializable` | Objects of the class can participate in Java serialization, subject to serialization rules |
| `Cloneable` | The class indicates support for the `Object.clone()` mechanism; it does not itself declare `clone()` |
| `RandomAccess` | A list indicates that indexed access is expected to be fast; `ArrayList` implements it |

Example check:

```java
if (obj instanceof Serializable) {
    // The object's type implements Serializable
}
```

### Marker interface vs annotation

| Marker interface | Annotation |
|---|---|
| Is a type that a class implements | Metadata attached to code elements |
| Can be checked with `instanceof` | Often inspected by compiler, tools, or reflection |
| Examples: `Serializable`, `Cloneable` | Examples: `@Override`, `@Deprecated` |

**Interview line:** A marker interface carries type-level information without declaring methods. Not every interface with no methods is necessarily used as a marker, but `Serializable` and `Cloneable` are standard examples.

---

## 3. Java Exception Hierarchy

```text
Object
  └── Throwable
       ├── Error
       │    ├── OutOfMemoryError
       │    └── StackOverflowError
       │
       └── Exception
            ├── RuntimeException  (unchecked)
            │    ├── NullPointerException
            │    ├── ArithmeticException
            │    ├── IndexOutOfBoundsException
            │    ├── ArrayIndexOutOfBoundsException
            │    ├── ClassCastException
            │    ├── IllegalArgumentException
            │    ├── IllegalStateException
            │    └── NumberFormatException
            │
            └── Other exceptions (checked)
                 ├── IOException
                 ├── SQLException
                 ├── ClassNotFoundException
                 └── InterruptedException
```

### What each level means

- **`Throwable`**: Root class for objects that can be thrown and caught.
- **`Error`**: Usually serious JVM/environment problems. Applications normally do not try to recover from these.
- **`Exception`**: Conditions an application may handle.
- **`RuntimeException`**: Parent of common unchecked exceptions, often caused by invalid program state or usage.

### Checked vs unchecked

| Type | Rule | Examples |
|---|---|---|
| Checked exception | Compiler requires handling or declaration | `IOException`, `SQLException`, `ClassNotFoundException`, `InterruptedException` |
| Unchecked exception | A `RuntimeException` subclass; compiler does not require handling/declaration | `NullPointerException`, `ArithmeticException`, `IllegalArgumentException` |
| Error | A subclass of `Error`; not normally handled as an application exception | `OutOfMemoryError`, `StackOverflowError` |

Checked exception example:

```java
void readFile() throws IOException {
    // File-reading code
}
```

Or handle it:

```java
try {
    // File-reading code
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

Unchecked example:

```java
int result = 10 / 0; // ArithmeticException at runtime
```

### Are these classes or interfaces?

`Throwable`, `Error`, `Exception`, `RuntimeException`, `IOException`, and `NullPointerException` are **classes**. `Exception` extends `Throwable`; `RuntimeException` extends `Exception`.

Custom checked exception:

```java
class InvalidOrderException extends Exception {
    InvalidOrderException(String message) {
        super(message);
    }
}
```

Custom unchecked exception:

```java
class InvalidOrderRuntimeException extends RuntimeException {
    InvalidOrderRuntimeException(String message) {
        super(message);
    }
}
```

**Interview line:** `Throwable` has two major branches: `Error` and `Exception`. Exceptions other than `RuntimeException` subclasses are generally checked; `RuntimeException` subclasses are unchecked.

---

## 4. Try-with-resources

**Purpose:** Automatically close resources when the `try` block finishes, whether it finishes normally or because an exception occurs.

A resource must implement **`AutoCloseable`** (or `Closeable`, which extends `AutoCloseable`).

### Without try-with-resources

You commonly need a `finally` block to close a resource manually.

### With try-with-resources

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class ReadFileExample {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
```

`br.close()` is called automatically at the end of the block.

### Key interview points

- Introduced in **Java 7**.
- Resource type must implement `AutoCloseable` (or `Closeable`).
- Multiple resources are allowed; they are closed in **reverse order** of declaration.
- Resources are closed even when the `try` body throws an exception.
- If both the body and `close()` throw, the body exception remains the primary exception and the close exception is generally available as a *suppressed exception* (`getSuppressed()`).
- From Java 9, an effectively final variable can be used as a resource: `try (br) { ... }`.

Interface relationship:

```text
AutoCloseable  (interface)
      ↑
Closeable      (interface)
```

**Interview line:** Try-with-resources makes resource cleanup automatic and safer by calling `close()` on `AutoCloseable` resources.

---

# 5. Java Collections: Big Picture

`Collection` is an interface. `Map` belongs to the Collections Framework but **does not extend `Collection`**.

```text
Iterable (interface)
  └── Collection (interface)
       ├── List
       │    ├── ArrayList
       │    ├── LinkedList
       │    └── Vector
       │         └── Stack
       ├── Set
       │    ├── HashSet
       │    │    └── LinkedHashSet
       │    └── SortedSet / NavigableSet
       │         └── TreeSet
       └── Queue
            ├── PriorityQueue
            └── Deque
                 ├── ArrayDeque
                 └── LinkedList also implements Deque

Map (separate interface)
  ├── HashMap
  │    └── LinkedHashMap
  ├── TreeMap
  ├── Hashtable
  └── ConcurrentHashMap
```

Remember:
- **List:** ordered sequence; duplicates usually allowed.
- **Set:** unique elements.
- **Queue/Deque:** elements arranged for processing at one or both ends.
- **Map:** key-value pairs; keys are unique according to the map's equality/ordering rules.

---

# 6. Internal Implementation of Collection Classes

Complexities below are typical. `O(1) amortized` means most operations are constant time, although an occasional resize costs more.

## 6.1 `ArrayList`

**Internal structure:** Resizable array (`Object[]`).

```text
[10] [20] [30] [40] [  ]
```

- `get(index)` jumps directly to an array position.
- Adding at the end is usually quick. When capacity is exhausted, a larger array is allocated and elements are copied.
- Inserting/removing in the middle shifts later elements.
- The public API does not promise an exact growth factor. A common OpenJDK implementation grows capacity by roughly 1.5x.

| Operation | Typical complexity |
|---|---:|
| `get(index)`, `set(index, value)` | `O(1)` |
| `add(value)` at end | `O(1)` amortized |
| `add(index, value)` | `O(n)` |
| `remove(index)` | `O(n)` |
| `contains(value)` | `O(n)` |

**Interview line:** ArrayList is best when fast indexed access is important and insertions/removals in the middle are less frequent.

## 6.2 `LinkedList`

**Internal structure:** Doubly linked list. Each node stores the element and references to the previous and next nodes.

```text
null <- [prev | A | next] <-> [prev | B | next] <-> [prev | C | next] -> null
```

- It maintains references to the first and last nodes.
- Adding/removing at either end is constant time.
- Getting an index requires walking through nodes, so random access is linear.
- Insertion is `O(1)` once the node/position is already reached, but finding an indexed position takes `O(n)`.

| Operation | Typical complexity |
|---|---:|
| `get(index)` | `O(n)` |
| `addFirst()`, `addLast()` | `O(1)` |
| `removeFirst()`, `removeLast()` | `O(1)` |
| `contains(value)` | `O(n)` |

**Interview line:** LinkedList is a doubly linked list, not an array. Do not claim every insertion is `O(1)` without accounting for the time needed to find the position.

## 6.3 `Vector`

**Internal structure:** Resizable array, similar to ArrayList.

- Its methods are synchronized, unlike ArrayList's methods.
- The traditional default growth behavior doubles the capacity when no custom capacity increment is configured; exact details are implementation-specific.
- Synchronizing individual methods does not automatically make a multi-step operation atomic.

**Interview line:** Vector is a legacy synchronized dynamic-array class; ArrayList is usually preferred when external synchronization is not required.

## 6.4 `Stack`

**Internal structure:** `Stack` extends `Vector`, so it uses Vector's dynamic array.

It follows **LIFO** (Last In, First Out):

```text
push(10), push(20), push(30)
Top -> 30
       20
       10
```

- `push()` adds to the top.
- `pop()` removes and returns the top.
- `peek()` reads the top without removing it.

**Interview line:** Stack is a legacy class. For stack behavior in modern Java, prefer `Deque` with `ArrayDeque` in most cases.

## 6.5 `HashSet`

**Internal structure:** Backed by a `HashMap`.

Conceptually, each set element is stored as a map key with a shared dummy value:

```text
HashSet element A  -> HashMap key A -> PRESENT
HashSet element B  -> HashMap key B -> PRESENT
```

- A map cannot contain duplicate keys, so the set cannot contain duplicate elements.
- Uses `hashCode()` to locate a bucket and `equals()` to determine whether an equal element is already present.
- Does not guarantee iteration order.

| Operation | Typical average complexity |
|---|---:|
| `add()`, `remove()`, `contains()` | `O(1)` |

**Interview line:** HashSet provides uniqueness by storing elements as keys in its backing HashMap.

## 6.6 `LinkedHashSet`

**Internal structure:** Backed by a `LinkedHashMap`, which combines hashing with a linked structure that tracks entry order.

- Disallows duplicates, like HashSet.
- Iteration follows insertion order.
- Usually has average `O(1)` add/remove/contains operations, with some extra memory for ordering links.

**Memory trick:** `HashSet` = uniqueness; `LinkedHashSet` = uniqueness + insertion order.

## 6.7 `TreeSet`

**Internal structure:** Backed by a `TreeMap`, implemented using a **Red-Black Tree**.

- Keeps elements sorted according to their natural ordering or a supplied `Comparator`.
- Uses comparison to determine order and whether elements are equivalent for set purposes.

| Operation | Typical complexity |
|---|---:|
| `add()`, `remove()`, `contains()` | `O(log n)` |

**Memory trick:** HashSet → hashing; LinkedHashSet → hashing + order; TreeSet → sorted tree.

## 6.8 `PriorityQueue`

**Internal structure:** Binary heap stored in a resizable array.

- By default, it is a **min-heap**: the smallest element is at the head.
- `peek()` reads the head; `poll()` removes the head and restores the heap property.
- Only the head is guaranteed to be the smallest (by default). Iterating over a PriorityQueue does **not** produce fully sorted output.
- It is not thread-safe and does not permit `null` elements.

| Operation | Typical complexity |
|---|---:|
| `peek()` | `O(1)` |
| `offer()` / `add()` | `O(log n)` |
| `poll()` | `O(log n)` |
| `contains()` / `remove(Object)` | `O(n)` |

**Interview line:** PriorityQueue uses a binary heap for efficient access to the highest-priority element; it is not a sorted list.

## 6.9 `ArrayDeque`

**Internal structure:** Resizable circular array.

- Tracks the logical front and rear, wrapping around the array as needed.
- Can act as a queue, stack, or double-ended queue.
- Usually offers amortized `O(1)` insertion/removal at either end.
- Does not permit `null` elements and is not thread-safe.

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(10);
stack.push(20);
int top = stack.pop(); // 20
```

**Interview line:** Prefer ArrayDeque over legacy Stack for most single-threaded stack use cases.

---

# 7. Map Implementations

## 7.1 `HashMap` — highest priority for interviews

**Internal structure:** An array of buckets. A bucket contains no entry, a linked chain of nodes, or (under certain conditions) a Red-Black Tree bin.

A node conceptually stores:

```text
hash | key | value | next
```

### How `put(key, value)` works

1. Calculate the key's `hashCode()` and process the hash.
2. Use the hash and table capacity to determine a bucket index.
3. If the bucket is empty, insert a node.
4. If it is occupied, compare hashes and keys (`equals()`) to find an existing key.
5. If an equal key exists, replace its value; otherwise add a new node to that bucket structure.
6. If the number of entries exceeds the resize threshold, the table may grow and entries are redistributed.

### Collision

A **collision** occurs when different keys land in the same bucket. A bucket can contain multiple entries. In common modern OpenJDK implementations, a heavily populated bucket may be treeified, subject to thresholds and table capacity.

### Common OpenJDK implementation facts

- Default initial table capacity: `16` (the table may be allocated lazily).
- Default load factor: `0.75`.
- Common resize threshold: `capacity * loadFactor`, initially `16 * 0.75 = 12` after allocation.
- Treeification threshold: commonly `8` nodes, but treeification also depends on the table being sufficiently large (commonly capacity `64`); otherwise resizing may be preferred.
- These numeric details are implementation details, not universal guarantees of the `Map` API.

### `hashCode()` and `equals()`

- `hashCode()` helps locate a bucket.
- `equals()` helps identify whether a candidate key is equal to the key being searched for.
- If two objects are equal according to `equals()`, they **must** have the same `hashCode()`.
- Incorrect implementations of either method can cause unexpected map/set behavior. Avoid mutating fields used by a key's `hashCode()`/`equals()` while it is stored in a map.

### Complexity and properties

| Operation | Typical average complexity |
|---|---:|
| `put()`, `get()`, `remove()` | `O(1)` |
| Treeified bucket lookup | Typically `O(log n)`, subject to key/comparison details |

- Allows one `null` key and multiple `null` values.
- Is not synchronized/thread-safe for concurrent structural modification.
- Does not guarantee iteration order.

**Interview line:** HashMap is a hash-table implementation that uses `hashCode()` for bucket selection and `equals()` for key equality; collisions are stored in bucket structures.

## 7.2 `LinkedHashMap`

**Internal structure:** Extends HashMap and maintains linked connections between entries to preserve a defined iteration order.

- In insertion-order mode, iteration follows insertion order.
- In access-order mode, entries are ordered by recent access; this is useful for an LRU-cache pattern.
- Typical basic map operations are average `O(1)`.

**Memory trick:** LinkedHashMap = HashMap + ordering links.

## 7.3 `TreeMap`

**Internal structure:** Red-Black Tree.

- Keeps keys sorted by natural ordering or a supplied `Comparator`.
- `get()`, `put()`, `remove()`, and `containsKey()` are `O(log n)`.
- With natural ordering, `null` keys are not accepted. A custom comparator may define different behavior.
- Comparisons should be consistent with `equals()` for the map to obey the usual Map contract properly.

**Interview line:** TreeMap is a sorted map implemented using a self-balancing Red-Black Tree.

## 7.4 `Hashtable`

**Internal structure:** Legacy hash-table implementation with synchronized methods.

- Does not permit `null` keys or values.
- Synchronizes operations at the table/method level, which can reduce concurrency.
- Legacy class; for modern concurrent map use, `ConcurrentHashMap` is generally preferred.

### HashMap vs Hashtable

| Feature | HashMap | Hashtable |
|---|---|---|
| Synchronization | Not synchronized by default | Synchronized methods |
| `null` key | One allowed | Not allowed |
| `null` values | Allowed | Not allowed |
| Status | Common modern choice for non-concurrent use | Legacy |

## 7.5 `ConcurrentHashMap`

**Purpose:** A thread-safe map designed for concurrent access.

In modern Java (Java 8+ implementation), it uses a hash table with techniques including **CAS** and synchronized coordination for particular bins/operations. It does not simply lock the entire map for every operation.

- Does not permit `null` keys or values.
- Supports concurrent reads and updates more effectively than synchronizing every operation on one whole-map lock.
- Iterators are weakly consistent: they can operate while updates occur and do not behave exactly like fail-fast ArrayList iterators.
- Older Java 7 implementations used a segment-based design; do not claim that all modern implementations use segments.

**Interview line:** ConcurrentHashMap provides thread-safe concurrent map operations using finer-grained coordination; modern implementations are not based on the old Java 7 segment design.

---

# 8. Comparison Tables

## ArrayList vs LinkedList

| Feature | ArrayList | LinkedList |
|---|---|---|
| Internal structure | Resizable array | Doubly linked list |
| Indexed `get()` | `O(1)` | `O(n)` |
| Add at end | Amortized `O(1)` | `O(1)` |
| Add/remove at front | Usually `O(n)` due to shifting | `O(1)` |
| Memory overhead | Lower per element | More references per node |
| Prefer when | Frequent reads/index access | Frequent end operations or deque behavior |

## HashSet vs LinkedHashSet vs TreeSet

| Class | Internal structure | Ordering | Typical `add` / `contains` |
|---|---|---|---:|
| HashSet | HashMap | No guaranteed order | Average `O(1)` |
| LinkedHashSet | LinkedHashMap | Insertion order | Average `O(1)` |
| TreeSet | TreeMap / Red-Black Tree | Sorted order | `O(log n)` |

## HashMap vs LinkedHashMap vs TreeMap

| Class | Internal structure | Iteration order | Typical basic operation |
|---|---|---|---:|
| HashMap | Hash table + bucket chains/tree bins | No guaranteed order | Average `O(1)` |
| LinkedHashMap | HashMap + linked entry order | Insertion or access order | Average `O(1)` |
| TreeMap | Red-Black Tree | Sorted keys | `O(log n)` |

## HashMap vs ConcurrentHashMap

| Feature | HashMap | ConcurrentHashMap |
|---|---|---|
| Concurrent access | Needs external coordination for unsafe concurrent structural updates | Designed for concurrent access |
| `null` key/value | Allows a null key and null values | Allows neither |
| Ordering | No guaranteed order | No guaranteed order |
| Typical use | Non-concurrent map | Shared map accessed by multiple threads |

---

# 9. Common Interview Traps

1. **`Map` is not a subtype of `Collection`.** It is a separate interface in the Collections Framework.
2. **ArrayList does not have a fixed capacity.** It grows automatically; its exact growth policy is not guaranteed by the API.
3. **LinkedList insertion is not always `O(1)`.** Finding an indexed position can take `O(n)`.
4. **HashSet does not promise insertion order.** Use LinkedHashSet for insertion order or TreeSet for sorted order.
5. **PriorityQueue iteration is not sorted.** Only the head has the priority guarantee.
6. **HashMap relies on both `hashCode()` and `equals()`.** They solve different parts of key lookup.
7. **HashMap's capacity, load factor and treeification thresholds are implementation details.** State the JDK/OpenJDK context if quoting exact values.
8. **ConcurrentHashMap does not allow null keys or values.** This avoids ambiguity in concurrent retrieval results.
9. **Stack and Vector are legacy synchronized classes.** ArrayDeque is generally preferred for stack/queue behavior when appropriate.
10. **Checked vs unchecked depends on the type hierarchy.** `RuntimeException` subclasses are unchecked; ordinary exception subclasses outside that branch are checked.
11. **Cloneable does not declare a `clone()` method.** It marks the class for the special behavior of `Object.clone()`.
12. **Try-with-resources closes resources in reverse declaration order.** Cleanup happens even if the try body throws.

---

# 10. Last-Minute Memorization Sheet

```text
Enum:
  values()       -> all enum constants as an array
  valueOf(name)  -> one exact-name constant; case-sensitive

Marker interfaces:
  Serializable, Cloneable, RandomAccess

Exception hierarchy:
  Object -> Throwable -> Error / Exception
  Exception -> RuntimeException (unchecked) / other checked exceptions
  Hierarchy entries such as Exception and IOException are classes, not interfaces

Try-with-resources:
  Resource implements AutoCloseable/Closeable
  Automatically calls close(); closes multiple resources in reverse order

Collections:
  ArrayList          -> resizable array
  LinkedList         -> doubly linked list
  Vector             -> synchronized resizable array
  Stack              -> extends Vector; LIFO; prefer ArrayDeque in most cases
  HashSet            -> backed by HashMap
  LinkedHashSet      -> backed by LinkedHashMap; insertion order
  TreeSet            -> backed by TreeMap; Red-Black Tree; sorted
  PriorityQueue      -> binary heap; min-heap by default
  ArrayDeque         -> resizable circular array
  HashMap            -> bucket array + linked nodes/tree bins
  LinkedHashMap      -> HashMap + linked ordering of entries
  TreeMap            -> Red-Black Tree; sorted keys
  Hashtable          -> legacy synchronized hash table; no nulls
  ConcurrentHashMap  -> concurrent hash table; modern implementation uses finer-grained coordination

Typical time complexities:
  ArrayList get(index)          -> O(1)
  ArrayList add(end)             -> O(1) amortized
  ArrayList add/remove(middle)   -> O(n)
  LinkedList get(index)          -> O(n)
  LinkedList add/remove at ends  -> O(1)
  HashMap / HashSet basic ops    -> O(1) average
  TreeMap / TreeSet basic ops    -> O(log n)
  PriorityQueue peek             -> O(1)
  PriorityQueue offer/poll       -> O(log n)
  ArrayDeque end operations      -> O(1) amortized
```

---

# 11. Official References

- [Java SE 25 `Enum`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Enum.html)
- [Java SE 25 `AutoCloseable`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/AutoCloseable.html)
- [Java SE 25 `ArrayList`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/ArrayList.html)
- [Java SE 25 `HashMap`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/HashMap.html)
- [Java SE 25 `HashSet`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/HashSet.html)
- [Java SE 25 `LinkedHashMap`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/LinkedHashMap.html)
- [Java SE 25 `TreeMap`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/TreeMap.html)
- [Java SE 25 `PriorityQueue`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/PriorityQueue.html)
- [Java SE 25 `Serializable`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/Serializable.html)
- [Java SE 25 `Cloneable`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Cloneable.html)

**Revision strategy:** First memorize the last-minute sheet, then practise explaining HashMap `put()`/`get()`, ArrayList resizing, HashSet uniqueness, checked vs unchecked exceptions, and try-with-resources aloud.
