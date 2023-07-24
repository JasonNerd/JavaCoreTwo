package streams.collection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static streams.optional.OpUsing.getWordList;

public class ToCollection {
    private static final List<String> wordList = getWordList();
    // 从流中收集结果
    // 1. 一个更优雅的 show() 方法: Collectors.joining()方法
    public static <T> void show(String label, Collection<T> collect){
        System.out.print(label+"-"+collect.getClass().getSimpleName()+": ");
        String colStr = collect.stream().limit(10)
                .map(T::toString).collect(Collectors.joining(", "));
        System.out.println("["+colStr+"]");
    }

    // 2. Stream.iterate 方法
    public static void testIterate(){
        System.out.println("1. 生成一个非负偶数流, 限制 10 个");
        Iterator<Integer> iterator = Stream.iterate(0, n->n+2).limit(10).iterator();
        while (iterator.hasNext())
            System.out.print(iterator.next()+" ");
        System.out.println("\n");

        System.out.println("2. 从流中得到数组, 注意此时仍为Object[], 可以使用show打印它");
        Object[] objects = Stream.iterate(0, n->n+2).limit(10).toArray();
        show("objects", List.of(objects));
        System.out.println("2.1 从流中得到正确类型数组的错误和正确示范");
        System.out.println("---- ---- 不可以先获取再转型, 这对于数组是不可行的");
        try{
            Integer number = (Integer) objects[0];
            System.out.println("The 0th of integer is "+number);
            // but if cast an object[], it's not allowed
            Integer[] integerA = (Integer[]) objects;
            System.out.println(Arrays.toString(integerA));
        }catch (ClassCastException e){
            System.out.println(e);
        }
        System.out.println("---- ---- 但是可以传入 Integer[] 构造器");
        Integer[] integers = Stream.iterate(0, n->n+2).limit(10).toArray(Integer[]::new);
        System.out.println(Arrays.toString(integers));  // this is ok
        System.out.println("\n");
    }

    // 3. Collectors.collect
    public static void testCollect(){
        System.out.println("3.1 collect to a normal HashSet");
        Set<String> stringSet = vowelStream().collect(Collectors.toSet());
        show("Collectors.toSet()", stringSet);
        System.out.println();

        System.out.println("3.2 collect to a TreeSet");
        TreeSet<String> stringTreeSet = vowelStream().collect(Collectors.toCollection(TreeSet::new));
        show("Collectors.toCollection(TreeSet::new)", stringTreeSet);
    }
    public static Stream<String> vowelStream(){
        return wordList.stream().filter(ToCollection::hasVowel);
    }
    public static boolean hasVowel(String s){
        return s.contains("a") || s.contains("e") || s.contains("i") ||
                s.contains("o") || s.contains("u") || s.contains("A") ||
                s.contains("E") || s.contains("I") || s.contains("O") ||
                s.contains("U");
    }
    // 4. statistics
    public static void testIntStatistic(){
        System.out.println("4.1 统计包含元音的单词流中各个单词的长度");
        IntSummaryStatistics statistics = vowelStream().collect(Collectors.summarizingInt(String::length));
        System.out.println("average: " + statistics.getAverage());
        System.out.println("total number: " + statistics.getCount());
        System.out.println("max length: " + statistics.getMax());
        System.out.println();
        System.out.println("4.2 foEach 语法");
        vowelStream().limit(10).forEach(System.out::println);
    }
    public static void main(String[] args) {
        testIterate();
        testCollect();
        testIntStatistic();
    }
}
