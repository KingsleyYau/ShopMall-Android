package lbs.goodplace.com.manage.uploadpicmanage;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RequestWithFormFile extends AsyncTask {	
	private String tag = "RequestDataTask-->";
	private Context context;
//	private NewGoodsModel pubmodel;
	private String requestURL;
	private boolean isok=false;
	private boolean iSTips = true; //false,不提示;true提示
	private ProgressDialog progressDlg; 
	private IDataListener mDataListener;
//	private MultipartEntity reqEntity;
	private Map<String, Object> params;
	private List<FormFile> files;
	private IParser mIparser;
	
	public RequestWithFormFile(Context context,String url, Map<String, Object> params, List<FormFile> files,boolean iSTips,IParser iparser,IDataListener dataListener){
		this.context = context;
		this.mDataListener = dataListener;
//		this.pubmodel = pubmodel;
		this.iSTips = iSTips;
		this.requestURL =url;
//		this.reqEntity = reqEntity;
		this.params = params;
		this.files = files;
		this.mIparser = iparser;
	}
	
//	public static String postFile(String url, Map<String, String> params, List<FormFile> files) throws IOException {
	public JSONObject getData() {
		  String multipartFormData = "multipart/form-data";	//;charset=UTF-8
	      String end = "\r\n";
	      String twoHyphens = "--";
	      String boundary = "----------8ce56f421821200";
	        
	      try
			{
			  URL uri = new URL(this.requestURL);
			  HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			  //conn.setReadTimeout(5 * 1000); // 缓存的最长时间
			  conn.setDoInput(true);// 允许输入
			  conn.setDoOutput(true);// 允许输出
			  conn.setUseCaches(false); // 不允许使用缓存
			  conn.setRequestMethod("POST");
			  conn.setRequestProperty("Connection", "keep-alive");
			  conn.setRequestProperty("Expect", "100-continue");
			  conn.setRequestProperty("Charsert", "UTF-8");
			  conn.setRequestProperty("Content-Type", multipartFormData + "; boundary=" + boundary);
			  
			  DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			  
			  // 首先组拼文本类型的参数
			  StringBuilder sb ;  
			  if (params != null){
				  sb = new StringBuilder();  
				  for (Map.Entry<String, Object> entry : params.entrySet()) {
					  sb.append(twoHyphens + boundary + end);
					  sb.append("Content-Disposition:form-data; name=\"" + entry.getKey() + "\"" + end + end);
					  sb.append(entry.getValue().toString() + end);
				  }
				  if (sb.length()>0){
//					  String str = URLEncoder.encode(sb.toString(), "UTF-8"); 
					  String str = sb.toString();
					  outStream.write(str.getBytes());
				  }
					  
			  }
			  if (files != null && files.size()>0){
				  // 发送文件数据
				  for (FormFile file : files) {
					  sb = new StringBuilder();
					  sb.append(twoHyphens + boundary + end);
					  sb.append("Content-Disposition: form-data; name=\"" + file.getFormName()+"\"; filename=\"" + file.getFileName()+"\"" + end);
					  sb.append("enctype=\""+ multipartFormData + "\"" + end);
					  sb.append("Content-Type: " + file.getContentType() + end + end);
					  
					  Log.i("zjj", "上传图片 FORMFILE:" + sb.toString());
					  
					  if (sb.length()>0)
						  outStream.write(sb.toString().getBytes());
	
					  // 先判断formfile里面是否为空 如果不为空的话则写出 获取formfile的data里面的
					  if (file.getInStream() != null) {
						  // 提供流的的方式的话就是一边读一边写了
						  byte[] buffer = new byte[1024];
						  int len = 0;
						  while ((len = file.getInStream().read(buffer)) != -1) {
							  outStream.write(buffer, 0, len);
						  }
						  file.getInStream().close();
					  } 
					  else {
						  outStream.write(file.getData(), 0, file.getData().length);
					  }
					  outStream.write(end.getBytes());
				  }
				  byte[] end_data = (twoHyphens + boundary + twoHyphens + end + end).getBytes();
				  outStream.write(end_data);
			  }else{
				  byte[] end_data = (twoHyphens + boundary + twoHyphens + end + end).getBytes();
				  outStream.write(end_data);
			  }
			  outStream.flush();
			  outStream.close();
			
			  // 得到响应号码
			  int res = conn.getResponseCode();
	
			  switch (res){
			  	  case 200:
			  		  StringBuffer result = null;
					  BufferedReader br = null;
					  br = new BufferedReader(new InputStreamReader((InputStream) conn.getContent(), "UTF-8"));
						result = new StringBuffer();
						String read = br.readLine();
						while (read != null) {
							result.append(read);
							read = br.readLine();
						}
						br.close();
						br = null;
	
					  String resultStr = result.toString();
					  System.out.println("上传图片接口返回：" + resultStr);
						
//						if(resultStr.indexOf(",") > -1)
//							resultStr = resultStr.split(",")[0];
						
						JSONObject resultJson;
						
						try {
//							String resulttamp = new String(Base64.decode(resultStr.getBytes("UTF-8")));
//							System.out.println("解BASE64后JSON：" + resulttamp);
							resultJson = new JSONObject(resultStr);
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
						
					  conn.disconnect();
					  return resultJson;
//			  	  case 302:
//			  		  return null;
			  	  default:
//			  		throw new RuntimeException("请求失败 ");
			  		return null;
			  }
		}
		catch(Exception ex)
		{
			System.out.println("上传图片接口 Exception：" + ex.toString());
			return null;
		}
	}
	
	@Override
	protected Object doInBackground(Object... params) {
	   	return getData();
	}
	@Override
	protected void onPostExecute(Object result) { 
	   	if(iSTips){
	   		progressDlg.dismiss();
	   	}
		if(this.mIparser != null && this.mDataListener != null){
//			   uicallback.callBack(result); 
			 //判断是否需要解析
				if (mIparser != null) {
					Object object = mIparser.parser((JSONObject)result);
					mDataListener.loadFinish(true, object);
				}else {
					mDataListener.loadFinish(true, result);
				}
		}else{
			showToast("数据提交失败");
		}
	} 

	protected void onPreExecute() { 
	   super.onPreExecute();
	   if(iSTips){
		  progressDlg = new ProgressDialog(context); 
		  progressDlg.setMessage("请稍等..."); 
		  progressDlg.setCancelable(true); 
		  progressDlg.show(); 
	   }
	}
	private void showToast(String mesg) {
		Toast toast = Toast.makeText(context,mesg,Toast.LENGTH_LONG);
		toast.show();
	}
}

