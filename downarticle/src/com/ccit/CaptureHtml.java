package com.ccit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaptureHtml
{
	
  /**
   * 截取中间的字符串
   * @param start  起始的正则
   * @param end    终止的正则
   * @param target 目标字符串
   * @return
   */
	public static String cutMiddleString(String start,String end,String target)
	{
		   String[] sp1=target.split(start);
		   String[] sp2=sp1[1].split(end);
		   
		   return sp2[0];
	}
	
   /**
    * 根据url取出当前页上的每个文章的url集合
    * @param html 当前要解析的html
    * @return
    */
	public static List<String> getUrlByPage(String regx,String html)
	{
		List<String> list=new ArrayList<String>();
	  
	   
	   //String test="							<li><a href=\"http://mil.news.sina.com.cn/2012-02-23/0944682918.html\" target=\"_blank\">两名西方记者在叙利亚遭炮击身亡(图)</a><span class=\"time\">(02月23日 09:44)</span></li>";
	   //String regx="(\\s+<li><a href=\"(http://mil.news.sina.com.cn/2012-\\d{2}-\\d{2}/\\d{3,}.html)\" target=\"_blank\">(.*?)</a><span class=\"time\">[(]\\d{2}月\\d{2}日 \\d{2}:\\d{2}[)]</span></li>)";
	   Pattern pattern=Pattern.compile(regx);
	   Matcher match=pattern.matcher(html);
	   int stindex=0;
	   while(match.find(stindex))
	   {
		   
		   list.add(match.group(2));
		   stindex=match.end();
	   }
	   return list;	
	}
	
	
	/**
	 * 
	 * @param titleRegx
	 * @param contentRegx
	 * @param contenturl
	 * String[] titleRegx,String[] contentRegx,String contenturl
	 */
	public static String[] parseContent(String titleRegx,String startCont,String endCont,String arturl)
	{
		String [] re=new String[2];
		
		String content=getStringByUrl(arturl);
		//String titleRegx="<title>(.*?)</title>";
		Matcher titMatch=Pattern.compile(titleRegx).matcher(content);
        if(titMatch.find())
        {
        	re[0]=titMatch.group(1);
        }
        
        //String startCont="<!--正文内容begin-->\\s+<!-- google_ad_section_start -->\\s+<div class=\"blkContainerSblkCon\" id=\"artibody\">";
       // String endCont="<div\\s+style=\"clear:both;height:0;visibility:hiddden;overflow:hidden;\"></div><style type=\"text/css\">";
        String fcontent=cutMiddleString(startCont,endCont,content);
        re[1]=fcontent;
        
        
        return re;
        
	}	
	
	/**
	 * 将url的数据读成一个字符串
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	public static String getStringByUrl(String pageurl) 
	{
		StringBuilder sb=new StringBuilder();
		try
		{
			URL url=new URL(pageurl);
			URLConnection uc=url.openConnection();
			InputStream is=uc.getInputStream();
		    
			
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
					
			String str=null;
			while(null!=(str=br.readLine()))
			{
				sb.append(str);
			}
	
		} catch (MalformedURLException e)
		{
		System.out.println("URL不存在，或语法不正确");
		} catch (IOException e)
		{
			System.out.println("打开URL出错");
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	

	    public static String delHTMLTag(String htmlStr){ 
	        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	       
	         
	        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        Matcher m_script=p_script.matcher(htmlStr); 
	        htmlStr=m_script.replaceAll(""); //过滤script标签 
	         
	        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        Matcher m_style=p_style.matcher(htmlStr); 
	        htmlStr=m_style.replaceAll(""); //过滤style标签 
	         
	 
	        return htmlStr.trim(); //返回文本字符串 
	    } 
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		 int starxpage=6;
		 int endpage=8;
		 String urlpattern="http://roll.mil.news.sina.com.cn/col/gjjq/index_{*}.shtml";
		 //分页面截取   
		 String start="<div\\s+class=\"fixList\">\\s+<ul\\s+class=\"linkNews\">";
	     String end="</ul>\\s+</div>\\s+<style>\\s+[.]page\\s+span\\s+[{]margin-right:5px[}]\\s+</style>";
		
	     //用于分隔每个文章的正则
	     String regx="(\\s+<li><a href=\"(http://mil.news.sina.com.cn/2012-\\d{2}-\\d{2}/\\d{3,}.html)\" target=\"_blank\">(.*?)</a><span class=\"time\">[(]\\d{2}月\\d{2}日 \\d{2}:\\d{2}[)]</span></li>)";
	     //截取内容   
	     String titleRegx="<title>(.*?)</title>";
			//String startCont="<!--正文内容begin-->\\s+<!-- google_ad_section_start -->\\s+<div class=\"blkContainerSblkCon\" id=\"artibody\">";
	     String startCont="<!--正文内容begin-->\\s+<!-- google_ad_section_start -->\\s+<div class=\"blkContainerSblkCon\" id=\"artibody\">";
	        
		 String endCont="<div\\s+style=\"clear:both;height:0;visibility:hiddden;overflow:hidden;\"></div><style type=\"text/css\">";
		
		
		for(int i=starxpage;i<=endpage;i++)
		{
		System.out.println("正在抓第"+i+"页............");
		// TODO Auto-generated method stub
        //第二页的所有数据
		String htmltxt=getStringByUrl(urlpattern.replace("{*}",new Integer(i).toString()));
        
		  //第二页文章的url列表
        List<String> list=getUrlByPage(regx,htmltxt);
        
        //截出中间的问分
        String titlelist=cutMiddleString(start,end,htmltxt);
        
        
	  //加入数据库
        try
		{


        for(int j=0;j<list.size();j++)
        {
        	System.out.println("....第"+i+"页,第"+j+"条....");
        	String [] result=parseContent(titleRegx, startCont, endCont, list.get(j));
            
           
            String prefix="(\\s+<!--\\s+publish_helper\\s+name='原始正文'\\s+p_id='\\d{2,}'\\s+t_id='\\d{1,}'\\s+d_id='\\d{6,10}'\\s+f_id='\\d{1,}'\\s+-->)";
            Matcher mm=Pattern.compile(prefix).matcher(result[1]);
            String finalcontent=null;
            if(mm.find())
            {
            	finalcontent=result[1].split(prefix)[1];
            }else
            {
            	finalcontent=result[1];
            }
            
            
            
           // String sql="insert into article(title,content,url) values('"+result[0]+"','"+delHTMLTag(finalcontent)+"','"+ list.get(j)+"')";
            String sql="insert into article(title,content,url) values('"+result[0]+"','"+finalcontent+"','"+ list.get(j)+"')";
            DBManager.getDBM().insertData(sql);
            
            //DBManager.getDBM().insertData(result[0],result[1],list.get(j));
            
            //PrintStream ps=new PrintStream("c:\\aa.html");
           // ps.println(result[1]);
        }
        
        
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}
    
        
        
        
	}

}
