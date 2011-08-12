package nik.funCalc.tree;

/**
 * @author nik
 */
public class ComparisonOperation {
  private int myOpcode;
  private String myText;

  public ComparisonOperation(int opcode, String text) {
    myOpcode = opcode;
    myText = text;
  }

  public int getOpcode() {
    return myOpcode;
  }

  public String getText() {
    return myText;
  }
}
