तुम्हारे Infosys Java Developer techno-managerial round के लिए JUnit + Mockito का 2–3 मिनट का revision content दे रहा हूँ। इसे याद कर लो, ताकि interviewer पूछे कि unit testing कैसे करते हो, तो confidently answer दे सको।

## 1. JUnit + Mockito — Interview Answer

Q. How do you perform unit testing in Java?

“I use JUnit to write and execute unit tests for my Java methods and business logic. JUnit helps verify whether the actual output matches the expected output. I use assertions like `assertEquals()`, `assertTrue()` and `assertThrows()` to validate results.

For Spring Boot applications, I test service-layer methods independently. When a service depends on a repository or another service, I use Mockito to mock those dependencies, so that I can test the business logic without connecting to the actual database or external service.

I commonly use `@Test`, `@BeforeEach`, `@Mock`, `@InjectMocks`, and `when().thenReturn()` for writing tests. I also use `verify()` to check whether a dependency method was called.

After writing unit tests, I execute them through the IDE or Maven as part of the development and CI/CD process.”

## 2. Unit test कैसे लिखते हैं? छोटा example

मान लो तुम्हारे Spring Boot project में एक service है:

```
public int add(int a, int b) {
    return a + b;
}
```

JUnit test:

```
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testAdd() {
        Calculator calculator = new Calculator();

        int result = calculator.add(10, 20);

        assertEquals(30, result);
    }
}
```

याद रखने का तरीका:

- `@Test` — method को test case बनाता है।
- `assertEquals(30, result)` — expected और actual result compare करता है।
- `@BeforeEach` — हर test से पहले common setup करता है।
- `assertThrows()` — exception आने की जाँच करता है।

## 3. Mockito कैसे use करते हैं?

मान लो `UserService`, `UserRepository` पर depend करती है। Database access किए बिना service test करनी है।

```
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUser() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(new User(1L, "Rahul")));

        User user = userService.getUser(1L);

        assertEquals("Rahul", user.getName());
        verify(userRepository).findById(1L);
    }
}
```

- `@Mock` — fake dependency बनाता है।
- `@InjectMocks` — mock dependencies को service में inject करता है।
- `when().thenReturn()` — mock का expected behaviour define करता है।
- `verify()` — check करता है कि method call हुआ या नहीं।

## 4. ये 5 cross-questions ज़रूर याद करना

| Interview question                       | Short answer                                                                                          |
| ---------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| JUnit और Mockito में क्या अंतर है?       | JUnit tests execute और assertions करता है; Mockito dependencies mock करता है।                         |
| Unit testing क्या है?                    | किसी छोटे unit, जैसे method या class, को independently test करना।                                     |
| Unit test और integration test में अंतर?  | Unit test isolated logic जाँचता है; integration test multiple components के interaction को जाँचता है। |
| `assertEquals()` और `verify()` में अंतर? | `assertEquals()` values compare करता है; `verify()` method invocation check करता है।                  |
| JUnit 4 और JUnit 5 में एक अंतर?          | JUnit 4 में `@Test` और `@Before` हैं; JUnit 5 में `@Test` और `@BeforeEach` इस्तेमाल होते हैं।         |

एक आखिरी बात: JUnit और Mockito को confuse मत करना। JUnit testing framework है और Mockito mocking framework। अगर तुमने अपने project में वास्तव में tests लिखे हैं, तो एक real example तैयार रखना—कौन-सी service test की, कौन-सी dependency mock की और कौन-सा result verify किया।