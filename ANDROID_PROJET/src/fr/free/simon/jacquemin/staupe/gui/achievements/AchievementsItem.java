package fr.free.simon.jacquemin.staupe.gui.achievements;

//another class to handle item's id and name
public class AchievementsItem {
    public String id = "";
    public String name = "";
    public String desc = "";
    public boolean isComplete = false;
    public float percentCompletion = 0f;

    public AchievementsItem(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.isComplete = true;
    }

    public AchievementsItem(String id, String name, String desc, float percentCompletion) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.isComplete = false;
        this.percentCompletion = percentCompletion;
    }

    public AchievementsItem(String id, String name, String desc, boolean isComplete, float percentCompletion) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.isComplete = isComplete;
        this.percentCompletion = percentCompletion;
    }
}