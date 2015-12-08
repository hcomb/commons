package eu.hcomb.common.swagger;


public class SwaggerConfig {

	private String resourcePackages;
	private String baseUrl;

	public void setResourcePackages(String resourcePackages) {
		this.resourcePackages = resourcePackages;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getResourcePackages() {
		return resourcePackages;
	}
	
}
