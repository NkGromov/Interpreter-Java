package AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Parser.FunctionDefiniton;

public class ConditionNode extends ExpressionNode  {
  private List<CompareNode> compares = new ArrayList<>();

  public ConditionNode(List<CompareNode> compares){
    this.compares = compares;
  }

  @Override
  public Integer applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions) {
    Integer result = 0;
    for (int i = 0; i < this.compares.size(); i++) {
      List<ExpressionNode> body = compares.get(i).getBody();
      if(compares.get(i).applyNode(scope, fnDefinitions) == 1){
        for (ExpressionNode node : body) result = node.applyNode(scope, fnDefinitions);
        break;
      }
    }
    return result;
  }

  public List<CompareNode> getCompares() {
    return compares;
  }
}
