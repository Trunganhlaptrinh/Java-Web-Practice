package model;

/**
 * Model tuong ung bang teachers
 * departmentId la khoa ngoai tro toi departments.id
 * departmentName chi dung de hien thi (khi DAO join sang departments),
 * khong luu vao DB
 */
public class Teacher {

    private int id;
    private String name;
    private int departmentId;
    private String departmentName;

    public Teacher() {
    }

    public Teacher(String name, int departmentId) {
        this.name = name;
        this.departmentId = departmentId;
    }

    public Teacher(int id, String name, int departmentId) {
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
