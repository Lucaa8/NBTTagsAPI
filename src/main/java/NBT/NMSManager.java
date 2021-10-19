package NBT;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSManager {

    public static String ServerVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    private static Class<?> ItemStack = getNMSClass("ItemStack");

    private static Object invoke(Class<?> methodClass, String methodName, Class<?>[] methodArgsType, Object objectToInvoke, Object...methodArgs) {
        Object value = null;
        try {
            Method m = (methodArgsType==null) ? methodClass.getDeclaredMethod(methodName) :
                    methodClass.getDeclaredMethod(methodName, methodArgsType);
            m.setAccessible(true);
            value = (methodArgs==null) ? m.invoke(objectToInvoke) : m.invoke(objectToInvoke, methodArgs);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static Class<?> getNMSClass(String classname){
        String name = "net.minecraft.server." + ServerVersion + "." + classname;
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            System.out.println("The class named \""+name+"\" can't be found...\n");
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getOBCClass(String packagename, String classname){
        String name = "org.bukkit.craftbukkit." + ServerVersion + "." + ((packagename == null || packagename.equals("") ? "":packagename+".")) + classname;
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            System.out.println("The class named \""+name+"\" can't be found...\n");
            e.printStackTrace();
            return null;
        }
    }

    public static Object getNMSItem(org.bukkit.inventory.ItemStack i) {
        Class<?> CraftItemStack = getOBCClass("inventory", "CraftItemStack");
        return invoke(CraftItemStack, "asNMSCopy", new Class<?>[]{ ItemStack.class }, CraftItemStack, i);
    }
    public static ItemStack getBukkitItem(Object nmsCopy) {
        Class<?> CraftItemStack = getOBCClass("inventory", "CraftItemStack");
        return (ItemStack)invoke(CraftItemStack, "asBukkitCopy", new Class<?>[]{ nmsCopy.getClass() }, CraftItemStack, nmsCopy);
    }

    public static Object fromObjectToNBTBase(Object o) {
        Class<?> nbtType = null;
        Class<?> paramType = null;
        if(o instanceof String) {
            nbtType = getNMSClass("NBTTagString");
            paramType = String.class;
        }
        else if(o instanceof Integer) {
            nbtType = getNMSClass("NBTTagInt");
            paramType = int.class;
        }
        else if(o instanceof Float) {
            nbtType = getNMSClass("NBTTagFloat");
            paramType = float.class;
        }
        else if(o instanceof Double) {
            nbtType = getNMSClass("NBTTagDouble");
            paramType = double.class;
        }
        else if(o instanceof Short) {
            nbtType = getNMSClass("NBTTagShort");
            paramType = short.class;
        }
        else if(o instanceof Long) {
            nbtType = getNMSClass("NBTTagLong");
            paramType = long.class;
        }
        else if(o instanceof Byte || o instanceof Boolean) {
            nbtType = getNMSClass("NBTTagByte");
            if(o instanceof Boolean) paramType = boolean.class;
            else paramType = byte.class;
        }
        if(nbtType==null)return null;
        return invoke(nbtType, "a", new Class<?>[] {paramType}, nbtType, o);
    }

    public static boolean hasTag(ItemStack i) {
        Object item = getNMSItem(i);
        return (boolean)invoke(ItemStack, "hasTag", null, item, null);
    }

    public static Object getTags(ItemStack i) {
        Object item = getNMSItem(i);
        return invoke(ItemStack, "getOrCreateTag", null, item, null);
    }

    public static boolean containsTag(ItemStack i, String tagname) {
        Object tags = getTags(i);
        Object value = invoke(tags.getClass(), "get", new Class<?>[] {String.class}, tags, tagname);
        return value!=null;
    }

    public static Object getTag(ItemStack i, String tagname) {
        Object tags = getTags(i);
        return invoke(tags.getClass(), "get", new Class<?>[] {String.class}, tags, tagname);
    }

    public static String getStringTag(ItemStack i, String tagname) {
        Object tag = getTag(i, tagname);
        return (String) invoke(tag.getClass(), "asString", null, tag, null);
    }

    public static ItemStack setTag(ItemStack i, String tagname, Object value) {
        Object tags = getTags(i);
        value = fromObjectToNBTBase(value);
        invoke(tags.getClass(), "set", new Class<?>[] {String.class, getNMSClass("NBTBase")}, tags, new Object[] {tagname, value});
        Object item = getNMSItem(i);
        invoke(ItemStack, "setTag", new Class<?>[] {tags.getClass()}, item, new Object[] {tags});
        return getBukkitItem(item);
    }

    public static ItemStack removeTag(ItemStack i, String tagname) {
        Object item = getNMSItem(i);
        invoke(ItemStack, "removeTag", new Class<?>[] {String.class}, item, tagname);
        return getBukkitItem(item);
    }

}
