package optional;

import streams.CountWord;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static streams.StreamCreating.showStream;

public class OpUsing {
    public static List<String> getWordList(){
        String txtPathStr = "txt/textA.txt";
        try{
            return List.of(CountWord.txt2List(txtPathStr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static List<String> wordList = getWordList();
    public static void main(String[] args) {
        System.out.println("1. 寻找第一个包含 ant 的单词, 使用包装器 Optional 将结果包装起来");
        Optional<String> optionalValue = wordList.stream()
                .filter(s->s.contains("ant"))
                .findFirst();
        System.out.println(optionalValue.orElse("No word")+" contains ant");

        System.out.println("2. orElse / orElseGet, 后者当Option为空时调用方法产生替代值");
        Optional<String> opt2 = Optional.empty();
        System.out.println(opt2.orElse("NAN"));
        System.out.println(opt2.orElseGet(()-> Locale.getDefault().getDisplayName()));

        System.out.println("3. orElseThrow, Option 为空则抛出异常");
        try{
            String res = opt2.orElseThrow(IllegalStateException::new);
            System.out.println("result: " + res);   // if opt2 is not empty, then result can be got
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        System.out.println("4. ifPresent, 如果值存在则执行操作");
    }
}
