package me.recursiveg.generictools.runtime;

import java.util.Map;

public class ExecutionContext {
    private GenericToolInstance gti;
    private Map<String, ?> variables;
    //private List<Runnable> scheduledTasks;

    public ExecutionContext(GenericToolInstance gti, Map<String, ?> variables) {
        this.gti = gti;
        this.variables = variables;
        //this.scheduledTasks = new ArrayList<>();
    }
}
