package me.tedwoodworth.diplomacy.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class FloatArrayPersistentDataType implements PersistentDataType<int[], float[]> {
    public static FloatArrayPersistentDataType instance = new FloatArrayPersistentDataType();

    private FloatArrayPersistentDataType() {}

    @Override
    @NotNull
    public Class<int[]> getPrimitiveType() {
        return int[].class;
    }

    @Override
    @NotNull
    public Class<float[]> getComplexType() {
        return float[].class;
    }

    @Override
    public int @NotNull [] toPrimitive(float @NotNull [] floats, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        int[] array = new int[floats.length];
        for (int i = 0; i < floats.length; i++) {
            array[i] = Float.floatToRawIntBits(floats[i]);
        }
        return array;
    }

    @Override
    public float @NotNull [] fromPrimitive(int @NotNull [] ints, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        float[] array = new float[ints.length];
        for (int i = 0; i < ints.length; i++) {
            array[i] = Float.intBitsToFloat(ints[i]);
        }
        return array;
    }
}
