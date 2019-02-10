package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.CodeWriter;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.Getter;

import java.util.List;

@Getter
public class JavassitCodeWriter implements CodeWriter {
    private String className;

    private CtClass classContainer;

    public JavassitCodeWriter(Class clazz) {
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
            method.insertAfter(code);
        } catch (CannotCompileException e) {
            throw new WeAssertException("JavassitCodeWriter: could not insert code", e);
        } catch (NotFoundException e) {
            throw new WeAssertException(
                    String.format("JavassitCodeWriter: could not find method named %s", methodName), e
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
            throw new WeAssertException("JavassitCodeWriter: could not write file", e);
        }
    }
}
