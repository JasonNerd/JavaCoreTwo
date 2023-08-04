package inputAndOutput.iostream;

import pojo.Employee;
import utils.DataIO;

import java.io.*;

import static inputAndOutput.iostream.TextRW.getStaff;

/**
 * random access file
 * 将 Employee 对象以等长记录的形式写入文件
 * 然后通过 seek 将文件指针指向第i条记录, 实现随机读写
 * 可以看出, Employee 对象的变长字段在于 name 字符串
 */
public class RAFileIO {
    static String filePath = "txt/rafEmp";
    static int NAME_SIZE = 20;  // name: 20 bytes
    // salary: 8 bytes, date: 3x4=12 bytes
    public static void empWriteRAF(DataOutput out, Employee emp) throws IOException {
        DataIO.writeFixedString(out, emp.getName(), NAME_SIZE);
        out.writeDouble(emp.getSalary());
        out.writeInt(emp.getYear());
        out.writeInt(emp.getMonth());
        out.writeInt(emp.getDay());
    }
    public static Employee empReadRAF(DataInput in) throws IOException {
        String name = DataIO.readFixedString(in, NAME_SIZE);
        double salary = in.readDouble();
        int year = in.readInt();
        int month = in.readInt();
        int day = in.readInt();
        return new Employee(name, salary, year, month, day);
    }

    public static void main(String[] args) {
        // 1. generate an emp list
        Employee[] staff = getStaff();
        // 2. write Employee[] to file
        try{
            DataOutput out = new DataOutputStream(new FileOutputStream(filePath));
            for(Employee e: staff)
                empWriteRAF(out, e);
        }catch (IOException e){
            System.out.println("Error occurred");
        }
    }
}
