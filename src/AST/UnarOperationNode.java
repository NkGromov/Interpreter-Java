package AST;

import java.util.Map;

import Token.Token;

public class UnarOperationNode extends ExpressionNode {
  private Token operator;
  private ExpressionNode operand;

  public UnarOperationNode(Token operator, ExpressionNode operand){
    this.operator = operator;
    this.operand = operand;
  }

  public Integer applyNode(Map<String, Integer> scope){
    System.out.println(this.operand.applyNode(scope));
    return this.operand.applyNode(scope);
  }

  public ExpressionNode getOperand() {
    return operand;
  }

  public Token getOperator() {
    return operator;
  }
  
}
