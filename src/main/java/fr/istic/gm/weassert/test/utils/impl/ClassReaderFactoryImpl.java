package fr.istic.gm.weassert.test.utils.impl;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import jdk.internal.org.objectweb.asm.ClassReader;

import static fr.istic.gm.weassert.test.exception.WeAssertException.WRONG_CLASS_PATH;

public class ClassReaderFactoryImpl implements ClassReaderFactory {

    @Override
    public ClassReader create(Class clazz) {
        try {
            return new ClassReader(clazz.getName());
        } catch (Exception e) {
            throw new WeAssertException(WRONG_CLASS_PATH, e);
        }
    }
}
