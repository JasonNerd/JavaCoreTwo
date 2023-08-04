package utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 一个输入输出流的工具类
 */
public class DataIO {
    /**
     * 向输出流 out 写入最多 size 个字符, 这些字符来自 s
     * 若长度不足, 则填充 0
     */
    public static void writeFixedString(DataOutput out, String s, int size) throws IOException {
        for(int i=0; i<size; i++){
            char c = 0;
            if(i<s.length()) c = s.charAt(i);
            out.writeChar(c);
        }
    }

    /**
     * 从读入流中读取最多 size 个字符, 当提前结束(遇到0)时
     * 会跳过多余的0
     */
    public static String readFixedString(DataInput in, int size) throws IOException {
        StringBuilder builder = new StringBuilder();
        int i;
        for(i=0; i<size; i++){
            char c = in.readChar();
            if(c != 0) builder.append(c);
        }
        return builder.toString();
    }
}
