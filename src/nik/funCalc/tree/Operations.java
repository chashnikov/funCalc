package nik.funCalc.tree;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author nik
 */
public class Operations {
  public static final Operation ADD = new InstructionOperation(Opcodes.IADD, "+");

  public static final Operation SUB = new InstructionOperation(Opcodes.ISUB, "-");

  public static final Operation MULT = new InstructionOperation(Opcodes.IMUL, "*");

  public static final Operation DIV = new InstructionOperation(Opcodes.IDIV, "/");

  private static class InstructionOperation implements Operation {
    private int myOpcode;
    private String myText;

    private InstructionOperation(int opcode, String text) {
      myOpcode = opcode;
      myText = text;
    }

    public void generate(MethodVisitor methodVisitor) {
      methodVisitor.visitInsn(myOpcode);
    }

    public String getText() {
      return myText;
    }
  }
}
