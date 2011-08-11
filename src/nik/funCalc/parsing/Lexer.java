package nik.funCalc.parsing;

import java.io.*;
import java.util.*;

/**
 * @author nik
 */
public class Lexer {
  private static final Set<Character> SYMBOLS = new HashSet<Character>(Arrays.asList('+', '-', '*', '/', '=', ';'));
  private PushbackReader myReader;
  private String myToken;
  private TokenType myTokenType;
  private boolean myPushedBack;

  public Lexer(Reader reader) {
    myReader = new PushbackReader(reader);
  }

  public TokenType nextToken() {
    if (myPushedBack) {
      myPushedBack = false;
      return myTokenType;
    }
    try {
      int ch = myReader.read();
      while (ch != -1 && Character.isWhitespace(ch)) {
        ch = myReader.read();
      }

      if (ch == -1) {
        return token("", TokenType.EOF);
      }

      if (SYMBOLS.contains((char) ch)) {
        return token(String.valueOf((char) ch), TokenType.SYMBOL);
      }

      if ('0' <= ch && ch <= '9') {
        StringBuilder buffer = new StringBuilder();
        buffer.append((char) ch);
        while ((ch = myReader.read()) != -1 && '0' <= ch && ch <= '9') {
          buffer.append((char) ch);
        }
        if (ch != -1) {
          myReader.unread(ch);
        }
        return token(buffer.toString(), TokenType.INT);
      }
      if (Character.isJavaIdentifierStart(ch)) {
        StringBuilder buffer = new StringBuilder();
        buffer.append((char) ch);
        while ((ch = myReader.read()) != -1 && Character.isJavaIdentifierPart(ch)) {
          buffer.append((char) ch);
        }
        if (ch != -1) {
          myReader.unread(ch);
        }
        return token(buffer.toString(), TokenType.IDENTIFIER);
      }
      throw new ParsingException("Invalid symbol: "+(char) ch);
    }
    catch (IOException e) {
      throw new ParsingException(e);
    }
  }

  public TokenType getTokenType() {
    return myTokenType;
  }

  private TokenType token(String token, final TokenType type) {
    myToken = token;
    myTokenType = type;
    return type;
  }

  public String getToken() {
    return myToken;
  }

  public void pushBack() {
    if (myPushedBack) {
      throw new AssertionError("Cannot push back more than one token!");
    }
    myPushedBack = true;
  }
}
