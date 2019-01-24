package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.List;

public class CodeWriterImpl implements CodeWriter {
    private String className;

    private CtClass classContainer;

    public CodeWriterImpl(String className) {
        ClassPool pool = ClassPool.getDefault();

        try {
            this.classContainer = pool.get(className);
            this.className = className;
        } catch (NotFoundException e) {
            throw new WeAssertException(String.format("Could not find class name %s", className), e);
        }
    }

    @Override
    public void insertOne(String code) {

    }

    @Override
    public void insertMany(List<String> codes) {

    }
}
