package pojo;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
    String name;
    Double salary;
    LocalDate entryDate;

    public Employee(String name, Double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.entryDate = LocalDate.of(year, month, day);
    }

    public int getYear(){
        return entryDate.getYear();
    }

    public int getMonth(){
        return entryDate.getMonthValue();
    }

    public int getDay(){
        return entryDate.getDayOfMonth();
    }

    public String getName() {
        return name;
    }

    public Double getSalary() {
        return salary;
    }
    // Convert Employee to a format String
    @Override
    public String toString() {
        return name + "|" + salary + "|" + getYear() + "|" + getMonth() + "|" + getDay();
    }
    // Convert a format String to Employee
    public Employee(String formatEmpStr){
        String[] attrList = formatEmpStr.split("\\|");
        name = attrList[0];
        salary = Double.parseDouble(attrList[1]);
        int year = Integer.parseInt(attrList[2]);
        int month = Integer.parseInt(attrList[3]);
        int day = Integer.parseInt(attrList[4]);
        entryDate = LocalDate.of(year, month, day);
    }
}
