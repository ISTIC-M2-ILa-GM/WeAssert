package fr.istic.gm.weassert.test.utils;

public class ClassResolverUtil {
    public static String mapClassToClassPath(Class clazz) {
        return String.format("%s%s.class", clazz.getProtectionDomain().getCodeSource().getLocation().getPath(), clazz.getName().replace(".", "/"));
    }
}
