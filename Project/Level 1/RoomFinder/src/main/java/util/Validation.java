package util;

public class Validation {
    public String checkRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return fieldName + " không được để trống";
        }
        return null;
    }

    public String checkPositiveInteger(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) return required;
        try {
            int number = Integer.parseInt(value.trim());
            if (number <= 0) return fieldName + " phải là số nguyên dương";
        } catch (NumberFormatException e) {
            return fieldName + " phải là số nguyên";
        }
        return null;
    }

    public String checkPositiveLong(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) return required;
        try {
            long number = Long.parseLong(value.trim());
            if (number <= 0) return fieldName + " phải là số nguyên dương lớn hơn 0";
        } catch (NumberFormatException e) {
            return fieldName + " phải là số";
        }
        return null;
    }

    public String checkPositiveFloat(String value, String fieldName) {
        String required = checkRequired(value, fieldName);
        if (required != null) return required;
        try {
            float number = Float.parseFloat(value.trim());
            if (number <= 0) return fieldName + " phải lớn hơn 0";
        } catch (NumberFormatException e) {
            return fieldName + " phải là số hợp lệ";
        }
        return null;
    }

    public String checkRole(String value) {
        String required = checkRequired(value, "Vai trò (Role)");
        if (required != null) return required;
        if (!value.equals("RENTER") && !value.equals("LANDLORD")) {
            return "Vai trò chỉ được là RENTER hoặc LANDLORD";
        }
        return null;
    }
}

