package me.tedwoodworth.diplomacy.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Float3DArrayPersistentDataType implements PersistentDataType<int[][][], float[][][]> {


    public static Float3DArrayPersistentDataType instance = new Float3DArrayPersistentDataType();

    private Float3DArrayPersistentDataType() {
    }

    @Override
    public @NotNull Class<int[][][]> getPrimitiveType() {
        return int[][][].class;
    }

    @Override
    public @NotNull Class<float[][][]> getComplexType() {
        return float[][][].class;
    }

    @Override
    public int[][] @NotNull [] toPrimitive(float[][] @NotNull [] floats, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        int[][][] array = new int[floats.length][floats[0].length][floats[0][0].length];
        for (int i = 0; i < floats.length; i++)
            for (int j = 0; j < floats[0].length; j++)
                for (int k = 0; k < floats[0][0].length; k++)
                    array[i][j][k] = Float.floatToRawIntBits(floats[i][j][k]);
        return array;
    }

    @Override
    public float[][] @NotNull [] fromPrimitive(int[][] @NotNull [] ints, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        float[][][] array = new float[ints.length][ints[0].length][ints[0][0].length];
        for (int i = 0; i < ints.length; i++)
            for (int j = 0; j < ints[0].length; j++)
                for (int k = 0; k < ints[0][0].length; k++)
                    array[i][j][k] = Float.intBitsToFloat(ints[i][j][k]);
        return array;
    }
}
