package streams.collection;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将流收集到映射
 * Collectors.toMap()
 */
public class ToMap {
    public static Stream<Person> personStream(){
        return Stream.of(new Person("Peter"),
                new Person("Julie"),
                new Person("Mike"),
                new Person("Lobby"));
    }
    // 1. 将 id 和 name 收集到 map
    public static void testA(){
        Map<Integer, String> personMap = personStream().collect(Collectors.toMap(Person::getId, Person::getName));
        System.out.println(personMap);
    }

    // 2. 将 id 和 对象本身 收集到 map
    public static void testB(){
        Map<Integer, Person> personMap = personStream().collect(Collectors.toMap(Person::getId, Function.identity()));
        System.out.println(personMap);
    }

    public static void main(String[] args) {
        testA();
        testB();
    }
}

class Person{
    private static Integer cnt=1011;
    private final Integer id;
    String name;
    public Person(String name) {
        this.id = cnt++;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}