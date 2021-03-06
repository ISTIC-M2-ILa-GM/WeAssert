package fr.istic.gm.weassert.test.utils.impl;

import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static fr.istic.gm.weassert.test.exception.WeAssertException.WRONG_CLASS_PATH;
import static fr.istic.gm.weassert.test.utils.ClassResolverUtil.mapClassToClassPath;

public class ClassReaderFactoryImpl implements ClassReaderFactory {

    @Override
    public ClassReader create(Class clazz) {
        try {
            String classPath = mapClassToClassPath(clazz);
            InputStream classInputStream = new FileInputStream(new File(classPath));
            return new ClassReader(classInputStream);
        } catch (Exception e) {
            throw new WeAssertException(WRONG_CLASS_PATH, e);
        }
    }
}
