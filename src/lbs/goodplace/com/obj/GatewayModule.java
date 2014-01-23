package lbs.goodplace.com.obj;

/**
 * 服务器（可支持多台）的地址或域名
 * (2.3.1.1)
 * @author Administrator
 *
 */
public class GatewayModule {
	private String name;	//
	private String ip;
	private String port;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
}
