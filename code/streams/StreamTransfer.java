package streams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static streams.StreamCreating.showStream;

public class StreamTransfer {
    public static Stream<String> wordStream(){
        String txtPathStr = "txt/textA.txt";
        try{
            return Stream.of(CountWord.txt2List(txtPathStr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void test(){
        // 1. map: 转为全部大写
        showStream("Uppercase word list", wordStream().map(String::toUpperCase));
        // 2. map: 取首字母
        showStream("First letter", wordStream().map(w-> w.substring(0, 1)));
        // 3. map: 首字母大写
        showStream("Capitalized word list", wordStream().map(w->
                w.substring(0, 1).toUpperCase()+w.substring(1)));
        // 4. flatMap
        showStream("Flat letter stream", wordStream().flatMap(StreamTransfer::codePoints));
        // 5. filter: 取出包含数字的部分
        showStream("take the owl", codePoints("aoef9q83ur9wq8asudc").filter("0123456789"::contains));

    }

    // 将字符串转为字符串的流, 例如 str -> s, t, r
    public static Stream<String> codePoints(String str){
        ArrayList<String> arrayList = new ArrayList<>();
        int i = 0;
        while (i < str.length()){
            int j = str.offsetByCodePoints(i, 1);
            arrayList.add(str.substring(i, j));
            i = j;
        }
        return arrayList.stream();
    }
    public static void main(String[] args) {
        test();
//        System.out.println("0123456789".contains("6"));
//        showStream("codePoints", codePoints("没错是我那么多的冷漠让你感觉到无比的失落"));
    }
}
