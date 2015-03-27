package io.brothers.sgm.Tutorial;

import android.os.Bundle;
import java.util.List;
import io.brothers.sgm.Tools.NoDuplicatesList;

public class SGMTutorial<Params> {
    private List<SGMStep> steps;
    private SGMStep stepActual = null;
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

    public void setSteps(List<SGMStep> steps) {
        this.steps = new NoDuplicatesList<>();
        this.stepActual = null;

        if(steps != null){
            this.steps = steps;
            this.stepActual = findNextStepId(null);
        }
    }

    public void addStep(SGMStep step){
        if(this.steps == null)
            this.steps = new NoDuplicatesList<>();
        this.stepActual = null;

        if(step != null){
            this.steps.add(step);
            this.stepActual = findNextStepId(null);
        }
    }

    private SGMStep findNextStepId(SGMStep stepActual) {
        SGMStep nextStepId = stepActual;

        for( SGMStep stepId : this.steps ){
            if(nextStepId == null || stepId.getId() < nextStepId.getId()){
                nextStepId = stepId;
            }
        }

        return nextStepId;
    }

    public void sendAction(Bundle paramsResult, Params... paramsCondition){
        boolean areAllConditionsValidated = true;

        for (SGMACondition condition : this.stepActual.getConditions()){
            condition.sendAction(paramsCondition);
            if(!condition.isValidate())
                areAllConditionsValidated = false;
        }

        if(areAllConditionsValidated){
            SGMStep step = findNextStepId(this.stepActual);
            if(this.stepActual.getId() == step.getId()){
                this.listener.sequenceFinished();
            } else {
                for (SGMAResult result : this.stepActual.getResults()){
                    result.sendAction(paramsResult);
                }
                this.listener.stepFinished();
                this.stepActual = step;
            }
        }
    }
}
