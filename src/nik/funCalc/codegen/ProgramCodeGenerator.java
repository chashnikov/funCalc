package nik.funCalc.codegen;

import nik.funCalc.tree.StatementsNode;
import org.objectweb.asm.*;

import java.io.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author nik
 */
public class ProgramCodeGenerator {
  public static void generate(StatementsNode node, File classDir, String className) throws IOException {
    byte[] bytes = generateClassContent(node, className);
    classDir.mkdirs();
    File file = new File(classDir, className+".class");
    FileOutputStream output = new FileOutputStream(file);
    try {
      output.write(bytes);
    }
    finally {
      output.close();
    }
  }

  public static byte[] generateClassContent(StatementsNode node, String className) {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    writer.visit(V1_6, ACC_PUBLIC, className, null, "java/lang/Object", new String[0]);
    MethodVisitor methodVisitor = writer.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[0]);
    methodVisitor.visitCode();
    node.accept(new FunctionCodeGenerator(methodVisitor));
    methodVisitor.visitInsn(RETURN);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();
    writer.visitEnd();
    return writer.toByteArray();
  }
}
