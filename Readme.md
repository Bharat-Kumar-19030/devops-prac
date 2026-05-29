# Minimize Cash Flow Settlement

## Overview

This project solves the **Debt Settlement / Minimize Cash Flow Problem**.

Given a list of transactions between people, the goal is to reduce the total number of transactions required to settle all debts while ensuring that everyone receives or pays the correct amount.

Instead of executing every original transaction, the algorithm computes each person's **net balance** and then performs settlements between debtors and creditors.

---

# Problem Statement

Suppose the following transactions occurred:

| From    | To      | Amount |
| ------- | ------- | ------ |
| Alice   | Bob     | 4000   |
| Bob     | Charlie | 2000   |
| Charlie | David   | 1000   |
| David   | Alice   | 500    |

The objective is to find the minimum set of transactions needed to settle all debts.

---

# Key Idea

Rather than tracking every transaction individually:

1. Calculate the **net balance** of every person.
2. People with:

   * Positive balance → Creditors (should receive money)
   * Negative balance → Debtors (should pay money)
3. Repeatedly match:

   * Largest creditor
   * Largest debtor
4. Settle the maximum possible amount between them.
5. Continue until all balances become zero.

---

# Data Structures Used

## 1. HashMap<String, Long>

```java
HashMap<String, Long> balance = new HashMap<>();
```

### Purpose

Stores the net balance of every person.

### Format

```text
Person → Net Amount
```

Example:

```text
Alice   → -3500
Bob     → +2000
Charlie → +1000
David   → +500
```

### Why HashMap?

* O(1) average insertion
* O(1) average lookup
* Efficient balance updates

---

## 2. Person Class

```java
static class Person {
    String name;
    long amount;
}
```

### Purpose

Represents:

* Person's name
* Current balance

Example:

```java
Person("Bob", 2000)
```

Meaning:

```text
Bob should receive ₹2000
```

---

## 3. Priority Queue (Max Heap)

### Creditors Heap

```java
PriorityQueue<Person> creditors
```

Comparator:

```java
(a,b) -> Long.compare(b.amount,a.amount)
```

Stores people with positive balances.

Largest creditor always appears at the top.

Example:

```text
Bob      +2000
Charlie  +1000
David    +500
```

Heap Top:

```text
Bob (+2000)
```

---

## 4. Priority Queue (Min Heap)

### Debtors Heap

```java
PriorityQueue<Person> debtors
```

Comparator:

```java
(a,b) -> Long.compare(a.amount,b.amount)
```

Stores people with negative balances.

Most negative debtor appears first.

Example:

```text
Alice  -3500
```

Heap Top:

```text
Alice (-3500)
```

---

# Algorithm Used

## Greedy Algorithm

### Greedy Choice

Always settle:

* Largest creditor
* Largest debtor

first.

Reason:

This removes at least one person from further consideration in every transaction.

Thus the number of transactions is minimized.

---

# Detailed Workflow

---

## Step 1: Read Input Transactions

Input:

```java
[
 [Alice, Bob, 4000],
 [Bob, Charlie, 2000],
 [Charlie, David, 1000],
 [David, Alice, 500]
]
```

---

## Step 2: Compute Net Balances

Initially:

```text
Alice   0
Bob     0
Charlie 0
David   0
```

---

### Transaction 1

```text
Alice → Bob 4000
```

Update:

```text
Alice = -4000
Bob   = +4000
```

---

### Transaction 2

```text
Bob → Charlie 2000
```

Update:

```text
Bob     = +2000
Charlie = +2000
```

Current balances:

```text
Alice   = -4000
Bob     = +2000
Charlie = +2000
```

---

### Transaction 3

```text
Charlie → David 1000
```

Update:

```text
Charlie = +1000
David   = +1000
```

Balances:

```text
Alice   = -4000
Bob     = +2000
Charlie = +1000
David   = +1000
```

---

### Transaction 4

```text
David → Alice 500
```

Update:

```text
David = +500
Alice = -3500
```

Final balances:

```text
Alice   = -3500
Bob     = +2000
Charlie = +1000
David   = +500
```

---

## Step 3: Build Heaps

### Debtors Heap

```text
Alice (-3500)
```

### Creditors Heap

```text
Bob (+2000)
Charlie (+1000)
David (+500)
```

---

# Settlement Phase

---

## Iteration 1

Extract:

```text
Debtor   = Alice (-3500)
Creditor = Bob (+2000)
```

Settlement:

```java
min(3500,2000)
```

```text
= 2000
```

Transaction:

```text
Alice → Bob 2000
```

Remaining balances:

```text
Alice = -1500
Bob   = 0
```

Bob removed.

Alice reinserted.

---

## Iteration 2

Extract:

```text
Alice (-1500)
Charlie (+1000)
```

Settlement:

```text
1000
```

Transaction:

```text
Alice → Charlie 1000
```

Remaining:

```text
Alice   = -500
Charlie = 0
```

Charlie removed.

Alice reinserted.

---

## Iteration 3

Extract:

```text
Alice (-500)
David (+500)
```

Settlement:

```text
500
```

Transaction:

```text
Alice → David 500
```

Remaining:

```text
Alice = 0
David = 0
```

Both removed.

---

## Heaps Become Empty

Algorithm terminates.

---

# Final Output

```text
Alice → Bob      2000
Alice → Charlie  1000
Alice → David     500
```

Total Transactions:

```text
3
```

---

# Why Does This Work?

The total money entering the system equals the total money leaving it.

Therefore:

```text
Sum of all positive balances
=
Sum of all negative balances
```

Every settlement:

1. Reduces at least one person's balance to zero.
2. Never violates the required payment amount.
3. Preserves the total balance.

Eventually all balances become zero.

---

# Complexity Analysis

Let:

```text
T = Number of transactions
N = Number of unique people
```

---

## Computing Balances

```java
for(each transaction)
```

Time:

```text
O(T)
```

Space:

```text
O(N)
```

---

## Building Priority Queues

Each insertion:

```text
O(log N)
```

Total:

```text
O(N log N)
```

---

## Settlement Phase

Each heap operation:

```text
O(log N)
```

Maximum settlements:

```text
N - 1
```

Total:

```text
O(N log N)
```

---

# Overall Complexity

### Time Complexity

```text
O(T + N log N)
```

Typically written as:

```text
O(N log N)
```

after balances are computed.

---

### Space Complexity

```text
O(N)
```

for:

* HashMap
* Debtor Heap
* Creditor Heap
* Result List

---

# Advantages of This Approach

✔ Efficient for large datasets

✔ Avoids unnecessary transactions

✔ Uses greedy optimization

✔ Easy to implement using heaps

✔ Produces a minimal settlement structure

---

# Sample Output

```text
[Alice, Bob, 2000]
[Alice, Charlie, 1000]
[Alice, David, 500]
```

Meaning:

```text
Alice pays Bob      ₹2000
Alice pays Charlie  ₹1000
Alice pays David    ₹500
```

After these transactions, everyone's balance becomes zero.
