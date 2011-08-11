package nik.funCalc.tree;

import org.objectweb.asm.MethodVisitor;

/**
 * @author nik
 */
public interface Operation {
  void generate(MethodVisitor methodVisitor);

  String getText();
}
