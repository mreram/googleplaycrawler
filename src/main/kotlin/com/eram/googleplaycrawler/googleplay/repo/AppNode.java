package com.eram.googleplaycrawler.googleplay.repo;


import java.io.File;

/**
 * Models files that belong to an App
 * @author patrick
 *
 */
public abstract class AppNode extends AbstractNode {

	/**
	 * App identifier
	 */
	protected final String packageName;
	
	/**
	 * App identifier
	 */
	protected final int versionCode;

	public AppNode(Layout layout, String packageName, int versionCode) {
		super(layout);
		this.packageName = packageName;
		this.versionCode = versionCode;
	}
	

	public final File resolve() {
		return new File(resolveContainer(), getFileName());
	}


	public final File resolveContainer() {
		return new File(layout.appsDir, packageName);
	}

	/**
	 * Compute the filename
	 * 
	 * @return the name under which the file is to be stored.
	 */
	public abstract String getFileName();
}
