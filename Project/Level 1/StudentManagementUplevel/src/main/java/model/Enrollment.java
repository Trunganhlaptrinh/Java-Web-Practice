package model;

/**
 * Model tuong ung bang enrollments (Student dang ky Course)
 * enrolledDate dung String dang "yyyy-MM-dd" cho de xu ly voi Gson va JDBC,
 * khong dung java.time.LocalDate de tranh phai cau hinh them adapter cho Gson
 * studentName / courseName chi dung de hien thi (khi DAO join)
 */
public class Enrollment {

    private int id;
    private int studentId;
    private int courseId;
    private String enrolledDate;
    private String studentName;
    private String courseName;

    public Enrollment() {
    }

    public Enrollment(int studentId, int courseId, String enrolledDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledDate = enrolledDate;
    }

    public Enrollment(int id, int studentId, int courseId, String enrolledDate) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledDate = enrolledDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(String enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
