# Java Collections — 10-Minute Interview Revision

## 13. Which collection objects have you mostly used?

### Interview Answer
> "In my Java backend work, I have mostly used `ArrayList`, `HashMap`, and `HashSet`.  
> I used `ArrayList` when I needed an ordered collection of objects, `HashMap` when I needed key-value lookup, and `HashSet` when uniqueness was important.  
> In the banking onboarding workflow, for example, we can use lists for collections of validation results or documents, and maps for mapping identifiers like document type or field name to its corresponding validation result."

### Remember
- **ArrayList → ordered data**
- **HashMap → key-value lookup**
- **HashSet → unique values**

---

## 14. Have you used LinkedList as well?

### Interview Answer
> "Yes, I know and have used `LinkedList`, but compared to `ArrayList`, my usage has been limited. In backend applications, `ArrayList` is generally more common because most operations are reads or indexed access. I would consider `LinkedList` when frequent insertion or deletion at the beginning or end of the list is required."

### Project Context
> "In an onboarding workflow, if I mainly need to iterate over documents, validations, or response objects, I would prefer `ArrayList` rather than `LinkedList`."

### Important
Don't say:
> "LinkedList is always faster for insertion."

Correct answer:
- Insertion/removal is efficient **when the node position is already known**
- Finding the position itself may take **O(n)**

---

# 15. Difference between ArrayList and LinkedList? When do you use each?

| Feature | ArrayList | LinkedList |
|---|---|---|
| Internal structure | Dynamic array | Doubly linked list |
| Random access `get(i)` | **O(1)** | **O(n)** |
| Add at end | Amortized **O(1)** | **O(1)** |
| Add/remove at beginning | **O(n)** | **O(1)** once position/node is known |
| Memory | Lower | Higher because of node pointers |
| Cache locality | Better | Worse |

### Interview Answer
> "`ArrayList` is backed by a dynamic array, so indexed access is O(1). `LinkedList` is implemented as a doubly linked list, so accessing an element by index is O(n).  
> I prefer `ArrayList` when I mainly need iteration, reads, or indexed access. I would choose `LinkedList` when the workload involves frequent insertions or deletions at the ends or around known nodes."

### Project-specific answer
> "For onboarding APIs, document lists, validation results, or API response collections, I would normally prefer `ArrayList` because the common operations are iteration and reading. `LinkedList` would rarely be my first choice."

### Memory Trick
**ArrayList = Array → Fast get**  
**LinkedList = Links → Fast link changes**

---

# 16. Do you know the internal working of ArrayList and LinkedList?

## ArrayList Internal Working

> "`ArrayList` internally uses a resizable array. When the current array becomes full, Java creates a larger array and copies the existing elements into it. That's why adding at the end is generally amortized O(1), while resizing causes an O(n) operation."

### Key point
```text
Array
   ↓
[0][1][2][3][4]
   ↓ full
Create larger array
   ↓
Copy elements
```

### Interview trap
Don't say:
> "Every add is O(n)."

Say:
> "Most end additions are amortized O(1), but resizing can take O(n)."

---

## LinkedList Internal Working

> "`LinkedList` internally consists of nodes. Each node contains the element, a reference to the previous node, and a reference to the next node."

Conceptually:

```text
null ← Node1 ⇄ Node2 ⇄ Node3 → null
```

Each node contains:

```text
prev | data | next
```

### Key point
- `get(index)` → O(n)
- Add/remove at known end/node → efficient
- More memory than ArrayList because of node references

---

# 17. How does HashMap work internally?

### Most Important Question

### Interview Answer
> "`HashMap` stores data as key-value pairs. Internally, it uses a hash table consisting of buckets.  
> When we insert a key-value pair, Java first calculates the key's hash code, processes it to get a hash value, and then calculates the bucket index.  
> The entry is stored in that bucket. During lookup, the hash is calculated again to find the bucket, and then `equals()` is used to identify the exact key."

### Simplified Flow

```text
Key
 ↓
hashCode()
 ↓
hash processing
 ↓
bucket index
 ↓
Bucket
 ↓
equals()
 ↓
Find exact key
 ↓
Value
```

### Java 8+ Collision Structure

If multiple keys land in the same bucket:

```text
Bucket
  ↓
Node → Node → Node
```

For sufficiently large collision chains, Java can convert the structure to a **Red-Black Tree**, improving worst-case lookup performance.

### Important numbers
- Default load factor commonly: **0.75**
- Treeification threshold: **8**
- Treeification also requires sufficient table capacity; commonly **64**
- Resize happens when size crosses the threshold based on capacity × load factor

### Project Example
> "In an onboarding backend, a `HashMap` can be useful when I need fast lookup, for example mapping a document type or field name to its validation result, instead of scanning a list every time."

---

# 18. What is collision in HashMap?

### Interview Answer
> "A collision occurs when two different keys map to the same bucket index in the HashMap."

Example:

```text
Key A ──┐
        ├──> Bucket 5
Key B ──┘
```

Both keys are different, but they end up in the same bucket.

### Important
Collision does **not** mean:
> "`hashCode()` is always equal."

It means:
> "The keys eventually map to the same bucket."

Two different hash codes can still result in the same bucket index.

---

# 19. How do you handle collision in HashMap?

### Interview Answer
> "HashMap handles collisions by storing multiple entries in the same bucket. In older/simple implementations this can be a linked structure. In Java 8 and later, if a bucket becomes sufficiently large and the table has enough capacity, the bucket can be converted into a Red-Black Tree."

### Then say:
> "During lookup, HashMap first identifies the bucket using the hash and then compares keys using `equals()` to find the exact entry."

### Flow

```text
Hash collision
     ↓
Same bucket
     ↓
Multiple entries
     ↓
Linked nodes
     ↓
If chain becomes large
     ↓
Red-Black Tree
```

### Interview Trap
Don't say:
> "HashMap completely avoids collisions."

Correct:
> "HashMap handles collisions."

---

# 20. Do you know equals() and hashCode() contract?

### Most Important Rule

> "Yes. The contract says that if two objects are equal according to `equals()`, they must return the same `hashCode()`."

Formally:

```text
a.equals(b) == true
        ↓
a.hashCode() == b.hashCode()
```

### But the reverse is NOT mandatory

```text
a.hashCode() == b.hashCode()
        ❌ does not guarantee
a.equals(b) == true
```

Because different objects can have the same hash value — that's a collision.

### Example

```java
class Customer {
    private String customerId;

    @Override
    public boolean equals(Object obj) {
        // compare customerId
    }

    @Override
    public int hashCode() {
        return customerId.hashCode();
    }
}
```

If `customerId` represents business identity, both methods should be consistent with that identity.

### Why is this important for HashMap?

Because HashMap effectively works like:

```text
hashCode() → find bucket
equals()   → find exact key
```

If you override `equals()` but not `hashCode()`, you can get incorrect behavior with `HashMap` / `HashSet`.

---

# 30-SECOND MASTER RECAP

```text
ArrayList
→ Dynamic array
→ Fast get(index)
→ Good for reads/iteration
→ Most common for API collections

LinkedList
→ Doubly linked list
→ Slow random access
→ Useful for frequent insert/delete at known positions/end

HashMap
→ Key-value structure
→ hashCode() → bucket
→ equals() → exact key
→ Collision handled inside bucket
→ Large collision chain can become Red-Black Tree

Collision
→ Different keys map to same bucket

equals/hashCode
→ equals true => same hashCode
→ same hashCode does NOT mean equals true
```

# Saudi Banking Onboarding — One Combined Answer

If interviewer asks:

**"Where have you used collections in your project?"**

Say:

> "In the banking onboarding backend, I mainly worked with collections such as ArrayList, HashMap, and HashSet. I used ArrayList for ordered collections like validation results or document-related data, HashMap for fast key-value lookups such as mapping field or document types to processing results, and HashSet where uniqueness was required. I generally prefer ArrayList for most API-layer collection handling because random access and iteration are efficient. I understand the internal working as well — ArrayList uses a resizable array, while HashMap uses hashing and buckets, with equals and hashCode working together to identify keys."

# 10-Minute Priority

### MUST KNOW
1. ArrayList vs LinkedList
2. ArrayList internal working
3. HashMap internal working
4. HashMap collision
5. Collision handling
6. equals/hashCode contract

### ONE-LINE MEMORY

**"List stores, Map looks up, Hash finds bucket, equals finds exact key."**