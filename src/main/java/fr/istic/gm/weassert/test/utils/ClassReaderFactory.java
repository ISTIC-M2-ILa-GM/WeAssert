package fr.istic.gm.weassert.test.utils;

import jdk.internal.org.objectweb.asm.ClassReader;

public interface ClassReaderFactory {
    ClassReader create(Class clazz);
}
