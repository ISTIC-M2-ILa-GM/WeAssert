package fr.istic.gm.weassert.test.impl;

import fr.istic.gm.weassert.test.AssertionWriter;
import fr.istic.gm.weassert.test.model.Assertion;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.List;

public class AssertionWriterImpl implements AssertionWriter {
    @Override
    public void insert(String className, String methodName, List<Assertion> assertions) {
        // TODO: 1. https://stackoverflow.com/a/26381675

        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass cc = pool.get(className);

            System.out.println("Found class file " + cc.getSimpleName());
        } catch (NotFoundException e) {
            System.err.println("Could not find class");
            e.printStackTrace();
        }

        // TODO: 2. look for class named 'clazz'
        // TODO: 3. look for class method named 'methodName'
        // TODO: 4. generate and insert assertions
    }

    @Override
    public String generate(Assertion assertion) {
        return String.format("assertEquals(%s,%s);", assertion.getActualValue(), assertion.getExpectedValue());
    }
}
