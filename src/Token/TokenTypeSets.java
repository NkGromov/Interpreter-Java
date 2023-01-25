package Token;


public enum TokenTypeSets{
  OPERATIONS(new TokenTypeList[]{TokenTypeList.PLUS,TokenTypeList.MINUS,TokenTypeList.MULTIPLICATION,TokenTypeList.DIVISION}),
  LOGICOPERATIONS(new TokenTypeList[]{TokenTypeList.LTEQ,TokenTypeList.GTEQ,TokenTypeList.LT,TokenTypeList.GT,TokenTypeList.DOABLEASSIGN,TokenTypeList.NOTASSIGN}),
  LOGICKEYWORDS(new TokenTypeList[]{TokenTypeList.IF,TokenTypeList.ELSE});

  private TokenTypeList[] sets;

  TokenTypeSets(TokenTypeList[] sets) {
    this.sets = sets;
  }

  public TokenTypeList[] getSets() {
    return sets;
  }
}