package pojo;

public class Manager extends Employee {
    private Employee secretary;

    public Manager(String name, Double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        secretary = null;
    }

    public Manager(String formatEmpStr) {
        super(formatEmpStr);
        secretary = null;
    }

    public void setSecretary(Employee secretary) {
        this.secretary = secretary;
    }

    public Employee getSecretary() {
        return secretary;
    }

    @Override
    public String toString() {
        return "Manager: " + super.toString()+", his/her secretary is "+secretary.getName();
    }
}
