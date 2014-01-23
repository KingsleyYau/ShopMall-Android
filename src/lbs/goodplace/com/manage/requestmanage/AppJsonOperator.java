/**
 * 
 */
package lbs.goodplace.com.manage.requestmanage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.gau.utils.net.operator.IHttpOperator;
import com.gau.utils.net.request.THttpRequest;
import com.gau.utils.net.response.BasicResponse;
import com.gau.utils.net.response.IResponse;

/**
 * 
 * <br>类描述:将数据流转化为ｊｓｏｎ
 * <br>功能详细描述:
 * 
 * @author  licanhui
 * @date  [2013-3-1]
 */
public class AppJsonOperator implements IHttpOperator {

	@Override
	public IResponse operateHttpResponse(THttpRequest request, HttpResponse response)
			throws IllegalStateException, IOException {
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		JSONObject json = parseMsgListStreamData(is);
		BasicResponse iResponse = new BasicResponse(IResponse.RESPONSE_TYPE_JSONOBJECT, json);
		return iResponse;
	}
	
	
	public static JSONObject parseMsgListStreamData(final InputStream in) {
		if (in == null) {
			return null;
		}
		try {
			String jsonString = null;
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len = -1;
			try {
				while ((len = in.read(buff)) != -1) {
					buffer.write(buff, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			byte[] data = buffer.toByteArray();
			jsonString = new String(data);

	
			if (jsonString != null) {
				Log.i("lch", "请求返回数据：" + jsonString); 
				JSONObject jsonObject = new JSONObject(jsonString);
				return jsonObject;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("lch", String.valueOf(e.toString()));
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}
}
