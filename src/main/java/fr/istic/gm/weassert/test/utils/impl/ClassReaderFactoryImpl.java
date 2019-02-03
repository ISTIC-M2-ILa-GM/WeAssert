package fr.istic.gm.weassert.test.utils.impl;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static fr.istic.gm.weassert.test.exception.WeAssertException.WRONG_CLASS_PATH;

public class ClassReaderFactoryImpl implements ClassReaderFactory {

    @Override
    public ClassReader create(Class clazz) {
        try {
            String classPath = String.format("%s%s.class", clazz.getProtectionDomain().getCodeSource().getLocation().getPath(), clazz.getName().replace(".", "/"));
            InputStream classInputStream = new FileInputStream(new File(classPath));
            return new ClassReader(classInputStream);
        } catch (Exception e) {
            throw new WeAssertException(WRONG_CLASS_PATH, e);
        }
    }
}
