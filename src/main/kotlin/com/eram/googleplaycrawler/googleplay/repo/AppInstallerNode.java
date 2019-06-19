package com.eram.googleplaycrawler.googleplay.repo;

/**
 * Represents an APK file
 * 
 * @author patrick
 * 
 */
public class AppInstallerNode extends AppNode {

	public AppInstallerNode(Layout layout, String packageName, int versionCode) {
		super(layout, packageName, versionCode);
	}

	@Override
	public String getFileName() {
		return packageName + "-" + versionCode + ".apk";
	}

}
