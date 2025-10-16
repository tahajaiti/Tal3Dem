package org.kyojin.util;

import org.kyojin.enums.BloodType;

public class BloodTypeUtil {

    private BloodTypeUtil() {}

    public static boolean isCompatible(BloodType donor, BloodType receiver) {
        return switch (receiver) {
            case O_NEG -> donor == BloodType.O_NEG;
            case O_POS -> donor == BloodType.O_NEG || donor == BloodType.O_POS;
            case A_NEG -> donor == BloodType.A_NEG || donor == BloodType.O_NEG;
            case A_POS -> donor == BloodType.A_POS || donor == BloodType.A_NEG ||
                    donor == BloodType.O_POS || donor == BloodType.O_NEG;
            case B_NEG -> donor == BloodType.B_NEG || donor == BloodType.O_NEG;
            case B_POS -> donor == BloodType.B_POS || donor == BloodType.B_NEG ||
                    donor == BloodType.O_POS || donor == BloodType.O_NEG;
            case AB_NEG -> donor == BloodType.AB_NEG || donor == BloodType.A_NEG ||
                    donor == BloodType.B_NEG || donor == BloodType.O_NEG;
            case AB_POS -> true;
        };
    }
}
