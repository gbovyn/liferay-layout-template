package be.gfi.liferay.tpl.model;

public class LayoutTemplate {

	private final String id;
	private final String name;
	private final String templatePath;
	private final String thumbnailPath;

	public LayoutTemplate(final String id, final String name, final String templatePath, final String thumbnailPath) {
		this.id = id;
		this.name = name;
		this.templatePath = templatePath;
		this.thumbnailPath = thumbnailPath;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}
}
