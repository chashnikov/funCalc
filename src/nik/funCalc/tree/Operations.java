package nik.funCalc.tree;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author nik
 */
public class Operations {
  public static final ArithmeticOperation ADD = new InstructionOperation(Opcodes.IADD, "+");
  public static final ArithmeticOperation SUB = new InstructionOperation(Opcodes.ISUB, "-");
  public static final ArithmeticOperation MULT = new InstructionOperation(Opcodes.IMUL, "*");
  public static final ArithmeticOperation DIV = new InstructionOperation(Opcodes.IDIV, "/");

  public static final ComparisonOperation EQUAL = new ComparisonOperation(Opcodes.IF_ICMPEQ, "==");
  public static final ComparisonOperation LESS = new ComparisonOperation(Opcodes.IF_ICMPLT, "<");
  public static final ComparisonOperation GREATER = new ComparisonOperation(Opcodes.IF_ICMPGT, ">");

  private static class InstructionOperation implements ArithmeticOperation {
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
