package lbs.goodplace.com.obj;

import java.io.Serializable;

/**
 * 用户对商家签到情况
 * @author Administrator
 *
 */
public class Signsituation implements Serializable{
	public int total;	//签到总人次
	public String curuser;	//最后签到用户
	public String detail;	//最后留言内容
	public long time; 		//最后签到时间（自1970年1月1日起的秒数）
}