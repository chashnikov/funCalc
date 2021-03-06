package nik.funCalc;

import junit.framework.Assert;
import junit.framework.TestCase;
import nik.funCalc.parsing.Lexer;
import nik.funCalc.parsing.TokenType;

import java.io.StringReader;

/**
 * @author nik
 */
public class LexerTest extends TestCase {
  public void testId() {
    Lexer lexer = create("a");
    Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
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
    Lexer lexer = create("+");
    assertEquals(TokenType.PLUS, lexer.nextToken());
    assertEquals("+", lexer.getToken());
    assertEof(lexer);
  }

  public void testSymbol2() throws Exception {
    Lexer lexer = create("==");
    assertEquals(TokenType.EQUAL, lexer.nextToken());
    assertEquals("==", lexer.getToken());
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
    assertEquals(TokenType.PLUS, lexer.nextToken());
    assertEquals("+", lexer.getToken());
    assertEquals(TokenType.INT, lexer.nextToken());
    assertEquals("2", lexer.getToken());
    assertEof(lexer);
  }

  public void testCompare() throws Exception {
    Lexer lexer = create("x==2");
    assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
    assertEquals("x", lexer.getToken());
    assertEquals(TokenType.EQUAL, lexer.nextToken());
    assertEquals("==", lexer.getToken());
    assertEquals(TokenType.INT, lexer.nextToken());
    assertEquals("2", lexer.getToken());
    assertEof(lexer);
  }

  public void testWhitespaces() throws Exception {
    Lexer lexer = create("  x \t-\n2    ");
    assertEquals(TokenType.IDENTIFIER, lexer.nextToken());
    assertEquals("x", lexer.getToken());
    assertEquals(TokenType.MINUS, lexer.nextToken());
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
