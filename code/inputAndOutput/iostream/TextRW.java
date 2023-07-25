package inputAndOutput.iostream;

import pojo.Employee;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * write and read employee list to the file
 */
public class TextRW {
    /**
     * write an employee[] to PrintWriter
     */
    public static void writeEmpArr(Employee[] employees, PrintWriter out){
        out.println(employees.length);
        for(Employee employee: employees)
            out.println(employee.toString());
        out.close();
    }

    /**
     * use a Scanner to read employees from reader
     */
    public static Employee[] readEmpArr(Scanner in){
        int n = Integer.parseInt(in.nextLine());
        Employee[] staff = new Employee[n];
        for(int i=0; i<n; i++){
            String line = in.nextLine();
            staff[i] = new Employee(line);
        }
        in.close();
        return staff;
    }

    public static Employee[] getStaff(){
        Employee[] staff = new Employee[5];
        staff[0] = new Employee("Carl Cracker", 75000.0, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000.0, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000.0, 1990, 3, 15);
        staff[3] = new Employee("Julie Matter", 85000.0, 1992, 2, 29);
        staff[4] = new Employee("Hemmer Crusher", 72000.0, 1985, 10, 23);
        return staff;
    }

    public static void main(String[] args) throws IOException{
        // 1. 将 Employee[] 写到文件: "txt/employee.txt"
        writeEmpArr(getStaff(), new PrintWriter(new FileOutputStream("txt/employee.txt")));
        //    另一种初始化 PrintWriter 的方法
        //    writeEmpArr(getStaff(), new PrintWriter("txt/employee.txt", StandardCharsets.UTF_8));
        // 2. 从文件/标准输入读到 Employee[]
        Employee[] staff = readEmpArr(new Scanner(new FileInputStream("txt/employee.txt")));
        //     将 Employee[] 写到标准输出: 也即打印对象的内容到屏幕上
        writeEmpArr(staff, new PrintWriter(System.out));
    }
}
