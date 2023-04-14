import java.io.File;
import java.util.ArrayList;

public class DirectoryContainer extends Container{
    private ArrayList<Container> files;


    public DirectoryContainer(File file) {
        super(file);
        this.files = new ArrayList<>();

    }

    public ArrayList<Container> getFiles() {
        return files;
    }
}
