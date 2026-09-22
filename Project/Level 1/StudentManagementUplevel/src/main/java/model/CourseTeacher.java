package model;

/**
 * Model tuong ung bang trung gian course_teacher (gan giao vien cho mon hoc)
 */
public class CourseTeacher {

    private int id;
    private int courseId;
    private int teacherId;
    private String teacherName;

    public CourseTeacher() {
    }

    public CourseTeacher(int courseId, int teacherId) {
        this.courseId = courseId;
        this.teacherId = teacherId;
    }

    public CourseTeacher(int id, int courseId, int teacherId) {
        this.id = id;
        this.courseId = courseId;
        this.teacherId = teacherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
