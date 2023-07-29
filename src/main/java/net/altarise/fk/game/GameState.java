package net.altarise.fk.game;

public class GameState {


    private int state;
    private boolean isDay = true;

    public GameState() {
        this.state = 0;
    }

    public void updateState() {
        state++;
    }

    public boolean isState(int stage) {
        return this.state == stage;
    }

    public int getState() {
        return state;
    }

    public int valueOf(String stage) {
        int i;
        switch (stage) {
            case "preparation":
                i = 1;
                break;
            case "pvp":
                i = 2;
                break;
            case "assault":
                i = 3;
                break;
            case "deathmatch":
                i = 4; break;
            default:
                i = 0; break;
        } return i;
    }

    public String getStrState() {
        return getStrStage(state);
    }

    public String getStrStage(int stage) {
        String val = ""; switch (stage) {
            case 0:
                val = "Waiting"; break;
            case 1:
                val = "Préparation"; break;
            case 2:
                val = "PVP"; break;
            case 3:
                val = "Assauts"; break;
            case 4:
                val = "DeathMatch"; break;
        } return val;
    }

    public String getEmote() {
        return getEmote(state);
    }

    public String getEmote(int stage) {
        String val = ""; switch (stage) {
            case 0:
            case 1:
                val = "⌛";
                break;
            case 2:
                val = "⚔";
                break;
            case 3:
                val = "⚡";
                break;
            case 4:
                val = "☠";
                break;
        }
        return val;
    }

    public String getDayNightEmote() {
        if (isDay) return "☀";
        return "☾";
    }

    public void setDay(boolean day) {
        isDay = day;
    }

}


