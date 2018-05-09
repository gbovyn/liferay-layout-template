package be.gfi.liferay.tpl.util;

import be.gfi.liferay.tpl.model.LayoutTemplate;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.xml.Element;
import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LayoutTemplateUtil {

	private static final String WEB_INF = "WEB-INF";
	private static final String XML_CONFIG = "liferay-layout-templates.xml";
	private static final String PROPERTIES_FILE = "liferay-plugin-package.properties";

	private static final String OSGI_WAR = "osgi/war";
	private static final String LAYOUT_TEMPLATE = "layout-template";


	public static Try<List<LayoutTemplate>> getCustomLayoutTemplates(final String warName) {
		final Try<Element> customElement = XmlUtil.getCustomElement(getLayoutTemplatesXml(warName));

		if (customElement.isFailure()) {
			return Try.failure(customElement.getCause());
		}

		final List<LayoutTemplate> layoutTemplates = new ArrayList<>(0);

		customElement.get().elements("layout-template").forEach(element -> {
			final String id = element.attribute("id").getValue();
			final String name = element.attribute("name").getValue();
			final String templatePath = element.element("template-path").getStringValue();
			final String thumbnailPath = element.element("thumbnail-path").getStringValue();

			layoutTemplates.add(
					new LayoutTemplate(id, name, templatePath, thumbnailPath)
			);
		});

		return Try.success(layoutTemplates);
	}

	public static Try<String> getLayoutTemplateContent(final String warName, final String layoutTemplatePath) {
		final Try<InputStream> inputStream = getInputStreamFromZip(warName, layoutTemplatePath);

		return Try.withResources(() -> new InputStreamReader(inputStream.get())).of(
				IOUtils::toString
		);
	}

	public static Try createCustomLayoutTemplate(final String warName, final LayoutTemplate layoutTemplate, final String content) {
		final Try<InputStream> layoutTemplatesXml = getLayoutTemplatesXml(warName);

		if (layoutTemplatesXml.isFailure()) {
			return Try.failure(layoutTemplatesXml.getCause());
		}

		final Try tryToAddXml = XmlUtil.addCustomLayoutTemplate(layoutTemplatesXml, layoutTemplate);

		if (tryToAddXml.isFailure()) {
			return Try.failure(tryToAddXml.getCause());
		}

		final Try trytoAddZip = addFileInZip(getLayoutTemplateWarPath(), layoutTemplate.getTemplatePath(), content);

		if (trytoAddZip.isFailure()) {
			return Try.failure(trytoAddZip.getCause());
		}

		return Try.success(layoutTemplate);
	}

	public static Try<InputStream> getLayoutTemplateTpl(final String warName, final String layoutTemplatePath) {
		return getInputStreamFromZip(warName, layoutTemplatePath);
	}

	private static Try<InputStream> getLayoutTemplatesXml(final String warName) {
		return getInputStreamFromZip(warName, WEB_INF + "/" + XML_CONFIG);
	}

	private static Try<InputStream> getInputStreamFromZip(final String warName, final String filename) {
		final Try<ZipFile> zipFile = getZipFile(warName);

		if (zipFile.isFailure()) {
			return Try.failure(zipFile.getCause());
		}

		final ZipEntry entry = zipFile.get().getEntry(
				filename.contains("WEB-INF") ? filename : filename.substring(1)
		);

		return Try.of(() ->
				zipFile.get().getInputStream(entry)
		);
	}

	public static Try<ZipEntry> getZipEntryFromZip(final String warName, final String filename) {
		final Try<ZipFile> zipFile = getZipFile(warName);

		if (zipFile.isFailure()) {
			return Try.failure(zipFile.getCause());
		}

		return Try.withResources(zipFile::get).of((zip) ->
				zip.getEntry(
						filename.contains("WEB-INF") ? filename : filename.substring(1)
				)
		);
	}

	public static Try<ZipFile> getZipFile(final String warName) {
		return getZipFileByPath(
				getOsgiWarFolder(), warName
		);
	}

	public static Path getOsgiWarFolder() {
		return Paths.get(getLiferayHome(), OSGI_WAR);
	}

	public static String getLiferayHome() {
		return PropsUtil.get(PropsKeys.LIFERAY_HOME);
	}

	private static Try<ZipFile> getZipFileByPath(final Path osgiWarFolder, final String warName) {
		final File file = new File(osgiWarFolder.toString(), warName);

		return Try.of(() ->
				new ZipFile(file)
		);
	}

	public static FileSystem getZipAsFileSystem(final String zipPath) throws IOException {
		return FileSystems.newFileSystem(
				getUriToJar(zipPath),
				getFileSystemEnv()
		);
	}

//	public static Try<FileSystem> getZipAsFileSystem(final String zipPath) {
////		Map<String, String> env = getFileSystemEnv();
////
////		final URI uri = getUriToJar(zipPath);
//
////		final Try<FileSystem> fileSystem = Try.of(() ->
////				FileSystems.getFileSystem(uri)
////		);
////
////		if (fileSystem.isSuccess()) {
////			return fileSystem;
////		}
//
//		return Try.of(() ->
//				FileSystems.newFileSystem(getUriToJar(zipPath), getFileSystemEnv())
//		);
//	}

	public static URI getUriToJar(final String zipPath) {
		return URI.create("jar:" + Paths.get(zipPath).toUri());
	}

	public static Map<String, String> getFileSystemEnv() {
		return Collections.singletonMap("create", "false");
	}

	public static Try deleteFileFromZip(final String zipPath, final String filename) {
		return Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs -> {
			final Path path = fs.getPath(filename);
			Files.deleteIfExists(path);
			return true;
		});
	}

	public static Try addFileInZip(final String zipPath, final String filename, final String newContent) {
		return Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs -> {
			final Path path = fs.getPath(filename);
			Files.write(path, newContent.getBytes());
			return true;
		});
	}

	private static Try<String> readFileInZip(final String zipPath, final String filename) {
		Try.withResources(() -> getZipAsFileSystem(zipPath)).of(fs -> {
			final Path path = fs.getPath(filename);
			return Try.withResources(() -> Files.newBufferedReader(path)).of(
					IOUtils::toString
			);
		});

		return Try.failure(new IOException("Path could not be read"));
	}

	// TODO duplicate code
	private static String getLayoutTemplateWarPath() {
		return Paths.get(
				LayoutTemplateUtil.getOsgiWarFolder().toString(),
				getLayoutTemplateWarName()
		).toString();
	}

	private static String getLayoutTemplateWarName() {
		return "my-liferay-layout-layouttpl.war"; // TODO use configuration
	}
}
