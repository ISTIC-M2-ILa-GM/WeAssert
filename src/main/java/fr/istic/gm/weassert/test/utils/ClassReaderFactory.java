package fr.istic.gm.weassert.test.utils;

import org.objectweb.asm.ClassReader;

public interface ClassReaderFactory {
    ClassReader create(Class clazz);
}
