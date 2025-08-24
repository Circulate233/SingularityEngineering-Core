package com.circulation.singularity_engineering_core.utils;

import crafttweaker.annotations.ZenRegister;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.circulation.singularity_engineering_core.crt.CrtAPI.CrtName;

@ZenRegister
@ZenClass(CrtName + "FormatUtils")
public class FormatUtils {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");
    public static final BigInteger BigLongMax = BigInteger.valueOf(Long.MAX_VALUE);
    public static boolean isClient = FMLCommonHandler.instance().getEffectiveSide().isClient();

    static {
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }

    @ZenMethod
    public static String formatFloat(float value, int decimalFraction) {
        return formatDouble(value, decimalFraction);
    }

    @ZenMethod
    public static String formatDouble(double value, int decimalFraction) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(decimalFraction);
        return nf.format(value);
    }

    @ZenMethod
    public static String formatDecimal(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    @ZenMethod
    @NotNull
    public static String formatNumber(long value) {
        if (value < 1_000L) {
            return String.valueOf(value);
        } else if (value < 1_000_000L) {
            return formatFloat((float) value / 1_000L, 2) + "K";
        } else if (value < 1_000_000_000L) {
            return formatDouble((double) value / 1_000_000L, 2) + "M";
        } else if (value < 1_000_000_000_000L) {
            return formatDouble((double) value / 1_000_000_000L, 2) + "G";
        } else if (value < 1_000_000_000_000_000L) {
            return formatDouble((double) value / 1_000_000_000_000L, 2) + "T";
        } else if (value < 1_000_000_000_000_000_000L) {
            return formatDouble((double) value / 1_000_000_000_000_000L, 2) + "P";
        } else {
            return formatDouble((double) value / 1_000_000_000_000_000_000L, 2) + "E";
        }
    }

    @ZenMethod
    public static String formatNumber(BigInteger num) {
        final var value = num.toString();
        var BigValue = num.abs();
        long big = BigValue.compareTo(BigLongMax) >= 0 ? Long.MAX_VALUE : BigValue.longValue();
        StringBuilder zf = new StringBuilder();
        if (value.startsWith("-")) {
            zf.append("-");
        }
        if (big != (Long.MAX_VALUE)) {
            return zf.append(formatNumber(big, 1)).toString();
        } else {
            int cfs = value.length() - 1;
            float cft = (1.00f * Integer.parseInt(value.substring(0, 3))) / 100;

            return zf.append(cft).append(" * 10 ^ ").append(cfs).toString();
        }
    }

    @ZenMethod
    @NotNull
    public static String formatNumber(long value, int decimalFraction) {
        if (value < 1_000L) {
            return String.valueOf(value);
        } else if (value < 1_000_000L) {
            return formatFloat((float) value / 1_000L, decimalFraction) + "K";
        } else if (value < 1_000_000_000L) {
            return formatDouble((double) value / 1_000_000L, decimalFraction) + "M";
        } else if (value < 1_000_000_000_000L) {
            return formatDouble((double) value / 1_000_000_000L, decimalFraction) + "G";
        } else if (value < 1_000_000_000_000_000L) {
            return formatDouble((double) value / 1_000_000_000_000L, decimalFraction) + "T";
        } else if (value < 1_000_000_000_000_000_000L) {
            return formatDouble((double) value / 1_000_000_000_000_000L, decimalFraction) + "P";
        } else {
            return formatDouble((double) value / 1_000_000_000_000_000_000L, decimalFraction) + "E";
        }
    }

    @ZenMethod
    @NotNull
    public static String formatPercent(double num1, double num2) {
        if (num2 == 0) {
            return "0%";
        }
        return formatDouble((num1 / num2) * 100D, 2) + "%";
    }

    @ZenMethod
    @NotNull
    public static String formatFLOPS(double value) {
        if (value < 1000.0F) {
            return formatDouble(value, 1) + "T FloPS";
        }
        return formatDouble(value / 1000.0D, 1) + "P FloPS";
    }

}