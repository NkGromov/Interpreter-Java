package Token;

public class Token {
  private int position;
  private TokenType token;
  private String text;

  public Token(int position, String text, TokenType token ){
    this.position = position;
    this.text = text;
    this.token = token;
  }

  public int getPosition(){
    return this.position;
  }

  public TokenType getToken(){
    return this.token;
  }

  public String getText(){
    return this.text;
  }
}
