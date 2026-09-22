package util;

/**
 * Kiem tra du lieu dau vao tu request (thay vi doc Scanner nhu ban console).
 * Moi ham tra ve String: null nghia la hop le, khac null la thong bao loi.
 * Controller se goi cac ham nay, gom loi lai va tra ve JSON cho JS hien thi,
 * thay vi vong lap hoi lai nhu console.
 */
public class Validation {

    // kiem tra khong duoc rong
    public String checkRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return fieldName + " khong duoc de trong";
        }
        return null;
    }

    // kiem tra chi chua chu cai va khoang trang (dung cho ten nguoi: Student, Teacher)
    public String checkNameFormat(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        if (!value.trim().matches("^[A-Za-z\\s]+$")) {
            return fieldName + " chi duoc chua chu cai va khoang trang";
        }
        return null;
    }

    // kiem tra chu + so + khoang trang (dung cho Semester, vi du "HK1 2025")
    public String checkSemesterFormat(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        if (!value.trim().matches("^[A-Za-z0-9\\s]+$")) {
            return fieldName + " chi duoc chua chu cai, chu so va khoang trang";
        }
        return null;
    }

    // kiem tra ten chung (Course, Department) - cho phep chu, so, khoang trang,
    // dau cham va dau gach ngang de dat ten kieu "Java Programming", "C/C++"
    public String checkGeneralNameFormat(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        if (!value.trim().matches("^[A-Za-z0-9À-ỹ\\s./+\\-]+$")) {
            return fieldName + " chua ky tu khong hop le";
        }
        return null;
    }

    // kiem tra so nguyen duong (dung cho id: departmentId, courseId, teacherId, studentId)
    public String checkPositiveInteger(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        try {
            int number = Integer.parseInt(value.trim());
            if (number <= 0) {
                return fieldName + " phai la so nguyen duong";
            }
        } catch (NumberFormatException e) {
            return fieldName + " phai la so nguyen";
        }
        return null;
    }

    // kiem tra dinh dang ngay yyyy-MM-dd (dung cho enrolledDate)
    public String checkDateFormat(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        if (!value.trim().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return fieldName + " phai dung dinh dang yyyy-MM-dd";
        }
        try {
            java.time.LocalDate.parse(value.trim());
        } catch (java.time.format.DateTimeParseException e) {
            return fieldName + " khong phai ngay hop le";
        }
        return null;
    }
}