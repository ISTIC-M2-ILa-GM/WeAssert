package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Slf4j
public class LocalVariableParserImpl implements LocalVariableParser {

    private static final List<Integer> PRIMITIVE_TYPE = asList(Type.BOOLEAN, Type.DOUBLE, Type.FLOAT, Type.INT, Type.LONG, Type.CHAR, Type.SHORT);

    private ClassReader classReader;

    private ClassNode classNode;

    private UrlClassLoaderWrapper urlClassLoaderWrapper;

    @Getter
    private Class clazz;

    public LocalVariableParserImpl(ClassReaderFactory classReaderFactory, Class clazz, ClassNode classNode, UrlClassLoaderWrapper urlClassLoaderWrapper) {
        this.classReader = classReaderFactory.create(clazz);
        this.clazz = clazz;
        this.classNode = classNode;
        this.urlClassLoaderWrapper = urlClassLoaderWrapper;
    }

    public List<LocalVariableParsed> parse() {

        log.info("LOCAL VARIABLE PARSE...");
        classReader.accept(classNode, 0);
        List<LocalVariableParsed> variableParseds = mapMethodsToLocalVariableParsed(classNode.methods);
        log.info("LOCAL VARIABLE PARSED: " + variableParseds);

        return variableParseds;
    }

    private List<LocalVariableParsed> mapMethodsToLocalVariableParsed(List<MethodNode> methods) {
        return methods.stream()
                .filter(m -> !"<init>".equals(m.name))
                .map(m -> LocalVariableParsed.builder()
                        .name(m.name)
                        .desc(m.desc)
                        .localVariables(retrieveLocalVariables(m))
                        .build())
                .collect(Collectors.toList());
    }

    private List<String> retrieveLocalVariables(MethodNode methodNode) {
        return methodNode.localVariables.stream()
                .filter(v -> !"this".equals(v.name))
                .map(this::retrieveVariableName)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<String> retrieveVariableName(LocalVariableNode localVariable) {
        List<String> variables = new ArrayList<>();
        Type t = Type.getType(localVariable.desc);
        if (t.getSort() == Type.OBJECT) {
            variables.addAll(retrieveGetters(localVariable, t));
        } else if (PRIMITIVE_TYPE.contains(t.getSort())) {
            variables.add(localVariable.name);
        }
        return variables;
    }

    private List<String> retrieveGetters(LocalVariableNode localVariable, Type type) {
        List<String> variables = new ArrayList<>();
        Class<?> c = urlClassLoaderWrapper.getClassList().stream().filter(cl -> cl.getName().equals(type.getClassName())).findFirst().orElse(null);
        if (c != null) {
            for (Field f : c.getDeclaredFields()) {
                if (f.getName() == null || f.getName().isEmpty()) {
                    continue;
                }
                String methodName = String.format("get%s%s", f.getName().substring(0, 1).toUpperCase(), f.getName().substring(1));
                if (contains(c.getDeclaredMethods(), methodName)) {
                    variables.add(String.format("%s.%s()", localVariable.name, methodName));
                }
            }
        }
        return variables;
    }

    private boolean contains(Method[] declaredMethods, String methodName) {
        for (Method m : declaredMethods) {
            if (methodName.equals(m.getName())) {
                return true;
            }
        }
        return false;
    }
}
