package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDelete {

  public static void main(String[] args) {

    try (Stream<Path> stream = Files.list(Path.of("src/test/resources/testdata"))) {
      List<Path> paths = stream.collect(Collectors.toList());

      for (Path path : paths) {
        if (Files.getLastModifiedTime(path).toInstant()
            .isBefore(ZonedDateTime.now().minusDays(7).toInstant())) {
          Files.deleteIfExists(path);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
