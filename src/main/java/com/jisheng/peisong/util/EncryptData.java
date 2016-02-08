package com.jisheng.peisong.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * @Filename: EncryptData.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年9月6日
 */
public class EncryptData {
    private static Random myRand;
    private static SecureRandom mySecureRand;
    private static String s_id;
    private static Object lockObj;
    
    static 
    {
        lockObj = new Object();
        synchronized(lockObj)
        {
            mySecureRand = new SecureRandom();
            long secureInitializer = mySecureRand.nextLong();
            myRand = new Random(secureInitializer);
            try
            {
                s_id = InetAddress.getLocalHost().toString();
            }
            catch(UnknownHostException e)
            {
                e.printStackTrace();
            }
        }
    }
    
	byte[] encryptKey;
	DESedeKeySpec spec;
	SecretKeyFactory keyFactory;
	SecretKey theKey;
	Cipher cipher;
	IvParameterSpec IvParameters;

	public EncryptData()
	{
		try
		{
			// 检测是否有 TripleDES 加密的供应程序
			// 如无，明确地安装SunJCE 供应程序
			try{ Cipher c = Cipher.getInstance("DESede");c.getClass(); }
			catch (Exception e)
			{
				System.err.println("Installling SunJCE provider.");
				Provider sunjce = new com.sun.crypto.provider.SunJCE();
				Security.addProvider(sunjce);
			}
			// 创建一个密钥
			encryptKey = "This is a test DESede Key".getBytes();

			// 为上一密钥创建一个指定的 DESSede key
			spec = new DESedeKeySpec(encryptKey);

			// 得到 DESSede keys
			keyFactory = SecretKeyFactory.getInstance("DESede");

			// 生成一个 DESede 密钥对象
			theKey = keyFactory.generateSecret(spec);

			// 创建一个 DESede 密码
			cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

			// 为 CBC 模式创建一个用于初始化的 vector 对象
			IvParameters =
				new IvParameterSpec(new byte[]{12,34,56,78,90,87,65,43} );
		}
		catch (Exception exc)
		{
			// 记录加密或解密操作错误
		}
	}
	
	/**
	 * 
	 * @param key 密钥
	 */
	public EncryptData(String key){
		try
		{
			// 检测是否有 TripleDES 加密的供应程序
			// 如无，明确地安装SunJCE 供应程序
			try{ Cipher c = Cipher.getInstance("DES");c.getClass(); }
			catch (Exception e)
			{
				System.err.println("Installling SunJCE provider.");
				Provider sunjce = new com.sun.crypto.provider.SunJCE();
				Security.addProvider(sunjce);
			}
			// 创建一个密钥
			encryptKey = key.getBytes();

			// 为上一密钥创建一个指定的 DESSede key
			spec = new DESedeKeySpec(encryptKey);

			// 得到 DESSede keys
			keyFactory = SecretKeyFactory.getInstance("DESede");

			// 生成一个 DESede 密钥对象
			theKey = keyFactory.generateSecret(spec);

			// 创建一个 DESede 密码
			cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

			// 为 CBC 模式创建一个用于初始化的 vector 对象
			IvParameters =
				new IvParameterSpec(new byte[]{12,34,56,78,90,87,65,43} );
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			// 记录加密或解密操作错误
		}
	}

	/**
	 * 加密算法
	 * @param password   等待加密的密码
	 * @return           加密以后的密码
	 * @throws Exception
	 */
	public byte[] encrypt(String password)
	{
//		String encrypted_password = null;
		byte[] encrypted_pwd = null;

		try
		{
			// 以加密模式初始化密钥
			cipher.init(Cipher.ENCRYPT_MODE,theKey,IvParameters);

			// 加密前的密码（旧）
			byte[] plainttext = password.getBytes();

			// 加密密码
			encrypted_pwd = cipher.doFinal(plainttext);

			// 转成字符串，得到加密后的密码（新）
//			encrypted_password = new String(encrypted_pwd);
		}
		catch(Exception ex)
		{
			// 记录加密错误
		}
		return encrypted_pwd;
     }

	/**
	 * 解密算法
	 * @param password   加过密的密码
	 * @return           解密后的密码
	 */
	public String decrypt(byte[] password)
	{
		String decrypted_password = null;
		try
		{
			// 以解密模式初始化密钥
			cipher.init(Cipher.DECRYPT_MODE,theKey,IvParameters);

			// 构造解密前的密码
			byte[] decryptedPassword = password;

			// 解密密码
			byte[] decrypted_pwd = cipher.doFinal(decryptedPassword);
			// 得到结果
			
			decrypted_password = new String(decrypted_pwd);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			// 记录解密错误
		}
		return decrypted_password;
	}
    
    /**
     * 获取guid
     * @param secure 一般都是false
     * @return
     */
    public String getRandomGUID(boolean secure)
    {
        MessageDigest md5 = null;
        StringBuffer sbValueBeforeMD5 = new StringBuffer();
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException e)
        {
            System.out.println("Error: " + e);
        }
        try
        {
            long time = System.currentTimeMillis();
            long rand = 0L;
            if(secure)
                rand = mySecureRand.nextLong();
            else
                rand = myRand.nextLong();
            sbValueBeforeMD5.append(s_id);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(rand));
            String valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update(valueBeforeMD5.getBytes());
            byte array[] = md5.digest();
            StringBuffer sb = new StringBuffer();
            for(int j = 0; j < array.length; j++)
            {
                int b = array[j] & 0xff;
                if(b < 16)
                    sb.append('0');
                sb.append(Integer.toHexString(b));
            }

            String raw = sb.toString().toUpperCase();
            StringBuffer re = new StringBuffer();
            re.append(raw.substring(0, 8));
            re.append("-");
            re.append(raw.substring(8, 12));
            re.append("-");
            re.append(raw.substring(12, 16));
            re.append("-");
            re.append(raw.substring(16, 20));
            re.append("-");
            re.append(raw.substring(20));

            return re.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
	
	/**
     * 生成md5码
     * @str String 源字符串
     * @return md5编码后的字符串
     */
    public String encryptMD5(String str) {
        try {
            MessageDigest md5sum = MessageDigest.getInstance("MD5");
            byte[] digest = md5sum.digest(str.getBytes());
            String thischecksum = "";
            for (int i = 0; i < digest.length; i++) {
                int tint = (int) (digest[i] & 0x000000ff);
                String tstr = Integer.toHexString(tint);
                if (tint >= 16) {
                    thischecksum += tstr;
                } else {
                    thischecksum += "0" + tstr;
                }
            }
            return thischecksum;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 获取hashcode
     * @return
     */
    public String getHashcode() {
    	return encryptMD5(new java.util.Date().getTime() + getRandomGUID(false));
    }
     
     public static void main(String[] args) throws Exception {
    	 EncryptData ed = new EncryptData("~!@#$%^&*()peingsong)(*&");
//    	 BASE64Encoder enc=new BASE64Encoder();
//    	 byte[] bt = ed.encrypt("{key:'md5',email:'encrypt_email',password:'encrypt_password',nickname:'encrypt_nickname'}");
//    	 String s = enc.encode(bt);
//    	 System.out.println("s:"+s);
    	 
//    	 String s = "admin!@#";
//		 BASE64Decoder dec=new BASE64Decoder();   
//		 byte[] nt;
//		 nt = dec.decodeBuffer(s);
//		 String t  = ed.decrypt(nt);
//		 System.out.println("t:"+t);
		 
	   	 	BASE64Encoder enc=new BASE64Encoder();
	   	 	byte[] bt = ed.encrypt("admin");
	   	 String password = enc.encode(bt);
	   	 System.out.println("t:"+password);
    	
	   	 
	   	 
		 BASE64Decoder dec=new BASE64Decoder();   
		 byte[] nt;
		 nt = dec.decodeBuffer(password.replace(" ", "+"));
		 String t  = ed.decrypt(nt);
		 System.out.println("t:"+t);
    	 
    	// BASE64Decoder dec=new BASE64Decoder();
    	// byte[] nt = dec.decodeBuffer(s);
    	// String t = ed.decrypt(nt);
    	 //System.out.println("t:"+t);
    	 
//    	 
//    	 byte[] tb = s.getBytes();
//    	 System.out.println(tb);
//    	 String t = ed.decrypt(tb);
//    	 System.out.println(t);
     }
}

