
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;

class Main {
    public static void main(String[] args) {

        int num[]={1,2,2,3,3,4,4,5,5,5,5,6,76};
        List<Integer>num=Arrays.asList(1,1,1,2,3,3,3,4,4,5,5,6,6,6,6,6);
 Map<Integer,Long>map=num.stream().collect(
    Collectors.groupingBy(Function.identity(),Collectors.counting()));
    System.out.println(map);

        
        System.out.println("Try clicking the Run button.");
    }
}



//functional interfaces in one code
// Online Java Compiler (Editor)
// Write and run Java online using this edit
import java.util.*;
import java.util.function.*;
class Main {
    public static void main(String[] args) {
        System.out.println("Try clicking the Run button.");
        Predicate<Integer>isEven=n->n%2==0;//filter()
        Function<String,Integer>length=s->s.length();//map()
        Consumer<String>print=System.out::println;//forEach()
        Supplier<String>supplier=()->"Java";//Stream.generate()
        Optional<String>name=Optional.ofNullable(null);
        System.out.println(length.apply("java"));
        System.out.println(isEven.test(14));
        print.accept("java");
        System.out.println(supplier.get());
        System.out.println(name.orElse("def_Value"));
        System.out.println(name.get());
        System.out.println(name.orElseThrow(()->new RuntimeException("user not presetnt")));
    }
}