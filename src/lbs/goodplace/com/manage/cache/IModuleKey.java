/*
 * 文 件 名:  IModuleKey.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-10-17
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;

import java.util.List;
import java.util.Map;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-10-17]
 */
public interface IModuleKey {
	
	String buildKey(String module, String extra);
	
	void saveModuleKey(String module, String extra);
	
	/** <br>功能简述:批量保存key值
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param module
	 * @param extraList
	 */
	void saveModuelKey(String module, List<String> extraList);

	/** <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param module
	 * @return
	 */
	List<String> getModuleKeyList(String module);

	/** <br>功能简述:获取键值对映射表
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param module
	 * @return
	 */
	Map<String, String> getModuleKeyMap(String module);

	void clearModuleKeyList(String module);

	void clearModuleKey(String module, String key);
	
	/** <br>功能简述:设置键值是否需要加密
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param encrypt
	 */
	void setKeyNeedEncrypt(boolean encrypt);
	
	boolean getKeyNeedEncrypt();
}
