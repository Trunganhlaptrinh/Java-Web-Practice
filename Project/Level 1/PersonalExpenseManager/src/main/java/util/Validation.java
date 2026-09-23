package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validation {
    public String checkRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return fieldName + " không được để trống";
        }
        return null;
    }

    public String checkGeneralNameFormat(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        if (!value.trim().matches("^[A-Za-z0-9À-ỹ\\s./+\\-]+$")) {
            return fieldName + " chứa ký tự không hợp lệ";
        }
        return null;
    }

    public String checkPositiveAmount(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        try {
            long amount = Long.parseLong(value.trim());
            if (amount <= 0) {
                return fieldName + " phải là số dương";
            }
        } catch (NumberFormatException exception) {
            return fieldName + " phải là số nguyên dương";
        }
        return null;
    }

    public String checkPositiveInteger(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        try {
            if (Integer.parseInt(value.trim()) <= 0) {
                return fieldName + " phải là số nguyên dương";
            }
        } catch (NumberFormatException exception) {
            return fieldName + " phải là số nguyên dương";
        }
        return null;
    }

    public String checkTransactionType(String value) {
        String required = checkRequired(value, "Loại giao dịch");
        if (required != null) {
            return required;
        }
        if (!"INCOME".equals(value) && !"EXPENSE".equals(value)) {
            return "Loại giao dịch chỉ được là INCOME hoặc EXPENSE";
        }
        return null;
    }

    public String checkDateFormat(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) {
            return required;
        }
        try {
            LocalDate.parse(value.trim());
        } catch (DateTimeParseException exception) {
            return fieldName + " phải đúng định dạng yyyy-MM-dd";
        }
        return null;
    }
}
