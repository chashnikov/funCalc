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
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    writer.visit(V1_6, ACC_PUBLIC, className, null, "java/lang/Object", new String[0]);
    MethodVisitor methodVisitor = writer.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[0]);
    node.accept(new FunctionCodeGenerator(methodVisitor));
    methodVisitor.visitEnd();
    writer.visitEnd();

    FileOutputStream output = new FileOutputStream(new File(classDir, className + ".class"));
    try {
      output.write(writer.toByteArray());
    }
    finally {
      output.close();
    }
  }
}
