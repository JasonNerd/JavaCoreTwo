package streams;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

public class StreamCreating {
    public static <T> void showStream(String title, Stream<T> streamT, int size){
        List<T> res = streamT.limit(size).toList();
        System.out.print(title+": ");
        for (T re : res){
            System.out.print(re);
            System.out.print(", ");
        }
        System.out.println("... ...");
    }
    public static <T> void showStream(String title, Stream<T> streamT){
        showStream(title, streamT, 10);
    }

    public static void streamGenTest(){
        // 1. of 方法
        try {
            List<String> wordList = List.of(CountWord.txt2List("txtPathStr"));
            showStream("wordList", Stream.of(wordList));
        } catch (IOException e) {}
        Stream<String> songs = Stream.of("Star", "Monk", "Rock", "Pop");
        showStream("songs", songs);
        showStream("empty", Stream.empty());
        // 2. generate
        Stream<String> echos = Stream.generate(()->"echo");
        showStream("echos", echos);
        Stream<Double> doubleStream = Stream.generate(Math::random);
        showStream("doubleStream", doubleStream, 12);
        // 3. iterate
        Stream<BigInteger> integerStream = Stream.iterate(BigInteger.ONE,
                n->n.compareTo(BigInteger.valueOf(1000000)) < 0,
                n->n.add(BigInteger.ONE));
        showStream("integerStream", integerStream);
        // ... more info, look up to the book

    }
    public static void main(String[] args) {
        streamGenTest();
    }
}
