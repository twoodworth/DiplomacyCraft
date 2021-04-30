package me.tedwoodworth.diplomacy.Items;

public class PersistentDataUtil {

    public static String floatsToString(float[] floats) {
        char[] chars = new char[floats.length * 2];
        for (int i = 0; i < floats.length; i++) {
            floatToChars(floats[i], i * 2, chars);
        }
        return new String(chars);
    }

    public static float[] stringToFloats(String string) {
        if (string.length() % 2 != 0) throw new IllegalArgumentException("String length must be even.");
        char[] chars = string.toCharArray();
        float[] results = new float[string.length() / 2];
        for (int i = 0; i < results.length; i++) {
            results[i] = charsToFloat(i * 2, chars);
        }
        return results;
    }

    private static void floatToChars(float val, int pos, char[] chars) {
        var bits = Float.floatToRawIntBits(val);
        chars[pos] = (char) (bits >> 16);
        chars[pos + 1] = (char) bits;
    }

    private static float charsToFloat(int pos, char[] chars) {
        var bits = (chars[pos] << 16) | ((int) (chars[pos + 1]));
        return Float.intBitsToFloat(bits);
    }
}
