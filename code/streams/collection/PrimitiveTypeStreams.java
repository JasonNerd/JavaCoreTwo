package streams.collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static streams.StreamTransfer.wordStream;

/**
 * 基本类型流
 * IntStream / DoubleStream / ...
 */
public class PrimitiveTypeStreams {
    // 1. show the int stream
    public static void show(String label, IntStream stream){
        final int SIZE = 10;
        int[] integers = stream.limit(SIZE).toArray();
        System.out.println(label);
        System.out.print("\t");
        for(int i=0; i<integers.length; i++){
            System.out.print(integers[i]);
            if(i<integers.length-1) System.out.print(", ");
            else System.out.println(" ...");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        IntStream stream = IntStream.generate(()->(int)(Math.random()*100));
        show("1. use generate() to get random int stream", stream);
        IntStream stream1 = IntStream.range(2, 10);
        show("2. range(2, 10)", stream1);
        IntStream stream2 = IntStream.rangeClosed(2, 10);
        show("3. rangeClosed(2, 100)", stream2);
        // 4. here we use wordStream() to get a string stream
        show("4. use mapToInt like the len(str)", wordStream().mapToInt(String::length));
        // 5. CharSequence 接口拥有 codePoints 和 chars 方法
        //    可以生成由字符的 Unicode 码或由 UTF-16编码机制的码元构成的IntStream
        String sentence = "5. \uD835\uDD46 is the set of oct onions.";
        System.out.println(sentence);
        IntStream stream3 = sentence.codePoints();
        System.out.println(stream3.mapToObj(s->String.format("%x", s)).collect(Collectors.joining(" ")));
        System.out.println();
        // 6. 可使用 Boxed() 将 IntStream 转为 Integer
        Stream<Integer> stream4 = IntStream.range(2, 100).boxed();
        show("6. `Stream<Integer>` <-> `IntStream`", stream4.mapToInt(Integer::intValue));
    }
}
