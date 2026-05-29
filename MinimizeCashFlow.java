import java.util.*;
public class MinimizeCashFlow {

    static class Person {
        String name;
        long amount;

        Person(String n, long am) {
            name = n;
            amount = am;
        }
    }

    public static List<List<String>> minimizeCashFlow(List<List<String>> transactions) {

        HashMap<String, Long> balance = new HashMap<>();
        for (List<String> tr : transactions) {
            String from = tr.get(0);
            String to = tr.get(1);
            long amount = Long.parseLong(tr.get(2));
            if (!balance.containsKey(from))
                balance.put(from, 0L);
            if (!balance.containsKey(to))
                balance.put(to, 0L);
            balance.put(from, balance.get(from) - amount);
            balance.put(to, balance.get(to) + amount);
        }
        PriorityQueue<Person> creditors = new PriorityQueue<>((a, b) -> Long.compare(b.amount, a.amount));
        PriorityQueue<Person> debtors = new PriorityQueue<>((a, b) -> Long.compare(a.amount, b.amount));

        for (String person : balance.keySet()) {
            long netAmount = balance.get(person);
            if (netAmount > 0) {
                creditors.add(new Person(person, netAmount));
            }
            else if (netAmount < 0) {
                debtors.add(new Person(person, netAmount));
            }
        }

        List<List<String>> result = new ArrayList<>();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Person creditor = creditors.poll();
            Person debtor = debtors.poll();
            long settleAmount = Math.min(
                    creditor.amount,
                    Math.abs(debtor.amount)
            );
            ArrayList<String> temp = new ArrayList<>();
            temp.add(debtor.name);
            temp.add(creditor.name);
            temp.add(String.valueOf(settleAmount));
            result.add(temp);   

            creditor.amount -= settleAmount;
            debtor.amount += settleAmount;

            if (creditor.amount > 0) {
                creditors.add(creditor);
            }

            if (debtor.amount < 0) {
                debtors.add(debtor);
            }
        }

        return result;
    }

    public static void main(String[] args) {

        List<List<String>> transactions = new ArrayList<>();

        transactions.add(Arrays.asList("Alice", "Bob", "4000"));
        transactions.add(Arrays.asList("Bob", "Charlie", "2000"));
        transactions.add(Arrays.asList("Charlie", "David", "1000"));
        transactions.add(Arrays.asList("David", "Alice", "500"));

        List<List<String>> answer = minimizeCashFlow(transactions);
        for (List<String> tr : answer) {
            System.out.println(tr);
        }
    }
}