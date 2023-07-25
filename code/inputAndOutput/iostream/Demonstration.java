package inputAndOutput.iostream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 本程序给出了第二章输入输出的所有小例子, 它们在书中零散出现
 * 这里给他进行汇总, 另外, 对于一些比较完整的实例, 则单独编写
 * 程序实现, 例如将员工实体列表存储到文本文件以及读取文本文件
 * 恢复员工实体列表
 */
public class Demonstration {
    // 1. InputStream / OutputStream 基本输入输出流, 按字节的方式读写
    // 它是抽象类, 实现了 closable 接口, 因此使用时是使用具体的子类, 例如
    // FileInputStream, System.in
    static String employeeFILE = "txt/employee.txt";
    public static void testInputStream() throws IOException {
        int b = System.in.read();
        System.out.println(b);
        byte[] bytes = new byte[10];    // read no more than 10 bytes
        System.in.read();   // read the enter key input
        System.in.read(bytes);
        // you can see that the enter key also keep in the array
        System.out.println(Arrays.toString(bytes));
        // transferTo 将输入流中的所有字节转移到输出流
        System.in.transferTo(System.out);   // echo
    }

    // 2. FileInputStream, FileOutputStream 提供了磁盘文件的输入输出流
    public static void testFileIO() throws IOException{
        // 1. 获取工作目录
        System.out.println(System.getProperty("user.dir"));
        // 2. 获取文件系统分隔符
        System.out.println(File.separator);
        // 3. 根据1和2编写合适的文件路径, 读取文件作为输入流并转入输出流: echo it
        FileInputStream employeeFIS = new FileInputStream("txt/employee.txt");
        employeeFIS.transferTo(System.out);     // 转到输出流
    }

    // 3. DataInputStream 提供了从输入流读取数字的方法
    public static void testDataIOS() throws IOException {
        // 1. try to read a double from file
        DataInputStream numberStream = new DataInputStream(new FileInputStream("txt/employee.txt"));
        System.out.println(numberStream.readDouble());  // 这里输出的并非文件中任何一个double值
        // 2. maybe use buffer data-block is an efficient way to get data
        DataInputStream numberStream2 = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream("txt/employee.txt")
                )
        );
        System.out.println(numberStream2.readInt());    // 这里输出的并非文件中任何一个 int 值
    }

    // 4. InputStreamWriter / OutputStreamReader 提供了可指定字节编码方式的读写器
    public static void testIOReaderWriter() throws IOException{
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(employeeFILE),
                StandardCharsets.UTF_8
        );
        // 将字节流以字符的形式打印出来
        int i=0;
        while (true){
            int c = reader.read();
            if (c == -1) break;
            ++i;
            System.out.print((char) c);     // c 是字符的编码, 是一个整数
        }
        System.out.println(i);
        reader.close();
    }
    // 5. 可以通过 PrintWriter 简单的向输出流写入任何基本数据类型
    public static void testPrintWriter() throws IOException {
        PrintWriter writer = new PrintWriter("txt/pw.txt", StandardCharsets.UTF_8);
        writer.print("Mike");
        writer.print(' ');
        writer.print(23);
        writer.print(' ');
        writer.println(2312.37);
        writer.close();
    }
    // 6. 可以使用简单的 Scanner 类来读入任意的文本
    // 或者使用 Files.read() 将文本读入为一个字符串, Files.readAllLines() 读入为一行行的字符串
    public static void testScanner() throws IOException{
        Scanner scanner = new Scanner(new FileInputStream(employeeFILE));
        int line = scanner.nextInt();
        System.out.println(line);
        System.out.println(scanner.next());

        String fileStr = Files.readString(Path.of(employeeFILE), StandardCharsets.UTF_8);
        System.out.println(fileStr);

        List<String> allLines = Files.readAllLines(Path.of(employeeFILE), StandardCharsets.UTF_8);
        System.out.println(allLines.get(2));
    }
    public static void main(String[] args) throws IOException {
//        testInputStream();
//        testFileIO();
//        testDataIOS();
//        testIOReaderWriter();
//        testPrintWriter();
        testScanner();
    }
}
