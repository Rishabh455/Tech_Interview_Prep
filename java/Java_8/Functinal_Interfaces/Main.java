
import java.util.concurrent.*;

public class Main {

    static int doWork() {
        return 30; // Our actual task
    }

    public static void main(String[] args) throws Exception {

        // 1. RUNNABLE: Task executes, but cannot return a value
        Runnable r = () -> {
            doWork(); // Result is discarded
            System.out.println("Runnable: No return value");
        };
        r.run();

        // 2. CALLABLE: Task can return a value
        Callable<Integer> task = () -> doWork();

        // Direct call: synchronous, no Future required
        int result = task.call();
        System.out.println("Callable.call(): " + result);

        // 3. EXECUTORSERVICE + FUTURE: Run asynchronously
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<Integer> future = executor.submit(task);

            // get() retrieves the result; waits if not ready
            System.out.println("Future.get(): " + future.get());

            // 4. COMPLETABLEFUTURE: Async execution + chaining
            CompletableFuture<Integer> cf =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return task.call();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }, executor);

            // Transform the result: 30 * 2 = 60
            System.out.println(
                "CompletableFuture: " +
                cf.thenApply(n -> n * 2).join()
            );

        } finally {
            executor.shutdown();
        }
    }
}
