package Token;


public enum TokenTypeSets{
  OPERATIONS(new TokenTypeList[]{TokenTypeList.PLUS,TokenTypeList.MINUS,TokenTypeList.MULTIPLICATION,TokenTypeList.DIVISION});

  private TokenTypeList[] sets;

  TokenTypeSets(TokenTypeList[] sets) {
    this.sets = sets;
  }

  public TokenTypeList[] getSets() {
    return sets;
  }
}