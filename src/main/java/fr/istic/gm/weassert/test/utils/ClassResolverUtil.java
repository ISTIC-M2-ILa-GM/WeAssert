package fr.istic.gm.weassert.test.utils;

public class ClassResolverUtil {

    private ClassResolverUtil() {
    }

    public static String mapClassToClassPath(Class clazz) {
        return String.format("%s%s.class", clazz.getProtectionDomain().getCodeSource().getLocation().getPath(), clazz.getName().replace(".", "/"));
    }

    public static String mapClassToSourcePath(Class clazz) {
        return String.format("%s%s.java", clazz.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/target/classes/", "/src/main/java/").replace("/target/test-classes/", "/src/test/java/"), clazz.getName().replace(".", "/"));
    }
}
