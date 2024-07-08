package back.api.business;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final String currentWorkingDirectory = System.getProperty("user.dir");

    public Resource getImage(String name) {
        return new FileSystemResource(currentWorkingDirectory+ "/images/" + name);
    }

    public void saveImage(String name, byte[] image) {
        try {
            Files.write(Paths.get(currentWorkingDirectory + "/images/" + name), image);
        } catch (IOException e) {
            throw new RuntimeException("Error saving image");
        }
    }
}
