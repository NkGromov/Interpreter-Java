package AST;

import java.util.Map;

import Parser.FunctionDefiniton;
import Token.Token;
import Token.TokenTypeList;

public class UnarOperationNode extends ExpressionNode {
  private Token operator;
  private ExpressionNode operand;

  public UnarOperationNode(Token operator, ExpressionNode operand){
    this.operator = operator;
    this.operand = operand;
  }
  
  @Override
  public Integer applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions){
    if (this.operator.getToken().getName() == TokenTypeList.LOG.getType().getName()) {
      System.out.println(this.operand.applyNode(scope, fnDefinitions));
    }

    return this.operand.applyNode(scope, fnDefinitions);
  }

  public ExpressionNode getOperand() {
    return operand;
  }

  public Token getOperator() {
    return operator;
  }
  
}
