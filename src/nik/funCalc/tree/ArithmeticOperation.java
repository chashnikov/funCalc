package nik.funCalc.tree;

import org.objectweb.asm.MethodVisitor;

/**
 * @author nik
 */
public interface ArithmeticOperation {
  void generate(MethodVisitor methodVisitor);

  String getText();
}
