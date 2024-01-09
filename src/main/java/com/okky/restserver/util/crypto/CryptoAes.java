package com.okky.restserver.util.crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CryptoAes {

	public static String Decrypt(String text, String key, boolean urlEnc)
			throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length)
			len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		byte[] results = cipher.doFinal(urlEnc ? Base64.getUrlDecoder().decode(text) : Base64.getDecoder().decode(text));
		
		return new String(results, "UTF-8");
	}

	public static String Encrypt(String text, String key, boolean urlEnc)
			throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length)
			len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

		return urlEnc ? Base64.getUrlEncoder().encodeToString(results) : Base64.getEncoder().encodeToString(results);	
		
	}

	// Decoding(WMS용 tracecount)
	public static String wmsDecrypt(String text, String key)
			throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] aesIv = { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
		byte[] keyBytes = new byte[32];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		
		if (len > keyBytes.length)
			len = keyBytes.length;
		
		System.arraycopy(b, 0, keyBytes, 0, len);		
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(aesIv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] results = cipher.doFinal(Base64.getDecoder().decode(text));

		return new String(results, "UTF-8");

	}

	// Eecoding(WMS용 tracecount)
	public static String wmsEncrypt(String text, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] aesIv = { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
		byte[] keyBytes = new byte[32];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		
		if (len > keyBytes.length)
			len = keyBytes.length;
		
		System.arraycopy(b, 0, keyBytes, 0, len);		
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(aesIv);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

		return Base64.getEncoder().encodeToString(results);

	}

}