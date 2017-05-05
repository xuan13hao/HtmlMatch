package com.ccit;

public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
         String str="<!-- publish_helper name='原始正文' p_id='27' t_id='1' d_id='682811' f_id='2' -->";
         String rex=">\\s*<!-- publish_helper name='原始正文' p_id='27' t_id='1' d_id='682811' f_id='2' -->";
	     System.out.println(str.matches(rex));
	}

}
