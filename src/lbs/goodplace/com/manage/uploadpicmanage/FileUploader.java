package lbs.goodplace.com.manage.uploadpicmanage;



import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import android.content.Context;
import android.os.Environment;

public class FileUploader {
	private String imgSavePath;
	
	/**
	 * 上传图片
	 * @param url
	 * @param fileNames
	 * @param requestJson
	 * @param context
	 * @param callBack
	 * @return
	 */
    public Boolean UploadPic(String url, List<FormFile> formFiles,Map<String, Object> postdata,
			Context context, IParser iparser,IDataListener dataListener){
    	
    	String requsestBase = "";
//		try {
//			long timestamp = 1232132;//requestJson.getLong(MyConstant.TIMESTAMP); //暂时不用,假数据
//			String base64Json = new String(Base64.encode(requestJson.toString().getBytes(HTTP.UTF_8)));
//			String sign = MyMethods.MD5(base64Json.toString());
		
			// 请求参数base64加密
//			requsestBase = 	"&bbb="
//							+ "1"	
//							+ "&params="
//							+ base64Json
//							+ "&sign="
//							+ sign
//							+ "&" + MyConstant.TIMESTAMP + "="
//							+ String.valueOf(timestamp);
			
//			System.out.println("上传图片 BASE64 JSON:" + requsestBase);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
    	
    	for (Object o : postdata.keySet()) {  
//    		System.out.println("key=" + o + " value=" + postdata.get(o)); 
    		requsestBase += ( "&" + o + "=" + postdata.get(o));
    	} 
    	System.out.println("请求参数 :" + requsestBase);
    	
    	//测试只是一个文件
		//文件(在配置文件   打开文件操作权限)
//		String path = Environment.getExternalStorageDirectory().toString()+ "/GSMobile";
//		System.out.println("图片保存路径:" + path);
//        File out = new File(path);
//		if (!out.exists()) { 
//			out.mkdirs();
//		}
		
		//读取文件
//		List formFiles = new ArrayList<FormFile>();
//		for(int i = 0 ; i < fileNames.size(); i ++)
//		{
//			imgSavePath = path + "/" + fileNames.get(i);
//			File file= new File(this.imgSavePath);
//			FileInputStream fileStream = null;
//			try {
//				fileStream = new FileInputStream(file);
//			} 
//			catch (FileNotFoundException ex) {
//				//MessageHelper.Alert(EvidenceActivity.this,"读取图片文件失败，请重试！");
//				return false;
//			}
//			FormFile formFile = new FormFile(fileStream,file.getName(),"","image/pjpeg");
//			formFiles.add(formFile);
//		}

		
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("json", "0");
		
    	try
    	{
    		RequestWithFormFile pf = new RequestWithFormFile(context, url, postdata, formFiles, true, iparser,dataListener);	//url + requsestBase
    		pf.execute();
        }
    	catch (Exception e){
    		System.out.println("FileUploader.UploadPic Exception:" + e.toString());
    		return false;
    	}
    	return true;
	    
    }
    
//	/**
//	 * 取证上传
//	 * @param url
//	 * @param fileNames
//	 * @param requestJson
//	 * @param context
//	 * @param callBack
//	 */
//	public void Upload(String url, String[] fileNames,JSONObject requestJson,
//			Context context, UICallBackDao callBack)
//	{
//		MultipartEntity reqEntity = new MultipartEntity();
//		
//		//其它参数
////		StringBody useridSB = null;
////		StringBody executeIdSB = null;
////		StringBody descriptionSB = null;
////		try
////		{
////			useridSB = new StringBody(userid);
////			executeIdSB = new StringBody(executeId);
////			descriptionSB = new StringBody(description);
////		}
////		catch(Exception e1)
////		{
////			
////		}
//		
//		//测试只是一个文件
//		//文件(在配置文件   打开文件操作权限)
//		String path = Environment.getExternalStorageDirectory().toString()+ "/GSMobile";
//		System.out.println("图片保存路径:" + path);
//        File out = new File(path);
//		if (!out.exists()) { 
//			out.mkdirs();
//			}
//		
////		imgSavePath = Environment.getExternalStorageDirectory().toString()+ "/GSMobile/upload.jpg";
//		for(int i = 0 ; i < fileNames.length; i ++)
//		{
//			imgSavePath = path + "/" + fileNames[i];
//			File f = new File(this.imgSavePath);
//			FileBody file = new FileBody(f);
//			reqEntity.addPart("file", file);
//		}
//
//		//封装参数
////		reqEntity.addPart("UserID",useridSB);
////		reqEntity.addPart("ExecuteID",executeIdSB);
////		reqEntity.addPart("Description",descriptionSB);
//		
//		String requsestBase = "";
//		try {
//			long timestamp = 1232132;//requestJson.getLong(MyConstant.TIMESTAMP); //暂时不用,假数据
//			String base64Json = new String(Base64.encode(requestJson.toString().getBytes(HTTP.UTF_8)));
//			String sign = MyMethods.MD5(base64Json.toString());
//		
//		// 请求参数base64加密
//			requsestBase = 	"&bbb="
//							+ "1"	
//							+ "&params="
//							+ base64Json
//							+ "&sign="
//							+ sign
//							+ "&" + MyConstant.TIMESTAMP + "="
//							+ String.valueOf(timestamp);
//			
//			System.out.println("上传图片 BASE64 JSON:" + requsestBase);
//			
//			StringBody jsonSB = new StringBody(requsestBase);
//			reqEntity.addPart("jsonstr",jsonSB);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//		RequestWithFile requestDaoWithImg=new RequestWithFile(context,url + requsestBase,reqEntity,true,callBack);
//	    requestDaoWithImg.execute();
//	}
//	
//	/**
//	 * 
//	 * @param url
//	 * @param fileNames
//	 * @param userid
//	 * @param executeId
//	 * @param description
//	 * @param context
//	 * @param callBack
//	 */
//	public void Upload(String url, String[] fileNames,String userid,String executeId,String description,
//			Context context, UICallBackDao callBack)
//	{
//		MultipartEntity reqEntity = new MultipartEntity();
//		
//		//其它参数
//		StringBody useridSB = null;
//		StringBody executeIdSB = null;
//		StringBody descriptionSB = null;
//		try
//		{
//			useridSB = new StringBody(userid);
//			executeIdSB = new StringBody(executeId);
//			descriptionSB = new StringBody(description);
//		}
//		catch(Exception e1)
//		{
//			
//		}
//		
//		//测试只是一个文件
//		//文件(在配置文件   打开文件操作权限)
//		String path = Environment.getExternalStorageDirectory().toString()+ "/GSMobile";
//		System.out.println("图片保存路径:" + path);
//        File out = new File(path);
//		if (!out.exists()) { 
//			out.mkdirs();
//			}
//		
////		imgSavePath = Environment.getExternalStorageDirectory().toString()+ "/GSMobile/upload.jpg";
//		for(int i = 0 ; i < fileNames.length; i ++)
//		{
//			imgSavePath = path + "/" + fileNames[i];
//			File f = new File(this.imgSavePath);
//			FileBody file = new FileBody(f);
//			reqEntity.addPart("file", file);
//		}
//
//		//封装参数
//		reqEntity.addPart("UserID",useridSB);
//		reqEntity.addPart("ExecuteID",executeIdSB);
//		reqEntity.addPart("Description",descriptionSB);
//		
////		long timestamp = 1232132;//requestJson.getLong(MyConstant.TIMESTAMP); //暂时不用,假数据
////		String sign = MyMethods.MD5(requestJson.toString());
////		
////		// 请求参数base64加密
////		try {
////			String requsestBase = 	"params="
////									+ new String(Base64.encode(requestJson.toString().getBytes(HTTP.UTF_8)))
////									+ "&sign="
////									+ sign
////									+ "&" + MyConstant.TIMESTAMP + "="
////									+ String.valueOf(timestamp);
////			
////			StringBody jsonSB = new StringBody(requsestBase);
////			reqEntity.addPart("params",jsonSB);
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
//
//		RequestWithFile requestDaoWithImg=new RequestWithFile(context,url,reqEntity,true,callBack);
//	    requestDaoWithImg.execute();
//	}
}

