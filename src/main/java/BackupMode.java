public enum BackupMode {
    NONE("None"),
    CONSECUTIVE("Consecutive"),
    NEW("New"),
    UPDATING("Updating");

    public final String label;

    @Override
    public String toString() {
        return this.label;
    }

    BackupMode(String label) {
        this.label = label;
    }
}
