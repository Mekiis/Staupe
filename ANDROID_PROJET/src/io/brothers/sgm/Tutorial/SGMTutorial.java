package io.brothers.sgm.Tutorial;

import android.os.Bundle;
import java.util.List;
import io.brothers.sgm.Tools.NoDuplicatesList;

public class SGMTutorial<Params> {
    private List<SGMTutorialStep> steps;
    private SGMTutorialStep stepActual = null;
    private TutorialListener listener = null;

    public interface TutorialListener{
        public abstract void firstStepLaunch();

        public abstract void stepFinished();

        public abstract void sequenceFinished();
    }

    public SGMTutorial(TutorialListener listener){
        this.listener = listener;
    }

    public void startTutorial(){
        this.listener.firstStepLaunch();
    }

    public void setSteps(List<SGMTutorialStep> steps) {
        this.steps = new NoDuplicatesList<>();
        this.stepActual = null;

        if(steps != null){
            this.steps = steps;
            this.stepActual = findNextStepId(null);
        }
    }

    public void addStep(SGMTutorialStep step){
        if(this.steps == null)
            this.steps = new NoDuplicatesList<>();
        this.stepActual = null;

        if(step != null){
            this.steps.add(step);
            this.stepActual = findNextStepId(null);
        }
    }

    private SGMTutorialStep findNextStepId(SGMTutorialStep stepActual) {
        SGMTutorialStep nextStepId = stepActual;

        for( SGMTutorialStep stepId : this.steps ){
            if(nextStepId == null || stepId.getId() < nextStepId.getId()){
                nextStepId = stepId;
            }
        }

        return nextStepId;
    }

    public void sendAction(Bundle paramsResult, Params... paramsCondition){
        boolean areAllConditionsValidated = true;

        for (SGMTutorialACondition condition : this.stepActual.getConditions()){
            condition.sendAction(paramsCondition);
            if(!condition.isValidate())
                areAllConditionsValidated = false;
        }

        if(areAllConditionsValidated){
            SGMTutorialStep step = findNextStepId(this.stepActual);
            if(this.stepActual.getId() == step.getId()){
                this.listener.sequenceFinished();
            } else {
                for (SGMTutorialAResult result : this.stepActual.getResults()){
                    result.sendAction(paramsResult);
                }
                this.listener.stepFinished();
                this.stepActual = step;
            }
        }
    }
}
