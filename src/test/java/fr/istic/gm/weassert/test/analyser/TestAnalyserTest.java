package fr.istic.gm.weassert.test.analyser;

import fr.istic.gm.weassert.fake.Person;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class TestAnalyserTest {

    private TestAnalyser testAnalyser;

    @Before
    public void setUp() {
        testAnalyser = new TestAnalyser();
    }

    @Test
    public void shouldRetrieveAllLocalVariables() throws NotFoundException, IOException, BadBytecode {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("fr/istic/gm/weassert/test/TestRunnerTest");
//        BufferedInputStream fin = new BufferedInputStream(new FileInputStream("fake/target/test-classes/fr/istic/gm/weassert/fake/PersonTest.class"));
//        ClassFile cf = new ClassFile(new DataInputStream(fin));
//        MethodInfo minfo = cf.getMethod("testAge");
//        CodeAttribute ca = minfo.getCodeAttribute();
//        CodeIterator codeIterator = ca.iterator();
//        while (codeIterator.hasNext()) {
//            int index = codeIterator.next();
//            int op = codeIterator.byteAt(index);
//            System.out.println(Mnemonic.OPCODE[op]);
//        }
//        ClassReader reader = new ClassReader("fr/istic/gm/weassert/analyser/TestAnalyserTest");
//        ClassNode classNode = new ClassNode();
//        reader.accept(classNode, 0);
//        for (MethodNode mn : classNode.methods) {
//            if ("testAge".equals(mn.name)) {
//                for (LocalVariableNode local : mn.localVariables) {
//                    System.out.println("Local Variable: " + local.name + " : " + local.desc + " : " + local.signature + " : " + local.index);
//                    if (!"this".equals(local.name)) {
//                        mn.visitVarInsn(Opcodes.AASTORE, local.index);
//                    }
//                }
//            }
//        }
//        ClassPool pool = ClassPool.getDefault();
//        CtClass cc = pool.get("test.Rectangle");
//        cc.setSuperclass(pool.get("test.Point"));
//        cc.writeFile();

    }

    @Test
    public void testAge() {

        Person p = new Person();
        p.setAge(13);
        p.setName("name");

        assertFalse(p.isAdult());
    }
}
