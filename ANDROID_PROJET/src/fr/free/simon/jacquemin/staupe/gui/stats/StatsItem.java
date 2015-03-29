package fr.free.simon.jacquemin.staupe.gui.stats;

//another class to handle item's id and name
public class StatsItem {
    public String id = "";
    public String name = "";
    public String desc = "";
    public String value = "";

    public StatsItem(String id, String name, String desc, String value) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.value = value;
    }
}