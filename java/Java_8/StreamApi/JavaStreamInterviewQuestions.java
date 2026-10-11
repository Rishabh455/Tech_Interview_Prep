import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Java 8-compatible solutions for 41 Java Stream API interview questions.
 * Compile: javac JavaStreamInterviewQuestions.java
 * Run:     java JavaStreamInterviewQuestions
 *
 * The examples use small in-memory datasets so they can be compiled and run
 * without external libraries or a build tool.
 */
public class JavaStreamInterviewQuestions {
    private static final List<Integer> NUMBERS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    public static void main(String[] args) {
        System.out.println("Q01 stream creation: see q01CreateStreams() output");
        q01CreateStreams();
        show("Q02 even numbers", evenNumbers(NUMBERS));
        show("Q03 squares", squares(Arrays.asList(1, 2, 3, 4, 5)));
        show("Q04 squares of even numbers", squaresOfEvenNumbers(Arrays.asList(1, 2, 3, 4, 5, 6)));
        show("Q05 first number > 10 (encounter order)", firstGreaterThan10(Arrays.asList(4, 12, 11, 20)));
        show("Q06 count > 5", countGreaterThan5(NUMBERS));
        show("Q07 sum", sum(NUMBERS));
        show("Q08 sum of even numbers", sumEvenNumbers(NUMBERS));
        show("Q09 maximum", maximum(NUMBERS));
        show("Q10 sum of squares of even numbers", sumSquaresOfEvenNumbers(NUMBERS));
        show("Q11 distinct", distinctInEncounterOrder(Arrays.asList(3, 2, 3, 1, 2, 4)));
        show("Q12 average", average(Arrays.asList(5, 10, 15, 20, 25)));
        show("Q13 ascending", sortAscending(Arrays.asList(4, 1, 3, 2)));
        show("Q13 descending", sortDescending(Arrays.asList(4, 1, 3, 2)));
        show("Q14 strings starting with a", countStartingWith(Arrays.asList("apple", "avocado", "banana", "apricot"), "a"));
        show("Q15 comma-joined", joinWithComma(Arrays.asList("Java", "Spring", "SQL")));
        show("Q16 all positive", allPositive(Arrays.asList(1, 2, 3)));
        show("Q17 any divisible by 3", anyDivisibleBy3(Arrays.asList(2, 4, 9, 10)));
        show("Q18 flatten", flatten(Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4), Arrays.asList(5, 6))));
        show("Q19 non-empty strings", removeEmptyStrings(Arrays.asList("Java", "", "Stream", "")));
        show("Q20 second-highest distinct", secondHighestDistinct(Arrays.asList(10, 20, 20, 5, 15)));

        List<Employee> employees = sampleEmployees();
        show("Q21 employees by salary ascending", sortEmployeesBySalary(employees));
        show("Q21 employees by salary descending", sortEmployeesBySalaryDescending(employees));
        show("Q22 average age", averageAge(Arrays.asList(new Person("Alice", 25), new Person("Bob", 35), new Person("Cara", 30))));
        show("Q23 partition even/odd", partitionEvenOdd(NUMBERS));
        show("Q24 words by length", groupWordsByLength(Arrays.asList("bat", "ball", "cat", "banana", "dog", "goat")));
        show("Q25 frequencies", frequencies(Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple")));
        show("Q26 average salary by department", averageSalaryByDepartment(employees));
        show("Q27 highest-paid employee by department", highestPaidByDepartment(employees));
        show("Q28 departments with > 2 employees", departmentsWithMoreThanTwoEmployees(employees));
        show("Q29 department with highest average salary", departmentWithHighestAverageSalary(employees));
        show("Q30 most frequent character", mostFrequentCharacter("abracadabra"));
        show("Q31 first non-repeating character", firstNonRepeatingCharacter("swiss"));
        show("Q32 most common employee-name initial", mostCommonNameInitial(employees));
        show("Q33 sliding-window averages", slidingWindowAverages(Arrays.asList(10, 20, 30, 40, 50), 3));
        show("Q34 longest word", longestWord("Java Streams are powerful and expressive."));
        show("Q35 top-three frequency tiers", topThreeFrequentWords("Java is great and Java is fun and Java is powerful"));
        show("Q36 reverse each word", reverseEachWord("Java Streams are fun"));

        List<Transaction> transactions = Arrays.asList(
                new Transaction(LocalDate.of(2026, 10, 1), 120.0),
                new Transaction(LocalDate.of(2026, 10, 1), 80.0),
                new Transaction(LocalDate.of(2026, 10, 2), 300.0),
                new Transaction(LocalDate.of(2026, 10, 2), 50.0),
                new Transaction(LocalDate.of(2026, 10, 3), 200.0));
        show("Q37 day with highest total spend", dayWithHighestSpend(transactions));
        show("Q38 employees grouped by salary range", groupEmployeesBySalaryRange(employees));
        show("Q39 character categories", categorizeCharacters("Java 17!"));
        List<WorkRecord> workRecords = Arrays.asList(
                new WorkRecord("John", "IT"), new WorkRecord("John", "Finance"),
                new WorkRecord("John", "HR"), new WorkRecord("Alice", "IT"),
                new WorkRecord("Alice", "HR"), new WorkRecord("John", "IT")); // duplicate department does not inflate count
        show("Q40 employees in 3+ distinct departments", employeesInAtLeastNDepartments(workRecords, 3));
        show("Q41 bigram frequencies", bigramFrequencies("Java is great, and Java is fun. Java is powerful."));
    }

    private static void q01CreateStreams() {
        List<String> names = Arrays.asList("Alice", "Bob");
        String[] array = {"red", "green", "blue"};
        System.out.println("  List.stream(): " + names.stream().collect(Collectors.toList()));
        System.out.println("  Arrays.stream(): " + Arrays.stream(array).collect(Collectors.toList()));
        System.out.println("  Stream.of(): " + Stream.of(1, 2, 3).collect(Collectors.toList()));
        System.out.println("  Stream.generate() with limit: " + Stream.generate(Math::random).limit(3).collect(Collectors.toList()));
        System.out.println("  Stream.iterate() with limit: " + Stream.iterate(0, n -> n + 1).limit(5).collect(Collectors.toList()));
    }

    // Q02
    public static List<Integer> evenNumbers(List<Integer> numbers) {
        return numbers.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
    }

    // Q03
    public static List<Integer> squares(List<Integer> numbers) {
        return numbers.stream().map(n -> n * n).collect(Collectors.toList());
    }

    // Q04
    public static List<Integer> squaresOfEvenNumbers(List<Integer> numbers) {
        return numbers.stream().filter(n -> n % 2 == 0).map(n -> n * n).collect(Collectors.toList());
    }

    // Q05: first means first in encounter order. Use min() if the requirement means smallest number > 10.
    public static Optional<Integer> firstGreaterThan10(List<Integer> numbers) {
        return numbers.stream().filter(n -> n > 10).findFirst();
    }

    // Q06
    public static long countGreaterThan5(List<Integer> numbers) {
        return numbers.stream().filter(n -> n > 5).count();
    }

    // Q07
    public static int sum(List<Integer> numbers) {
        return numbers.stream().reduce(0, Integer::sum);
    }

    // Q08
    public static int sumEvenNumbers(List<Integer> numbers) {
        return numbers.stream().filter(n -> n % 2 == 0).mapToInt(Integer::intValue).sum();
    }

    // Q09
    public static Optional<Integer> maximum(List<Integer> numbers) {
        return numbers.stream().max(Integer::compareTo);
    }

    // Q10
    public static int sumSquaresOfEvenNumbers(List<Integer> numbers) {
        return numbers.stream().filter(n -> n % 2 == 0).mapToInt(n -> n * n).sum();
    }

    // Q11: distinct() preserves encounter order for an ordered stream such as a List's stream.
    public static List<Integer> distinctInEncounterOrder(List<Integer> numbers) {
        return numbers.stream().distinct().collect(Collectors.toList());
    }

    // Q12
    public static OptionalDouble average(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).average();
    }

    // Q13
    public static List<Integer> sortAscending(List<Integer> numbers) {
        return numbers.stream().sorted().collect(Collectors.toList());
    }

    public static List<Integer> sortDescending(List<Integer> numbers) {
        return numbers.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    // Q14: prefix matching is case-sensitive; normalize strings first for case-insensitive matching.
    public static long countStartingWith(List<String> strings, String prefix) {
        return strings.stream().filter(Objects::nonNull).filter(s -> s.startsWith(prefix)).count();
    }

    // Q15
    public static String joinWithComma(List<String> strings) {
        return strings.stream().filter(Objects::nonNull).collect(Collectors.joining(","));
    }

    // Q16: positive means strictly greater than zero.
    public static boolean allPositive(List<Integer> numbers) {
        return numbers.stream().allMatch(n -> n > 0);
    }

    // Q17
    public static boolean anyDivisibleBy3(List<Integer> numbers) {
        return numbers.stream().anyMatch(n -> n % 3 == 0);
    }

    // Q18
    public static <T> List<T> flatten(List<List<T>> nested) {
        return nested.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    // Q19: removes empty strings, not whitespace-only strings. Use !s.trim().isEmpty() to remove both.
    public static List<String> removeEmptyStrings(List<String> strings) {
        return strings.stream().filter(Objects::nonNull).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    // Q20: second-highest DISTINCT value. Returns empty if fewer than two distinct values exist.
    public static Optional<Integer> secondHighestDistinct(List<Integer> numbers) {
        return numbers.stream().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst();
    }

    // Q21
    public static List<Employee> sortEmployeesBySalary(List<Employee> employees) {
        return employees.stream().sorted(Comparator.comparingDouble(Employee::getSalary)).collect(Collectors.toList());
    }

    public static List<Employee> sortEmployeesBySalaryDescending(List<Employee> employees) {
        return employees.stream().sorted(Comparator.comparingDouble(Employee::getSalary).reversed()).collect(Collectors.toList());
    }

    // Q22
    public static OptionalDouble averageAge(List<Person> people) {
        return people.stream().mapToInt(Person::getAge).average();
    }

    // Q23: true key = even numbers; false key = odd numbers.
    public static Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> numbers) {
        return numbers.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }

    // Q24
    public static Map<Integer, List<String>> groupWordsByLength(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(String::length, LinkedHashMap::new, Collectors.toList()));
    }

    // Q25: generic frequency map.
    public static <T> Map<T, Long> frequencies(List<T> items) {
        return items.stream().collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
    }

    // Q26
    public static Map<String, Double> averageSalaryByDepartment(List<Employee> employees) {
        return employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new,
                Collectors.averagingDouble(Employee::getSalary)));
    }

    // Q27: the downstream maxBy collector yields Optional<Employee> per department.
    public static Map<String, Optional<Employee>> highestPaidByDepartment(List<Employee> employees) {
        return employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new,
                Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))));
    }

    // Q28
    public static Map<String, Long> departmentsWithMoreThanTwoEmployees(List<Employee> employees) {
        Map<String, Long> counts = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment,
                TreeMap::new, Collectors.counting()));
        return counts.entrySet().stream().filter(e -> e.getValue() > 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    // Q29
    public static Optional<Map.Entry<String, Double>> departmentWithHighestAverageSalary(List<Employee> employees) {
        return averageSalaryByDepartment(employees).entrySet().stream().max(Map.Entry.comparingByValue());
    }

    // Q30: ties are resolved by first encounter order in the input text.
    public static Optional<Character> mostFrequentCharacter(String text) {
        Map<Character, Long> counts = text.chars().mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
        return counts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey).findFirst();
    }

    // Q31: LinkedHashMap retains character encounter order, so findFirst returns the first unique character.
    public static Optional<Character> firstNonRepeatingCharacter(String text) {
        Map<Character, Long> counts = text.chars().mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
        return counts.entrySet().stream().filter(e -> e.getValue() == 1L)
                .map(Map.Entry::getKey).findFirst();
    }

    // Q32: case-insensitive initial counts; ties resolved by first occurrence among employee names.
    public static Optional<Character> mostCommonNameInitial(List<Employee> employees) {
        Map<Character, Long> counts = employees.stream().map(Employee::getName).filter(Objects::nonNull)
                .filter(name -> !name.isEmpty()).map(name -> Character.toLowerCase(name.charAt(0)))
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
        return counts.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey).findFirst();
    }

    // Q33: generalized sliding-window average. Returns an empty list when the window is larger than input.
    public static List<Double> slidingWindowAverages(List<Integer> numbers, int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowSize must be greater than zero");
        }
        return IntStream.range(0, numbers.size() - windowSize + 1)
                .mapToObj(i -> numbers.subList(i, i + windowSize))
                .map(window -> window.stream().mapToInt(Integer::intValue).average().orElse(Double.NaN))
                .collect(Collectors.toList());
    }

    // Q34: punctuation is treated as a separator. Empty/punctuation-only input returns Optional.empty().
    public static Optional<String> longestWord(String sentence) {
        String cleaned = sentence.replaceAll("[^\\p{L}\\p{N}\\s]", " ").trim();
        if (cleaned.isEmpty()) {
            return Optional.empty();
        }
        return Arrays.stream(cleaned.split("\\s+"))
                .max(Comparator.comparingInt(String::length));
    }

    // Q35: returns all words whose counts fall in the top three DISTINCT frequency tiers (ties included).
    // If exactly three words are required, sort the entries and apply limit(3) instead.
    public static List<String> topThreeFrequentWords(String paragraph) {
        String cleaned = paragraph.toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}\\s]", " ").trim();
        if (cleaned.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Long> counts = Arrays.stream(cleaned.split("\\s+"))
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
        List<Long> topTiers = counts.values().stream().distinct().sorted(Comparator.reverseOrder())
                .limit(3).collect(Collectors.toList());
        return counts.entrySet().stream().filter(e -> topTiers.contains(e.getValue()))
                .sorted((a, b) -> {
                    int byCount = Long.compare(b.getValue(), a.getValue());
                    return byCount != 0 ? byCount : a.getKey().compareTo(b.getKey());
                })
                .map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.toList());
    }

    // Q36: reverses each whitespace-delimited token; punctuation attached to a token is reversed too.
    public static String reverseEachWord(String sentence) {
        if (sentence.trim().isEmpty()) {
            return sentence;
        }
        return Arrays.stream(sentence.trim().split("\\s+"))
                .map(word -> new StringBuilder(word).reverse().toString())
                .collect(Collectors.joining(" "));
    }

    // Q37
    public static Optional<Map.Entry<LocalDate, Double>> dayWithHighestSpend(List<Transaction> transactions) {
        Map<LocalDate, Double> totals = transactions.stream().collect(Collectors.groupingBy(Transaction::getDate,
                TreeMap::new, Collectors.summingDouble(Transaction::getAmount)));
        return totals.entrySet().stream().max(Map.Entry.comparingByValue());
    }

    // Q38: example business thresholds: LOW < 50,000; MEDIUM < 80,000; HIGH >= 80,000.
    public static Map<SalaryRange, List<Employee>> groupEmployeesBySalaryRange(List<Employee> employees) {
        return employees.stream().collect(Collectors.groupingBy(e -> salaryRange(e.getSalary()),
                () -> new EnumMap<>(SalaryRange.class), Collectors.toList()));
    }

    private static SalaryRange salaryRange(double salary) {
        if (salary < 50000.0) return SalaryRange.LOW;
        if (salary < 80000.0) return SalaryRange.MEDIUM;
        return SalaryRange.HIGH;
    }

    // Q39: groups UTF-16 char values; this is suitable for common BMP characters, not all supplementary code points.
    public static Map<CharacterCategory, List<Character>> categorizeCharacters(String text) {
        return text.chars().mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(JavaStreamInterviewQuestions::characterCategory,
                        () -> new EnumMap<>(CharacterCategory.class), Collectors.toList()));
    }

    private static CharacterCategory characterCategory(char ch) {
        if (Character.isUpperCase(ch)) return CharacterCategory.UPPERCASE;
        if (Character.isLowerCase(ch)) return CharacterCategory.LOWERCASE;
        return CharacterCategory.OTHER;
    }

    // Q40: counts DISTINCT departments per employee, so duplicate records don't inflate the count.
    public static List<String> employeesInAtLeastNDepartments(List<WorkRecord> records, int minimumDepartments) {
        if (minimumDepartments < 1) {
            throw new IllegalArgumentException("minimumDepartments must be at least 1");
        }
        Map<String, Set<String>> departmentsByEmployee = records.stream().collect(Collectors.groupingBy(
                WorkRecord::getEmployeeName,
                Collectors.mapping(WorkRecord::getDepartment, Collectors.toSet())));
        return departmentsByEmployee.entrySet().stream().filter(e -> e.getValue().size() >= minimumDepartments)
                .map(Map.Entry::getKey).sorted().collect(Collectors.toList());
    }

    // Q41
    public static Map<String, Long> bigramFrequencies(String paragraph) {
        String cleaned = paragraph.toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}\\s]", " ").trim();
        if (cleaned.isEmpty()) {
            return Collections.emptyMap();
        }
        String[] words = cleaned.split("\\s+");
        return IntStream.range(0, words.length - 1).mapToObj(i -> words[i] + " " + words[i + 1])
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
    }

    private static List<Employee> sampleEmployees() {
        return Arrays.asList(
                new Employee("Alice", "HR", 50000, 25),
                new Employee("Aaron", "HR", 55000, 29),
                new Employee("Bob", "Finance", 70000, 32),
                new Employee("Barbara", "Finance", 65000, 31),
                new Employee("Charlie", "IT", 90000, 35),
                new Employee("Cara", "IT", 85000, 28),
                new Employee("Chris", "IT", 80000, 30),
                new Employee("David", "Operations", 45000, 27));
    }

    private static void show(String label, Object value) {
        System.out.println(label + ": " + value);
    }

    public enum SalaryRange { LOW, MEDIUM, HIGH }
    public enum CharacterCategory { UPPERCASE, LOWERCASE, OTHER }

    public static final class Employee {
        private final String name;
        private final String department;
        private final double salary;
        private final int age;

        public Employee(String name, String department, double salary, int age) {
            this.name = name;
            this.department = department;
            this.salary = salary;
            this.age = age;
        }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public double getSalary() { return salary; }
        public int getAge() { return age; }
        @Override public String toString() {
            return name + "(" + department + ", salary=" + salary + ")";
        }
    }

    public static final class Person {
        private final String name;
        private final int age;
        public Person(String name, int age) { this.name = name; this.age = age; }
        public String getName() { return name; }
        public int getAge() { return age; }
        @Override public String toString() { return name + "(" + age + ")"; }
    }

    public static final class Transaction {
        private final LocalDate date;
        private final double amount;
        public Transaction(LocalDate date, double amount) { this.date = date; this.amount = amount; }
        public LocalDate getDate() { return date; }
        public double getAmount() { return amount; }
        @Override public String toString() { return date + " total=" + amount; }
    }

    public static final class WorkRecord {
        private final String employeeName;
        private final String department;
        public WorkRecord(String employeeName, String department) {
            this.employeeName = employeeName;
            this.department = department;
        }
        public String getEmployeeName() { return employeeName; }
        public String getDepartment() { return department; }
        @Override public String toString() { return employeeName + "-" + department; }
    }
}
