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
   * ��ȡ�м���ַ���
   * @param start  ��ʼ������
   * @param end    ��ֹ������
   * @param target Ŀ���ַ���
   * @return
   */
	public static String cutMiddleString(String start,String end,String target)
	{
		   String[] sp1=target.split(start);
		   String[] sp2=sp1[1].split(end);
		   
		   return sp2[0];
	}
	
   /**
    * ����urlȡ����ǰҳ�ϵ�ÿ�����µ�url����
    * @param html ��ǰҪ������html
    * @return
    */
	public static List<String> getUrlByPage(String regx,String html)
	{
		List<String> list=new ArrayList<String>();
	  
	   
	   //String test="							<li><a href=\"http://mil.news.sina.com.cn/2012-02-23/0944682918.html\" target=\"_blank\">�����������������������ڻ�����(ͼ)</a><span class=\"time\">(02��23�� 09:44)</span></li>";
	   //String regx="(\\s+<li><a href=\"(http://mil.news.sina.com.cn/2012-\\d{2}-\\d{2}/\\d{3,}.html)\" target=\"_blank\">(.*?)</a><span class=\"time\">[(]\\d{2}��\\d{2}�� \\d{2}:\\d{2}[)]</span></li>)";
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
        
        //String startCont="<!--��������begin-->\\s+<!-- google_ad_section_start -->\\s+<div class=\"blkContainerSblkCon\" id=\"artibody\">";
       // String endCont="<div\\s+style=\"clear:both;height:0;visibility:hiddden;overflow:hidden;\"></div><style type=\"text/css\">";
        String fcontent=cutMiddleString(startCont,endCont,content);
        re[1]=fcontent;
        
        
        return re;
        
	}	
	
	/**
	 * ��url�����ݶ���һ���ַ���
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
		System.out.println("URL�����ڣ����﷨����ȷ");
		} catch (IOException e)
		{
			System.out.println("��URL����");
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	

	    public static String delHTMLTag(String htmlStr){ 
	        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //����script��������ʽ 
	        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //����style��������ʽ 
	       
	         
	        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        Matcher m_script=p_script.matcher(htmlStr); 
	        htmlStr=m_script.replaceAll(""); //����script��ǩ 
	         
	        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        Matcher m_style=p_style.matcher(htmlStr); 
	        htmlStr=m_style.replaceAll(""); //����style��ǩ 
	         
	 
	        return htmlStr.trim(); //�����ı��ַ��� 
	    } 
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		 int starxpage=6;
		 int endpage=8;
		 String urlpattern="http://roll.mil.news.sina.com.cn/col/gjjq/index_{*}.shtml";
		 //��ҳ���ȡ   
		 String start="<div\\s+class=\"fixList\">\\s+<ul\\s+class=\"linkNews\">";
	     String end="</ul>\\s+</div>\\s+<style>\\s+[.]page\\s+span\\s+[{]margin-right:5px[}]\\s+</style>";
		
	     //���ڷָ�ÿ�����µ�����
	     String regx="(\\s+<li><a href=\"(http://mil.news.sina.com.cn/2012-\\d{2}-\\d{2}/\\d{3,}.html)\" target=\"_blank\">(.*?)</a><span class=\"time\">[(]\\d{2}��\\d{2}�� \\d{2}:\\d{2}[)]</span></li>)";
	     //��ȡ����   
	     String titleRegx="<title>(.*?)</title>";
			//String startCont="<!--��������begin-->\\s+<!-- google_ad_section_start -->\\s+<div class=\"blkContainerSblkCon\" id=\"artibody\">";
	     String startCont="<!--��������begin-->\\s+<!-- google_ad_section_start -->\\s+<div class=\"blkContainerSblkCon\" id=\"artibody\">";
	        
		 String endCont="<div\\s+style=\"clear:both;height:0;visibility:hiddden;overflow:hidden;\"></div><style type=\"text/css\">";
		
		
		for(int i=starxpage;i<=endpage;i++)
		{
		System.out.println("����ץ��"+i+"ҳ............");
		// TODO Auto-generated method stub
        //�ڶ�ҳ����������
		String htmltxt=getStringByUrl(urlpattern.replace("{*}",new Integer(i).toString()));
        
		  //�ڶ�ҳ���µ�url�б�
        List<String> list=getUrlByPage(regx,htmltxt);
        
        //�س��м���ʷ�
        String titlelist=cutMiddleString(start,end,htmltxt);
        
        
	  //�������ݿ�
        try
		{


        for(int j=0;j<list.size();j++)
        {
        	System.out.println("....��"+i+"ҳ,��"+j+"��....");
        	String [] result=parseContent(titleRegx, startCont, endCont, list.get(j));
            
           
            String prefix="(\\s+<!--\\s+publish_helper\\s+name='ԭʼ����'\\s+p_id='\\d{2,}'\\s+t_id='\\d{1,}'\\s+d_id='\\d{6,10}'\\s+f_id='\\d{1,}'\\s+-->)";
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
