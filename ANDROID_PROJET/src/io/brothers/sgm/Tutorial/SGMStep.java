package io.brothers.sgm.Tutorial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 17/02/2015.
 */
public class SGMStep {
    private int id = -1;
    private List<SGMACondition> conditions = null;
    private List<SGMAResult> results = null;

    public SGMStep(int id, SGMACondition condition, SGMAResult result) {
        this.id = id;
        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        addCondition(condition);
        addResult(result);
    }

    public SGMStep(int id, List<SGMACondition> conditions, SGMAResult result) {
        this.id = id;
        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        setCondition(conditions);
        addResult(result);
    }

    public SGMStep(int id, SGMACondition condition, List<SGMAResult> results) {
        this.id = id;
        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        addCondition(condition);
        setResult(results);
    }

    public SGMStep(int id, List<SGMACondition> conditions, List<SGMAResult> results) {
        this.id = id;
        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        setCondition(conditions);
        setResult(results);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SGMACondition> getConditions() {
        return conditions;
    }

    public List<SGMAResult> getResults() {
        return results;
    }

    public void addCondition(SGMACondition condition){
        if(condition != null)
            this.conditions.add(condition);
    }

    public void addResult(SGMAResult result){
        if(result != null)
            this.results.add(result);
    }

    public void setResult(List<SGMAResult> results){
        if(results != null)
            this.results = results;
        else
            this.results.clear();
    }

    public void setCondition(List<SGMACondition> conditions){
        if(conditions != null)
            this.conditions = conditions;
        else
            this.conditions.clear();
    }
}
