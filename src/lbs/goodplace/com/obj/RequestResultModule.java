package lbs.goodplace.com.obj;

/**
 * 对只返回成功/失败的接口使用
 * @author Administrator
 *
 */
public class RequestResultModule {
	public boolean result;
	public int errorcode;	//错误代码
	
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	
}
