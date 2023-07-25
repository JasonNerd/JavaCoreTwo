package inputAndOutput.iostream;

import pojo.Employee;

import java.io.*;

import static inputAndOutput.iostream.TextRW.getStaff;
import static inputAndOutput.iostream.TextRW.writeEmpArr;

/**
 * 读写二进制文件, 可以使用 DataInput 接口和 DataOutput 中的众多对于基本数据类型的方法
 * DataInputStream 和 DataOutputStream 分别实现了两个接口, 可以传入输入输出流
 */
public class BinRW {
    /**
     * write an employee[] to DataOutputStream
     */
    public static void writeEmpBin(Employee[] employees, DataOutputStream out) throws IOException {
        int n = employees.length;
        out.writeInt(n);
        for (Employee employee : employees) {
            String empStr = employee.toString();
            out.writeInt(empStr.length());  // 这里还写入了每一个 empStr 的长度
            out.writeChars(empStr);
        }
        out.close();
    }
    /**
     * read an employee[] from DataInputStream
     */
    public static Employee[] readEmpBin(DataInputStream in) throws IOException {
        int n = in.readInt();
        Employee[] staff = new Employee[n];
        // 没有类似 readChars 的方法, 只能读取单字符或者字节数组
        // writeChars 调用了 writeChar 方法, 每个字符按两个字节存放, 因此
        // 如果操作字节数组可能会比较麻烦, 这里采用 StringBuilder.append 方法, 构造出字符串
        for(int i=0; i<n; i++){
            StringBuilder empStr = new StringBuilder();
            int empStrLen = in.readInt();
            for(int j=0; j<empStrLen; j++)
                empStr.append(in.readChar());
            staff[i] = new Employee(empStr.toString());
        }
        in.close();
        return staff;
    }

    public static void main(String[] args) throws IOException {
        // 1. 先测试写入 bin file
        String binFile = "txt/employee";
        Employee[] staff = getStaff();
        writeEmpBin(staff, new DataOutputStream(new FileOutputStream(binFile)));
        // 2. 再测试读出 Employee[]
        Employee[] staff2 = readEmpBin(new DataInputStream(new FileInputStream(binFile)));
        // 3. 打印 staff2, 不妨使用已编写的 PrintWriter 方法
        writeEmpArr(staff2, new PrintWriter(System.out));
    }
}
