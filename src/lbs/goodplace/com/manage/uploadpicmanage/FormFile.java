package lbs.goodplace.com.manage.uploadpicmanage;


import java.io.InputStream;

public class FormFile {

	// 定义了使用的文件的特点
	// 上传文件的数据
	private byte[] data;
	private InputStream inStream;
	// 文件名称
	private String fileName = "ufile";
	// 请求参数名称
	private String formName;
	// 内容类型
	private String contentType = "application/octet-stream";
	public FormFile(byte[] data, String fileName, String formName, String contentType) {
		this.data = data;
		this.fileName = fileName;
		this.formName = formName;
		this.contentType = contentType;
	}
	public FormFile(InputStream inStream, String fileName, String formName, String contentType) {
		this.inStream = inStream;
		this.fileName = fileName;
		this.formName = formName;
		this.contentType = contentType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public InputStream getInStream() {
		return inStream;
	}
	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	} 
}