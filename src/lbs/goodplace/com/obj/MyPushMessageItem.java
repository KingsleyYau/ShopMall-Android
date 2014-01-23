package lbs.goodplace.com.obj;


public class MyPushMessageItem{
	public String alert = "";
	public int badge = 0;
	public String sound = "";	
	public int etype = 0;
	
	public int typeid = 0;
	public String id = "";
	
//	public MyPushMessageItem(){
//		
//	}
//	public MyPushMessageItem(String packageName,int badge,String sound,int etype){
//		this.alert = packageName ;
//		this.badge = badge ;
//		this.sound = sound ;
//		this.etype = etype;
//	}
//	private void readFromParcel(Parcel in) {
//		alert = in.readString();
//		badge = in.readInt();
//		sound = in.readString();		
//		etype = in.readInt();
//	}
//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		// TODO Auto-generated method stub
//		dest.writeString(alert);	
//		dest.writeInt(badge);		
//		dest.writeString(sound);	
//		dest.writeInt(etype);
//	}
//	
//	public static final Parcelable.Creator<MyPushMessageItem> CREATOR  
//	= new Parcelable.Creator<MyPushMessageItem>(){
//		@Override
//		public MyPushMessageItem createFromParcel(Parcel source){
//			MyPushMessageItem item = new MyPushMessageItem(); 
//			item.readFromParcel(source);
//			return item;
//		}			
//		public MyPushMessageItem[] newArray(int size){  
//	        return new MyPushMessageItem[size];  
//	    } 
//	}; 
}
