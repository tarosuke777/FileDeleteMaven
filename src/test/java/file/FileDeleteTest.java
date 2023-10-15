package file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class FileDeleteTest {

  @Test
  void mainTest_ê≥èÌån() throws IOException {

    Path testDirPath = Path.of("src/test/resources/testdata");
    ZonedDateTime now = spy(ZonedDateTime.of(2023, 10, 7, 0, 0, 0, 0, ZoneId.systemDefault()));

    Path testPath1 = testDirPath.resolve("test1.csv");
    Path testPath2 = testDirPath.resolve("test2.csv");
    Path testPath3 = testDirPath.resolve("test3.csv");

    try (Stream<Path> stream = Files.list(testDirPath)) {
      List<Path> paths = stream.collect(Collectors.toList());
      for (Path path : paths) {
        Files.deleteIfExists(path);
      }

      Files.createFile(testPath1);
      Files.setLastModifiedTime(testPath1, FileTime.from(now.minusDays(8).toInstant()));

      Files.createFile(testPath2);
      Files.setLastModifiedTime(testPath2, FileTime.from(now.minusDays(7).toInstant()));

      Files.createFile(testPath3);
      Files.setLastModifiedTime(testPath3, FileTime.from(now.minusDays(6).toInstant()));

    }

    ZonedDateTime deleteDate = now.minusDays(7);
    when(now.minusDays(7)).thenReturn(deleteDate);
    try (MockedStatic<ZonedDateTime> mocked = mockStatic(ZonedDateTime.class)) {
      mocked.when(ZonedDateTime::now).thenReturn(now);
      FileDelete.main(null);
    }

    try (Stream<Path> stream = Files.list(testDirPath)) {
      List<Path> paths = stream.collect(Collectors.toList());
      assertEquals(2, paths.size());
      assertEquals(testPath2, paths.get(0));
      assertEquals(testPath3, paths.get(1));
    }
  }
}
