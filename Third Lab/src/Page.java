/**
 * This is the class page which simulates the pages in the main memory and disk
 * */

public class Page {
    private Integer variableId;
    private Variable variable;

    public Page() {
        this.variableId = null;
        this.variable = null;
    }
    public Page (Integer variableId, Variable variable){
        this.variableId = variableId;
        this.variable = variable;
    }

    public Integer getVariableId() {
        return variableId;
    }

    public void setVariableID(Integer variableID) {
        this.variableId = variableID;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}
