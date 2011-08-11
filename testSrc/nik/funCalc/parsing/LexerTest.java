package nik.funCalc.parsing;

import junit.framework.TestCase;

import java.io.StringReader;

/**
 * @author nik
 */
public class LexerTest extends TestCase {
  public void testId() {
    Lexer lexer = create("a");
    assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
    assertEquals("a", lexer.getToken());
    assertEof(lexer);
  }

  public void testId2() {
    Lexer lexer = create("a1");
    assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
    assertEquals("a1", lexer.getToken());
    assertEof(lexer);
  }

  public void testNum() {
    Lexer lexer = create("1");
    assertEquals(TokenType.INT, lexer.nextToken());
    assertEquals("1", lexer.getToken());
    assertEof(lexer);
  }

  public void testNum2() {
    Lexer lexer = create("12");
    assertEquals(TokenType.INT, lexer.nextToken());
    assertEquals("12", lexer.getToken());
    assertEof(lexer);
  }

  public void testSymbol() throws Exception {
    Lexer lexer = create("12");
    assertEquals(TokenType.INT, lexer.nextToken());
    assertEquals("12", lexer.getToken());
    assertEof(lexer);
  }

  public void testEmpty() throws Exception {
    Lexer lexer = create("");
    assertEof(lexer);
  }

  public void testComplex() throws Exception {
    Lexer lexer = create("x+2");
    assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
    assertEquals("x", lexer.getToken());
    assertEquals(TokenType.SYMBOL, lexer.nextToken());
    assertEquals("+", lexer.getToken());
    assertEquals(TokenType.INT, lexer.nextToken());
    assertEquals("2", lexer.getToken());
    assertEof(lexer);
  }

  public void testWhitespaces() throws Exception {
    Lexer lexer = create("  x \t+\n2    ");
    assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
    assertEquals("x", lexer.getToken());
    assertEquals(TokenType.SYMBOL, lexer.nextToken());
    assertEquals("+", lexer.getToken());
    assertEquals(TokenType.INT, lexer.nextToken());
    assertEquals("2", lexer.getToken());
    assertEof(lexer);
  }

  private void assertEof(Lexer lexer) {
    assertEquals(TokenType.EOF, lexer.nextToken());
  }

  private Lexer create(final String text) {
    StringReader r = new StringReader(text);
    return new Lexer(r);
  }
}
