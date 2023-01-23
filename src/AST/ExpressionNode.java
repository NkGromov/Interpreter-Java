package AST;

import java.util.Map;

import Parser.FunctionDefiniton;

public class ExpressionNode {
  public Integer applyNode(Map<String, Integer> scope) {return 0;}
  public void applyNode(Map<String, Integer> scope,Map<String, FunctionDefiniton> fnDefinitions) {}
}
