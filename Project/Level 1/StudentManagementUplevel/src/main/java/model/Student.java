package model;

/**
 * Model tuong ung bang students
 */
public class Student {

    private int id;
    private String name;
    private String semester;

    public Student() {
    }

    public Student(String name, String semester) {
        this.name = name;
        this.semester = semester;
    }

    public Student(int id, String name, String semester) {
        this.id = id;
        this.name = name;
        this.semester = semester;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
