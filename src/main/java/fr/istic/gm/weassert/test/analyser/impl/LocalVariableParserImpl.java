package fr.istic.gm.weassert.test.analyser.impl;

import fr.istic.gm.weassert.test.analyser.LocalVariableParser;
import fr.istic.gm.weassert.test.model.LocalVariableParsed;
import fr.istic.gm.weassert.test.utils.ClassReaderFactory;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LocalVariableParserImpl implements LocalVariableParser {

    private ClassReader classReader;

    private ClassNode classNode;

    @Getter
    private Class clazz;

    public LocalVariableParserImpl(ClassReaderFactory classReaderFactory, Class clazz, ClassNode classNode) {
        this.classReader = classReaderFactory.create(clazz);
        this.clazz = clazz;
        this.classNode = classNode;
    }

    public List<LocalVariableParsed> parse() {

        log.info("LOCAL VARIABLE PARSE...");
        classReader.accept(classNode, 0);
        List<LocalVariableParsed> variableParseds = classNode.methods.stream()
                .filter(m -> !"<init>".equals(m.name))
                .map(m -> LocalVariableParsed.builder()
                        .name(m.name)
                        .desc(m.desc)
                        .localVariables(retrieveLocalVariables(m))
                        .build())
                .collect(Collectors.toList());
        log.info("LOCAL VARIABLE PARSED: " + variableParseds);

        return variableParseds;
    }

    private List<String> retrieveLocalVariables(MethodNode methodNode) {
        return methodNode.localVariables.stream().filter(v -> "this".equals(v.name)).map(v -> v.name).collect(Collectors.toList());
    }
}
