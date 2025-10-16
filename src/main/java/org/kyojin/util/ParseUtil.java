package org.kyojin.util;

import org.kyojin.enums.BloodType;
import org.kyojin.enums.Gender;
import org.kyojin.enums.Urgency;

public class ParseUtil {

    private ParseUtil() {}

    public static Double parseDouble(String val) {
        try { return val != null ? Double.valueOf(val) : null; }
        catch (NumberFormatException e) { return null; }
    }

    public static Integer parseInt(String val) {
        try { return val != null ? Integer.valueOf(val) : null; }
        catch (NumberFormatException e) { return null; }
    }

    public static Boolean parseBool(String val) {
        return val != null && (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("on"));
    }

    public static BloodType parseBloodType(String val) {
        if (val == null || val.isBlank()) return null;

        String type = val.trim().toUpperCase();

        return switch (type) {
            case "A+", "A POS", "A POSITIVE" -> BloodType.A_POS;
            case "A-", "A NEG", "A NEGATIVE" -> BloodType.A_NEG;
            case "B+", "B POS", "B POSITIVE" -> BloodType.B_POS;
            case "B-", "B NEG", "B NEGATIVE" -> BloodType.B_NEG;
            case "AB+", "AB POS", "AB POSITIVE" -> BloodType.AB_POS;
            case "AB-", "AB NEG", "AB NEGATIVE" -> BloodType.AB_NEG;
            case "O+", "O POS", "O POSITIVE" -> BloodType.O_POS;
            case "O-", "O NEG", "O NEGATIVE" -> BloodType.O_NEG;
            default -> null;
        };
    }

    public static Urgency parseUrgency(String val) {
        if (val == null || val.isBlank()) return null;

        String urgency = val.trim().toUpperCase();

        return switch (urgency) {
            case "NORMAL" -> Urgency.NORMAL;
            case "URGENT" -> Urgency.URGENT;
            case "CRITICAL" -> Urgency.CRITICAL;
            default -> null;
        };
    }

    public static Gender parseGender(String val){
        if (val == null || val.isBlank()) return null;

        String gender = val.trim().toUpperCase();

        return switch (gender) {
            case "MALE" -> Gender.MALE;
            case "FEMALE" -> Gender.FEMALE;
            default -> null;
        };
    }

}
