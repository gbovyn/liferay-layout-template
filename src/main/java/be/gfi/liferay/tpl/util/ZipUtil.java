package be.gfi.liferay.tpl.util;

import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Map;

public class ZipUtil {

	public static Try<Path> getFileFromZip(final Path zipPath, final String filename) {
		return Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs ->
				fs.getPath(filename)
		);
	}

	public static Try<BasicFileAttributes> getFileAttributesFromZip(final Path zipPath, final String filename) {
		return Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs ->
				Files.readAttributes(fs.getPath(filename), BasicFileAttributes.class)
		);
	}

	public static Try<String> readFileFromZip(final Path zipPath, final String filename) {
		final Try<Try<String>> readTry = Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs ->
				Try.withResources(() -> Files.newBufferedReader(fs.getPath(filename))).of(
						IOUtils::toString
				)
		);

		if (readTry.isFailure()) {
			return Try.failure(readTry.getCause());
		}

		return readTry.get();
	}

	public static Try<Path> writeFileToZip(final Path zipPath, final String filename, final String content) {
		return Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs ->
				Files.write(
						fs.getPath(filename), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING
				)
		);
	}

	public static Try<Boolean> deleteFileFromZip(final Path zipPath, final String filename) {
		return Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs ->
				Files.deleteIfExists(
						fs.getPath(filename)
				)
		);
	}

	/*
	 *   Private methods
	 */
	private static FileSystem getZipAsFileSystem(final Path zipPath) throws IOException {
		return FileSystems.newFileSystem(
				getUriToJar(zipPath),
				getFileSystemEnv()
		);
	}

	private static URI getUriToJar(final Path zipPath) {
		return URI.create(
				"jar:" + zipPath.toUri()
		);
	}

	private static Map<String, String> getFileSystemEnv() {
		return Collections.singletonMap(
				"create", "false"
		);
	}
}