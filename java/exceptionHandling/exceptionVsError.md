# Exception vs Error in Java — Made Easy

## The 1-Minute Pitch (Simple Version)

> Both `Exception` and `Error` come from the same parent: **`Throwable`**.
>
> **`Exception`** = a *"fixable problem."* It's caused by your code or something outside your app (bad input, missing file, null value). Your program **can catch it and recover**.
>
> **`Error`** = a *"fatal problem."* It's caused by the system or JVM itself (out of memory, stack overflow). You **can't fix it**, so you **shouldn't try to catch it**. Just let it crash.

---

## The Simple Analogy

| | Think of it like... |
|---|---|
| **Exception** | A flat tire 🛞 — annoying, but you can pull over and fix it. |
| **Error** | The engine exploded 💥 — no fixing that on the side of the road. Time to call it quits. |

---

## Quick Comparison Table

| Feature | `Exception` | `Error` |
|---|---|---|
| **What causes it?** | Your code / user input / resources | The system / JVM / memory |
| **Can you recover?** | ✅ Yes | ❌ No |
| **Should you catch it?** | ✅ Yes, handle it | 🚫 Never |
| **What happens if unhandled?** | Program may crash *if unchecked* | Program always crashes |
| **Checked by compiler?** | Some are (Checked), some aren't (Unchecked) | Never checked |
| **Examples** | `IOException`, `NullPointerException` | `OutOfMemoryError`, `StackOverflowError` |

---

## The Family Tree

```
Throwable
├── Exception
│   ├── Checked       → IOException, SQLException  (must handle)
│   └── Unchecked     → NullPointerException, ArithmeticException
└── Error             → OutOfMemoryError, StackOverflowError  (never handle)
```

---

## Golden Rules to Remember

1. **Exception = handle it. Error = don't touch it.**
2. **Checked exceptions** must be caught or declared. **Unchecked** don't have to be.
3. **Errors** are always unchecked — the compiler won't force you, and you shouldn't try.
4. If you see `OutOfMemoryError` or `StackOverflowError`, your app is done — restart it.

---

## One-Liner to Memorize

> **"Exception = recoverable (your fault). Error = fatal (JVM's fault)."**