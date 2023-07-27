package inputAndOutput.serializable;

import pojo.Employee;
import pojo.Manager;

import java.io.*;
import java.util.Arrays;

/**
 * 演示对象序列化的使用
 */
public class Demonstration {
    // 1. 读写简单单个对象
    public static void testSigObjSer() throws IOException, ClassNotFoundException {
        // 写到文件
        Employee mike = new Employee("Mike", 43000.0, 2001, 3, 6);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("txt/emp-ser"));
        out.writeObject(mike);      // 注意 Employee 需要实现 Serializable 接口
        // 从文件读
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("txt/emp-ser"));
        Employee read = (Employee) in.readObject();
        // 打印读出的对象
        System.out.println(read);
        System.out.println(read==mike);     // 创建了一个新的对象, 它相当于原对象的拷贝
    }

    // 2. 读写复杂的多个对象, 假设 Manager 类继承了 Employee 类, 额外包含一个 secretary
    // 字段, 该字段表示经理的秘书, 类型为 Employee. 现有两个经理 a 和 b, 它们共用一位秘书 c
    // 将它们存放在 Employee[3] 中, 尝试 Serializable 是否仍适用
    public static void testMulObjSer() throws IOException, ClassNotFoundException{
        // 先构造这两个经理和一个秘书
        Manager julie = new Manager("Julie Matter|85000.0|1992|2|29");
        Manager hemmer = new Manager("Hemmer Crusher|72000.0|1985|10|23");
        Employee tony = new Employee("Tony Tester|40000.0|1990|3|15");
        julie.setSecretary(tony);
        hemmer.setSecretary(tony);
        Employee[] staff = {julie, hemmer, tony};
        // 再把 staff 存入文件
        String rwF = "txt/staff-ser";
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(rwF));
        out.writeObject(staff);
        // 尝试还原 staff
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(rwF));
        Employee[] rdStaff = (Employee[]) in.readObject();
        for(Employee e: rdStaff)
            System.out.println(e);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        testSigObjSer();
        testMulObjSer();
    }
}
/*
 * testMulObjSer
 * Manager: Julie Matter|85000.0|1992|2|29, his/her secretary is Tony Tester
 * Manager: Hemmer Crusher|72000.0|1985|10|23, his/her secretary is Tony Tester
 * Tony Tester|40000.0|1990|3|15
 *
 */