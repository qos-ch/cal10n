/*
 * Copyright (c) 2009 QOS.ch All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS  IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.qos.cal10n.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ch.qos.cal10n.MessageConveyorException;
import ch.qos.cal10n.util.Token.TokenType;

/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class TokenStream {

  enum State {
    START, COMMENT, KEY, SEPARATOR, VAL, TRAILING_BACKSLASH;
  }

  BufferedReader lineReader;
  State state = State.START;

  TokenStream(Reader reader) {
    this.lineReader = new BufferedReader(reader);
  }

  List<Token> tokenize() {
    List<Token> tokenList = new ArrayList<Token>();

    while (true) {
      String currentLine;
      try {
        currentLine = lineReader.readLine();
      } catch (IOException e) {
        throw new MessageConveyorException("Failed to read input stream", e);
      }
      if (currentLine == null) {
        break;
      }
      if(state != State.TRAILING_BACKSLASH) {
        state = State.START;
      }
      tokenizeLine(tokenList, currentLine);
      tokenList.add(Token.EOL);
    }

    return tokenList;
  }

  private void tokenizeLine(List<Token> tokenList, String line) {
    int len = line.length();
    StringBuffer buf = new StringBuffer();

    for (int pointer = 0; pointer < len; pointer++) {
      char c = line.charAt(pointer);
      switch (state) {
      case START:
        if (isWhiteSpace(c)) {
          // same sate
        } else if (c == '#') {
          state = State.COMMENT;
          return;
        } else if (isNonWhiteSpaceSeparator(c)) {
          state = State.SEPARATOR;
          buf.append(c);
        } else {
          state = State.KEY;
          buf.append(c);
        }
        break;

      case KEY:
        if (isWhiteSpace(c) || isNonWhiteSpaceSeparator(c)) {
          tokenList.add(new Token(TokenType.KEY, buf.toString()));
          buf.setLength(0);
          buf.append(c);
          state = State.SEPARATOR;
        } else {
          buf.append(c);
        }
        break;
        
      case SEPARATOR:

        if (isWhiteSpace(c) || isNonWhiteSpaceSeparator(c)) {
          buf.append(c);
        } else {
          tokenList.add(new Token(TokenType.SEPARATOR, buf.toString()));
          buf.setLength(0);
          buf.append(c);
          state = State.VAL;
        } 
        break;

      case VAL:
        if(c == '\\') {
          if(isTrailingBackSlash(line, pointer+1)) {
            tokenList.add(new Token(TokenType.VALUE, buf.toString()));
            buf.setLength(0);
            state = State.TRAILING_BACKSLASH;
            tokenList.add(Token.TRAILING_BACKSLASH);
            return;
          } else {
            buf.append(c);
          }
        } else {
          buf.append(c);
        }
        break;
        
      case TRAILING_BACKSLASH:
        if (!isWhiteSpace(c)) {
          buf.append(c);
          state = State.VAL;
        }
      }
    }
    
    if(state == State.VAL) {
      tokenList.add(new Token(TokenType.VALUE, buf.toString()));
      buf.setLength(0);
    }
  }
  
  boolean isTrailingBackSlash(String line, int next) {
    int len = line.length();
    for(int i = next; i < len; i++) {
      char c = line.charAt(i);
      if(!isWhiteSpace(c)) 
        return false;
    }
    return true; 
  }

  boolean isWhiteSpace(char c) {
    switch (c) {
    case ' ':
    case '\t':
      return true;
    default:
      return false;
    }
  }

  boolean isNonWhiteSpaceSeparator(char c) {
    switch (c) {
    case ':':
    case '=':
      return true;
    default:
      return false;
    }
  }
}
