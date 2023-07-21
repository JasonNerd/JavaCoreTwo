package streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CountWord {
    // 读取 txtPath 指示的 txt 文件, 将其保存为静态 List
    public static String[] txt2List(String txtPathStr) throws IOException {
        Path txtPath = Paths.get(txtPathStr);
        String contents = Files.readString(txtPath);    // may throw IOException
        return contents.split("\\PL+");    // 以非字母作为分隔符
    }

    public static long countWordsFor(List<String> wordList, int len){
        long res = 0;
        for(String w: wordList){
            if(w.length() > len) res++;
        }
        return res;
    }

    public static long countWordsStream(List<String> wordList, int len){
        return wordList.stream().filter(w->w.length()>len).count();
    }

    public static long countWordsParallelStream(List<String> wordList, int len){
        return wordList.parallelStream().filter(w->w.length()>len).count();
    }

    public static void test(){
        String txtPathStr = "txt/textA.txt";
        int len = 10;
        try {
            List<String> wordList = List.of(txt2List(txtPathStr));
            System.out.println("countWordsFor: "+countWordsFor(wordList, len));
            System.out.println("countWordsStream: "+countWordsStream(wordList, len));
            System.out.println("countWordsParallelStream: "+countWordsParallelStream(wordList, len));
        }catch (IOException io){
            io.printStackTrace();
        }
    }
    public static void main(String[] args) {
         test();
    }
}