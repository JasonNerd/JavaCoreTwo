package inputAndOutput.serializable;

import java.io.*;
import java.time.LocalDate;

/**
 * 为了克隆对象而使用序列化
 * 通常不建议这么做, 因为效率很低
 */
public class SerialClone {
    public static void main(String[] args) throws CloneNotSupportedException {
        Employee harry = new Employee("Harry Hacker", 35000, 1989, 10, 1);
        Employee harry2 = (Employee) harry.clone();
        harry2.raiseSalary(10);
        System.out.println(harry);
        System.out.println(harry2);
    }
}

class SerialCloneable implements Cloneable, Serializable {
    public Object clone() throws CloneNotSupportedException{
        try {
            // write to ByteArrayOutputStream
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(bout);
            o.writeObject(this);
            // read from ByteArrayOutputStream
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bin);
            return in.readObject();
        }catch (IOException | ClassNotFoundException e){
            CloneNotSupportedException e2 = new CloneNotSupportedException();
            e2.initCause(e);
            throw e2;
        }

    }
}

class Employee extends SerialCloneable
{
    private String name;
    private double salary;
    private LocalDate hireDay;

    public Employee(String n, double s, int year, int month, int day)
    {
        name = n;
        salary = s;
        hireDay = LocalDate.of(year, month, day);
    }

    public String getName()
    {
        return name;
    }

    public double getSalary()
    {
        return salary;
    }

    public LocalDate getHireDay()
    {
        return hireDay;
    }

    /**
     Raises the salary of this employee.
     @param byPercent the percentage of the raise
     */
    public void raiseSalary(double byPercent)
    {
        double raise = salary * byPercent / 100;
        salary += raise;
    }

    public String toString()
    {
        return getClass().getName()
                + "[name=" + name
                + ",salary=" + salary
                + ",hireDay=" + hireDay
                + "]";
    }
}
