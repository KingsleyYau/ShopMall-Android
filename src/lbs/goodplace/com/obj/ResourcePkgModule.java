package lbs.goodplace.com.obj;

/**
 * 资源包
 * (2.3.1.2)
 * @author Administrator
 *
 */
public class ResourcePkgModule {
	private String lastmdate;	//时间戳
	private String name;
	private String url;
	private String verifycode;	//MD5校验码
	public String getLastmdate() {
		return lastmdate;
	}
	public void setLastmdate(String lastmdate) {
		this.lastmdate = lastmdate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVerifycode() {
		return verifycode;
	}
	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}
}
