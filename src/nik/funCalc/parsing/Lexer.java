package nik.funCalc.parsing;

import java.io.*;
import java.util.*;

/**
 * @author nik
 */
public class Lexer {
  private static final TokenType[] SYMBOL_TOKENS = {
      TokenType.PLUS, TokenType.MINUS, TokenType.MULT, TokenType.DIV,
      TokenType.LPAREN, TokenType.RPAREN,
      TokenType.COMMA, TokenType.SEMICOLON,
      TokenType.ASSIGN,
      TokenType.LBRACE, TokenType.RBRACE,
      TokenType.EQUAL, TokenType.LESS, TokenType.GREATER, TokenType.GREATER_EQ, TokenType.LESS_EQ, TokenType.NOT_EQ
  };
  private PushbackReader myReader;
  private String myToken;
  private TokenType myTokenType;
  private Stack<TokenType> myTypeBuffer = new Stack<TokenType>();
  private Stack<String> myTokenBuffer = new Stack<String>();
  private Map<String, TokenType> mySymbolTokens = new HashMap<String, TokenType>();

  public Lexer(Reader reader) {
    myReader = new PushbackReader(reader);
    for (TokenType token : SYMBOL_TOKENS) {
      mySymbolTokens.put(token.getName(), token);
    }
  }

  public TokenType nextToken() {
    if (!myTokenBuffer.isEmpty()) {
      myTokenType = myTypeBuffer.pop();
      myToken = myTokenBuffer.pop();
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

      int ch2 = myReader.read();
      if (ch2 != -1) {
        String s = (char) ch+""+(char) ch2;
        if (mySymbolTokens.containsKey(s)) {
          return token(s, mySymbolTokens.get(s));
        }
        myReader.unread(ch2);
      }
      String s = String.valueOf((char) ch);
      if (mySymbolTokens.containsKey(s)) {
        return token(s, mySymbolTokens.get(s));
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
    pushBack(myToken, myTokenType);
  }

  public void pushBack(final String token, final TokenType tokenType) {
    myTokenBuffer.push(token);
    myTypeBuffer.push(tokenType);
  }
}
