Both the **for-loop and Stream API are generally O(n) time and O(k) space** for frequency counting, so the main difference is not Big-O but **readability, control, and overhead**.

I prefer **Streams** for declarative data processing like **filter → map → group → sort → collect**, because they reduce boilerplate and improve readability.

I prefer a **for-loop** when I need **complex control flow, break/continue, index-based logic, mutable state, heavy mutation, or performance-critical processing**, because loops have lower abstraction overhead and more direct control.

So my rule is: **Streams for data transformation; loops for algorithms and control flow.**