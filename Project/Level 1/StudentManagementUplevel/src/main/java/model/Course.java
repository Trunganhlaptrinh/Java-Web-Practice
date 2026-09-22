package model;

/**
 * Model tuong ung bang courses
 * departmentName chi dung de hien thi (khi DAO join sang departments)
 */
public class Course {

    private int id;
    private String name;
    private int departmentId;
    private String departmentName;

    public Course() {
    }

    public Course(String name, int departmentId) {
        this.name = name;
        this.departmentId = departmentId;
    }

    public Course(int id, String name, int departmentId) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
