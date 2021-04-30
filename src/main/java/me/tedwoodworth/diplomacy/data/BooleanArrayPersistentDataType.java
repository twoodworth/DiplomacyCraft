package me.tedwoodworth.diplomacy.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BooleanArrayPersistentDataType implements PersistentDataType<Byte[], boolean[]> {
    public static BooleanArrayPersistentDataType instance = new BooleanArrayPersistentDataType();

    private BooleanArrayPersistentDataType() {}

    @Override
    @NotNull
    public Class<Byte[]> getPrimitiveType() {
        return Byte[].class;
    }

    @Override
    @NotNull
    public Class<boolean[]> getComplexType() {
        return boolean[].class;
    }

    @Override
    public Byte @NotNull [] toPrimitive(boolean @NotNull [] booleans, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        Byte[] array = new Byte[booleans.length];
        for (int i = 0; i < booleans.length; i++) {
            array[i] = booleans[i] ? (byte) 1 : 0;
        }
        return array;
    }

    @Override
    public boolean @NotNull [] fromPrimitive(Byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        boolean[] array = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            array[i] = switch (bytes[i]) {
                case 1 -> true;
                case 0 -> false;
                default -> throw new IllegalArgumentException("Error converting persistent types, byte must be 0 or 1, not" + bytes[i]);
            };
        }
        return array;
    }
}
