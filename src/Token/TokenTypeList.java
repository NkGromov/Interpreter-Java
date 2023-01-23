package Token;

import java.util.ArrayList;

public enum TokenTypeList {
  NUMBER(new TokenType("NUMBER", "[0-9]+")),
  VARIABLE(new TokenType("VARIABLE", "[а-я]+")),
  SEMICOLON(new TokenType("SEMICOLON", ";")),
  SPACE(new TokenType("SPACE", "[ \\n\\t\\r]")),
  ASSIGN(new TokenType("ASSIGN", "=")),
  LOG(new TokenType("LOG", "КОНСОЛЬ")),
  RETURN(new TokenType("RETURN", "ВЕРНУТЬ")),
  PLUS(new TokenType("PLUS", "\\+")),
  MINUS(new TokenType("MINUS", "\\-")),
  MULTIPLICATION(new TokenType("MULTIPLICATION", "\\*")),
  DIVISION(new TokenType("DIVISION", "\\/")),
  LPAR(new TokenType("LPAR", "\\(")),
  RPAR(new TokenType("RPAR", "\\)")),
  LBRACE(new TokenType("LBRACE", "\\{")),
  RBRACE(new TokenType("RBRACE", "\\}")),
  LT(new TokenType("LT", "\\<")),
  GT(new TokenType("GT", "\\>")),
  COMMA(new TokenType("COMMA", "\\,"));

  private TokenType type;
  
  TokenTypeList(TokenType type) {
    this.type = type;
  }

  public TokenType getType() {
    return this.type;
  }
}