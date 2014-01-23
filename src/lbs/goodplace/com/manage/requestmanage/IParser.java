package lbs.goodplace.com.manage.requestmanage;

import org.json.JSONObject;

/**
 * 数据解析接口，负责解析数据
 * @author zhoujun
 *
 */
public interface IParser {

	public Object parser(JSONObject json) ;
}
