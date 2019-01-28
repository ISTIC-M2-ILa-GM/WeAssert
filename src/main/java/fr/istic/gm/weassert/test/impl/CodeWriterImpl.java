package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import javassist.*;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

@Getter
public class CodeWriterImpl implements CodeWriter {
    private String className;

    private CtClass classContainer;

    public CodeWriterImpl(Class clazz) {
        ClassPool pool = ClassPool.getDefault();

        try {
            this.classContainer = pool.get(clazz.getName());
            this.className = clazz.getName();
        } catch (NotFoundException e) {
            throw new WeAssertException(
                    String.format("CodeWriter: could not find class named %s", clazz.getName()), e
            );
        }
    }

    public void insertOne(String methodName, String desc, String code) {
        try {
            CtMethod method = this.classContainer.getMethod(methodName, desc);
            try {
                method.insertAfter(code);
            } catch (CannotCompileException e) {
                throw new WeAssertException("CodeWriterImpl: could not insert code", e);
            }
        } catch (NotFoundException e) {
            throw new WeAssertException(
                    String.format("CodeWriterImpl: could not find method named %s", methodName), e
            );
        }
    }

    @Override
    public void insertMany(String methodName, String desc, List<String> codes) {
        codes.forEach(src -> this.insertOne(methodName, desc, src));
    }

    @Override
    public void writeAndCloseFile() {
        try {
            this.classContainer.writeFile();
        } catch (Exception e) {
            throw new WeAssertException("CodeWriterImpl: could not write file", e);
        }
    }
}
