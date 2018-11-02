package com.huayin.printmanager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomUtils;

import com.huayin.common.util.DateTimeUtil;
import com.huayin.common.util.PackageUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.security.Digests;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.Encodes;

public class Test
{

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		for(int i=0;i<24;i++)
		{
			testOnlineCount(DateTimeUtil.addDate(DateTimeUtil.addSecond(new Date(), 3600*i),2));
		}
		
		//codeCreate();
		if (true)
			return;
		/*
		 * String str="???"; Pattern p=Pattern.compile("\\?"); Matcher m=p.matcher(str); while(m.find()) {
		 * System.out.println(m.start()); }
		 */
		try
		{
			String str = "from a where a=? and  b=? and c=?";
			Pattern p = Pattern.compile("(\\?)");
			Matcher m = p.matcher(str);
			StringBuffer sb = new StringBuffer();
			int i = 0;
			while (m.find())
			{
				m.appendReplacement(sb, "?" + (++i));
			}
			m.appendTail(sb);
			System.out.println(sb);

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println((User) null);
		System.out.println(new File(ClassLoader.getSystemResource("").getPath() + "menu.xml").getAbsolutePath());
		System.out.println(entryptPassword("test"));
		System.out.println(validatePassword("test", "58b25f3c683149f7e40bd1b0f569103b5213c8930301f911334b0084"));

		System.out.println(entryptPassword("admin"));
		System.out.println(validatePassword("admin", "20f7e81b1d38fe9f93c323c487688505007eaf8e2a3aaef5a3053e13"));
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword)
	{
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SysConstants.SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, SysConstants.HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证原始密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password)
	{
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, SysConstants.HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	public static void codeCreate()
	{

		// List<String> classNames = getClassName(packageName);
		List<String> classNames = PackageUtil.getClassName("com.huayin.printmanager.persist.enumerate", false);

		for (String className : classNames)
		{
			String _className = className.replace("com.huayin.printmanager.persist.enumerate.", "");
			String column = _className.substring(0, 1).toLowerCase() + _className.substring(1);
			String out = "public String get" + _className + "Text()" +

					"{" + "try" + "{" + "return  ((" + _className + ")Reflections.getFieldValue(this, \"" + column
					+ "\")).getText();" + "}" + "catch (Exception e1)" + "{" + "}" + "return \"-\";" + "}";
			System.out.println("\r\n");
			System.out.println(out);
		}
	}

	public static void testOnlineCount(Date date)
	{
		int count = 0;
		int baseCount = 0;
		int randomCount = 0;
		Date currTime = date;
		if (DateTimeUtil.getDayForWeek(currTime) == 1)
		{// 周日
			baseCount = 0;
			randomCount = RandomUtils.nextInt(0, 1000);
		}
		else
		{// 周一到周六
			if (DateUtils.isInTime("08:01-18:00", DateTimeUtil.formatToStr(currTime, "HH:mm")))
			{
				baseCount = 3000;
				randomCount = RandomUtils.nextInt(0, 4000);
			}
			else if (DateUtils.isInTime("18:00-20:30", DateTimeUtil.formatToStr(currTime, "HH:mm")))
			{
				baseCount = 1000;
				randomCount = RandomUtils.nextInt(0, 2000);
			}
			else if (DateUtils.isInTime("20:30-18:00", DateTimeUtil.formatToStr(currTime, "HH:mm")))
			{
				baseCount = 0;
				randomCount = RandomUtils.nextInt(0, 1000);
			}
		}
		count = baseCount + randomCount;
		
		System.out.println(DateTimeUtil.formatLongStr(currTime)+"             "+count);
	}

}
