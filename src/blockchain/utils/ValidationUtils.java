package blockchain.utils;

public class ValidationUtils {

    public static boolean returnValidationFalseWithMessage(String message) {
        System.out.println("Validation Error: " + message);
        return false;
    }

    private ValidationUtils() {
        throw new IllegalArgumentException("Cannot instantiate utility class");
    }
}
