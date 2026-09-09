/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import java.util.List;
import model.Student;

/**
 *
 * @author Trung Anh
 */
public class StudentDAO {

    private static List<Student> students = new ArrayList<>();

    // add sẵn 1 số data 
    private static int counter = 6;

    static {
        students.add(new Student(1, "Dang Van A", "van@gmail.com", 20));
        students.add(new Student(2, "Nguyen Thi B", "b@gmail.com", 21));
        students.add(new Student(3, "Tran Van C", "c@gmail.com", 22));
        students.add(new Student(4, "Le Thi D", "d@gmail.com", 23));
        students.add(new Student(5, "Pham Van E", "e@gmail.com", 24));
    }

    // READ
    public static List<Student> getStudents() {
        return students;

    }

    // EDIT
    public static Student getStudentById(int id) {
        for (Student student : students) {
            return student;
        }
        return null;

    }

    // CREATE
    public static void addStudent(Student s) {
        s.setId(counter++);
        students.add(s);

    }

    // DELETE
    public static void deleteStudent(int id) {
        Student stu = getStudentById(id);
        students.remove(stu);
    }
}
