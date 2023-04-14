import java.io.File;

public abstract class Container {
    private File file;


    public Container (File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
