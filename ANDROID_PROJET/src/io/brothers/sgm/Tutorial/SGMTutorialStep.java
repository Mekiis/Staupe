package io.brothers.sgm.Tutorial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 17/02/2015.
 */
public class SGMTutorialStep {
    private int id = -1;
    private List<SGMTutorialACondition> conditions = null;
    private List<SGMTutorialAResult> results = null;

    public SGMTutorialStep(int id, SGMTutorialACondition condition, SGMTutorialAResult result) {
        this.id = id;
        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        addCondition(condition);
        addResult(result);
    }

    public SGMTutorialStep(int id, List<SGMTutorialACondition> conditions, SGMTutorialAResult result) {
        this.id = id;
        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        setCondition(conditions);
        addResult(result);
    }

    public SGMTutorialStep(int id, SGMTutorialACondition condition, List<SGMTutorialAResult> results) {
        this.id = id;
        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        addCondition(condition);
        setResult(results);
    }

    public SGMTutorialStep(int id, List<SGMTutorialACondition> conditions, List<SGMTutorialAResult> results) {
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

    public List<SGMTutorialACondition> getConditions() {
        return conditions;
    }

    public List<SGMTutorialAResult> getResults() {
        return results;
    }

    public void addCondition(SGMTutorialACondition condition){
        if(condition != null)
            this.conditions.add(condition);
    }

    public void addResult(SGMTutorialAResult result){
        if(result != null)
            this.results.add(result);
    }

    public void setResult(List<SGMTutorialAResult> results){
        if(results != null)
            this.results = results;
        else
            this.results.clear();
    }

    public void setCondition(List<SGMTutorialACondition> conditions){
        if(conditions != null)
            this.conditions = conditions;
        else
            this.conditions.clear();
    }
}
