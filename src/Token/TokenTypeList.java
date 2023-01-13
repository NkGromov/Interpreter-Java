package Token;

public enum TokenTypeList {
  NUMBER(new TokenType("NUMBER", "[0-9]+")),
  VARIABLE(new TokenType("VARIABLE", "[а-я]+")),
  SEMICOLON(new TokenType("SEMICOLON", ";")),
  SPACE(new TokenType("SPACE", "[ \\n\\t\\r]")),
  ASSIGN(new TokenType("ASSIGN", "=")),
  LOG(new TokenType("LOG", "КОНСОЛЬ")),
  PLUS(new TokenType("PLUS", "\\+")),
  MINUS(new TokenType("MINUS", "\\-")),
  LPAR(new TokenType("LPAR", "\\(")),
  RPAR(new TokenType("RPAR", "\\)"));

  private TokenType type;
  
  TokenTypeList(TokenType type) {
    this.type = type;
  }

 

  public TokenType getType() {
    return this.type;
  }
}