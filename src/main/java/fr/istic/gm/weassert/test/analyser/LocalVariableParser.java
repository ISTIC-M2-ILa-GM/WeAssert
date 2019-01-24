package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class LocalVariableParser {

    private static final String LOCAL_VARIABLE_FOUND = "Found local Variable: %s, %s, %s,%s";

    private ClassReader classReader;

    private ClassNode classNode;

    public LocalVariableParser(ClassReaderFactory classReaderFactory, Class clazz, ClassNode classNode) {
        this.classReader = classReaderFactory.create(clazz);
        this.classNode = classNode;
    }

    public Map<String, List<String>> parse() {
        classReader.accept(classNode, 0);
        Map<String, List<String>> variables = new HashMap<>();
        classNode.methods.stream().filter(m -> !"<init>".equals(m.name)).forEach(m -> variables.put(m.name, retrieveLocalVariables(m)));
        return variables;
    }

    private List<String> retrieveLocalVariables(MethodNode methodNode) {
        List<String> variables = new ArrayList<>();
        for (LocalVariableNode v : methodNode.localVariables) {
            if ("this".equals(v.name)) {
                continue;
            }
            log.info(String.format(LOCAL_VARIABLE_FOUND, v.name, v.desc, v.signature, v.index));
            variables.add(v.name);
        }
        return variables;
    }
}
