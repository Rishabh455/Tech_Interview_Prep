# Java Interview Revision Notes — Today's Chat

## 1. Functional interface extending another functional interface

A **functional interface** has exactly one distinct abstract method. It can extend another functional interface as long as the child interface still has exactly one distinct abstract method.

### Case 1: Child inherits the parent's only abstract method — valid

```java
@FunctionalInterface
interface A {
    void show();
}

@FunctionalInterface
interface B extends A {
}

public class Main {
    public static void main(String[] args) {
        B b = () -> System.out.println("Hello");
        b.show();
    }
}
```

`B` inherits `show()` from `A`, so it still has one abstract method. The lambda is valid.

### Case 2: Child adds a different abstract method — not a functional interface

```java
interface A {
    void show();
}

interface B extends A {
    void display();
}
```

`B` now has two distinct abstract methods (`show()` and `display()`), so it cannot be used with a lambda. Adding `@FunctionalInterface` to `B` would cause a compilation error.

### Case 3: Child overrides the same abstract method — still valid

```java
@FunctionalInterface
interface A {
    void show();
}

@FunctionalInterface
interface B extends A {
    @Override
    void show();
}
```

The overridden method has the same signature, so it counts as one distinct abstract method.

**Interview rule:** A functional interface may extend one or more interfaces if the combined inherited abstract methods still amount to exactly one distinct method signature (with compatible return types).

---

## 2. Java `Iterator`

An `Iterator` traverses a collection one element at a time, moving forward.

### Main methods

| Method | What it does |
|---|---|
| `hasNext()` | Checks whether another element exists; does not advance the iterator |
| `next()` | Returns the next element and advances the iterator |
| `remove()` | Removes the last element returned by `next()` |
| `forEachRemaining(action)` | Performs an action on all remaining elements; available since Java 8 |

### Example

```java
List<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30));
Iterator<Integer> it = list.iterator();

System.out.println(it.hasNext()); // true
System.out.println(it.next());    // 10
System.out.println(it.next());    // 20
it.remove();                      // removes 20
System.out.println(it.next());    // 30
System.out.println(list);         // [10, 30]
```

### Where does the iterator start?

Conceptually, it starts **before the first element**. The first call to `next()` returns the first element.

For `[10, 20, 30]`:
1. `iterator()` — nothing returned yet.
2. `next()` returns `10`.
3. `next()` returns `20`.
4. `remove()` removes `20`, the element most recently returned.
5. The next `next()` returns `30`.

`remove()` does not remove the upcoming element. It removes the element most recently returned by `next()`.

### Important exceptions and rules

- `next()` when no elements remain throws `NoSuchElementException`.
- `remove()` before a valid `next()`, or twice for the same returned element, can throw `IllegalStateException`.
- Prefer `Iterator.remove()` when removing elements during iteration. Directly modifying a typical collection during iteration can cause `ConcurrentModificationException`.
- `Iterator` moves forward only. `ListIterator` also supports backward traversal through `hasPrevious()` and `previous()`.

---

## 3. Iterating through a `Map` with an `Iterator`

A `Map` is not itself a `Collection`, so get an iterator from a map view such as `entrySet()`.

### Recommended: `entrySet()`

```java
Map<Integer, String> map = new HashMap<>();
map.put(1, "Aditya");
map.put(2, "Rahul");
map.put(3, "Amit");

Iterator<Map.Entry<Integer, String>> it =
        map.entrySet().iterator();

while (it.hasNext()) {
    Map.Entry<Integer, String> entry = it.next();
    System.out.println(entry.getKey() + " " + entry.getValue());
}
```

- `entrySet()` provides key-value entries.
- `iterator()` creates an iterator over those entries.
- `next()` returns a `Map.Entry`.
- `getKey()` and `getValue()` retrieve the key and value.

### Removing a map entry safely

```java
Iterator<Map.Entry<Integer, String>> it =
        map.entrySet().iterator();

while (it.hasNext()) {
    Map.Entry<Integer, String> entry = it.next();

    if (entry.getKey() == 2) {
        it.remove();
    }
}
```

This removes the entry with key `2`. Avoid calling `map.remove()` directly inside this iteration; with a regular `HashMap`, that can trigger `ConcurrentModificationException`.

### Other ways to traverse a map

```java
// Keys only
for (Integer key : map.keySet()) {
    System.out.println(key);
}

// Values only
for (String value : map.values()) {
    System.out.println(value);
}

// Both key and value (Java 8+)
map.forEach((key, value) ->
    System.out.println(key + " " + value)
);
```

---

## 4. Java `Map`: core concepts and methods

A `Map` stores **key-value pairs**. Keys are unique; values may repeat.

```java
Map<Integer, String> map = new HashMap<>();
map.put(1, "Aditya");
map.put(2, "Rahul");
map.put(3, "Aditya");

System.out.println(map.get(2)); // Rahul
```

### Important methods

| Method | Purpose |
|---|---|
| `put(key, value)` | Adds an entry or replaces the value for an existing key |
| `get(key)` | Gets the value for a key; returns `null` if no mapping is found (or if mapped to null) |
| `getOrDefault(key, defaultValue)` | Gets the value or the supplied default when the key is not mapped |
| `containsKey(key)` | Checks whether a key is present |
| `containsValue(value)` | Checks whether a value is present |
| `remove(key)` | Removes the entry for a key |
| `putIfAbsent(key, value)` | Adds the value if the key is absent or currently mapped to null |
| `replace(key, value)` | Replaces a value only if the key exists |
| `keySet()` | Returns a set view of the keys |
| `values()` | Returns a collection view of the values |
| `entrySet()` | Returns a set view of key-value entries |
| `size()` | Returns the number of entries |
| `isEmpty()` | Checks whether there are no entries |
| `clear()` | Removes all entries |
| `forEach(action)` | Performs an action for each key-value pair |

### Common implementations

| Implementation | Key behavior |
|---|---|
| `HashMap` | Average O(1) lookup/insert/remove; no guaranteed iteration order |
| `LinkedHashMap` | Maintains insertion order by default; can also use access order |
| `TreeMap` | Sorts keys; basic operations O(log n) |
| `Hashtable` | Legacy synchronized map; does not allow null keys or values |
| `ConcurrentHashMap` | Designed for concurrent access; does not allow null keys or values |

**Interview points**
- `Map` does not extend `Collection`.
- `HashMap` allows one null key and multiple null values.
- `put()` with an existing key replaces that key's value.
- `entrySet()` is a good choice when both key and value are needed.
- `HashMap` is not thread-safe; `ConcurrentHashMap` is designed for concurrent access.

---

## 5. `Queue`: FIFO

A queue commonly follows **First In, First Out (FIFO)**: the first inserted item is the first removed.

```java
Queue<String> queue = new LinkedList<>();
queue.add("first");
queue.add("second");

System.out.println(queue.peek());   // first; views head
System.out.println(queue.poll());   // first; retrieves and removes
System.out.println(queue.remove()); // second; retrieves and removes
System.out.println(queue.isEmpty()); // true
```

### Queue methods

| Method | Purpose | If empty / insertion fails |
|---|---|---|
| `add(e)` | Inserts an element | Throws an exception if insertion fails |
| `offer(e)` | Inserts an element | Returns `false` if insertion fails |
| `peek()` | Views the head without removing it | Returns `null` if empty |
| `element()` | Views the head without removing it | Throws `NoSuchElementException` if empty |
| `poll()` | Retrieves and removes the head | Returns `null` if empty |
| `remove()` | Retrieves and removes the head | Throws `NoSuchElementException` if empty |
| `size()` | Returns the number of elements | — |
| `isEmpty()` | Checks whether the queue is empty | — |

**Memory trick:** `peek()` views; `poll()` removes.

---

## 6. `Deque`: operations at both ends

`Deque` means **double-ended queue**. It supports insertion, inspection, and removal at both the front and rear.

```java
Deque<String> deque = new ArrayDeque<>();

deque.addFirst("A");
deque.addLast("B");

System.out.println(deque.peekFirst()); // A
System.out.println(deque.peekLast());  // B

System.out.println(deque.pollFirst()); // A; removes front
System.out.println(deque.pollLast());  // B; removes rear

System.out.println(deque.isEmpty());   // true
```

### Matching methods at each end

| Front / first | Rear / last |
|---|---|
| `addFirst(e)` | `addLast(e)` |
| `offerFirst(e)` | `offerLast(e)` |
| `peekFirst()` | `peekLast()` |
| `pollFirst()` | `pollLast()` |
| `removeFirst()` | `removeLast()` |
| `getFirst()` | `getLast()` |

- `offerFirst()` / `offerLast()` return `false` if insertion fails.
- `pollFirst()` / `pollLast()` return `null` when empty.
- `removeFirst()` / `removeLast()` and `getFirst()` / `getLast()` throw `NoSuchElementException` when empty.
- `ArrayDeque` does not allow `null` elements.

---

## 7. Using `Deque` as a stack: LIFO

A stack follows **Last In, First Out (LIFO)**: the last item pushed is the first popped.

```java
Deque<String> stack = new ArrayDeque<>();

stack.push("a");
stack.push("b");

System.out.println(stack.peek()); // b; views top
System.out.println(stack.pop());  // b; removes top
System.out.println(stack.pop());  // a; removes top
System.out.println(stack.isEmpty()); // true
```

| Stack method | Equivalent deque method | Meaning |
|---|---|---|
| `push(e)` | `addFirst(e)` | Adds to the top |
| `pop()` | `removeFirst()` | Removes and returns the top |
| `peek()` | `peekFirst()` | Views the top without removing |

**Interview tip:** Prefer `ArrayDeque` over the legacy `Stack` class for typical stack operations.

---

## 8. Quick revision sheet

- **Functional interface inheritance:** valid when the child still has exactly one distinct abstract method.
- **Iterator:** `hasNext()` checks; `next()` fetches and advances; `remove()` removes the last returned item; `forEachRemaining()` processes the rest.
- **Iterator cursor:** conceptually starts before the first element.
- **Map iterator:** use `map.entrySet().iterator()` to access both key and value.
- **Safe removal during iteration:** use `it.remove()`.
- **Queue:** FIFO; `peek()` views the head, `poll()` removes it.
- **Deque:** operations at both ends.
- **Stack using Deque:** LIFO; `push()` adds, `pop()` removes, `peek()` views.
- **`ArrayDeque`:** does not permit null elements.
- **`Map` vs `Collection`:** `Map` is a separate interface, not a subtype of `Collection`.
