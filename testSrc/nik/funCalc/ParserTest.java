package nik.funCalc;

import junit.framework.TestCase;
import nik.funCalc.parsing.Lexer;
import nik.funCalc.parsing.ParserImpl;
import nik.funCalc.tree.StatementsNode;

import java.io.StringReader;

/**
 * @author nik
 */
public class ParserTest extends TestCase {
  public void testNumber() {
    assertParsed("1;");
  }

  public void testAdd() {
    assertParsed("1+2;", "(1)+(2);");
  }

  public void testAssociativity() {
    assertParsed("1-2-3;", "((1)-(2))-(3);");
  }

  public void testPriority() {
    assertParsed("1-2*3;", "(1)-((2)*(3));");
  }

  public void testPrint() {
    assertParsed("print 1;");
  }

  public void testStatements() {
    assertParsed("print 1;\n" +
                 "print 2;");
  }

  public void testAssign() {
    assertParsed("a=1;");
  }

  public void testVariable() {
    assertParsed("a+b;", "(a)+(b);");
  }

  private void assertParsed(final String text) {
    assertParsed(text, text);
  }

  private void assertParsed(final String text, String nodeText) {
    StringReader r = new StringReader(text);
    Lexer lexer = new Lexer(r);
    ParserImpl parser = new ParserImpl(lexer);
    StatementsNode node = parser.parseProgram();
    assertEquals(nodeText, node.getText());
  }
}
