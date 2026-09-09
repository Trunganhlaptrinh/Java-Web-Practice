
package model;

/**
 *
 * @author Trung Anh
 */

public class Student {
    private int id;
    private String name;
    private String email;
    private int dob;

    // Constructor không tham số
    public Student() {
    }

    // Constructor có đầy đủ tham số
    public Student(int id, String name, String email, int dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    // Getter và Setter cho id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter và Setter cho dob
    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }
}
