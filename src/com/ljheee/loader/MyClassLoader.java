package com.ljheee.loader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ljheee.security.Use3DES;

/**
 * �Զ����������
 * �Լ��ܺ���ֽ�����н���
 * @author ljheee
 *
 */
public class MyClassLoader extends ClassLoader {

	/**
	 * ԭ �ֽ���·��
	 */
	private String byteCode_Path;
	
	/**
	 * ��Կ
	 */
	private byte[] key;
	
	public MyClassLoader(String byteCode_Path, byte[] key) {
		this.byteCode_Path = byteCode_Path;
		this.key = key;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] value = null;
		BufferedInputStream in = null;
		
		try {
			in = new BufferedInputStream(new FileInputStream(byteCode_Path+name+".class"));
			
			value = new byte[in.available()];
			in.read(value);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != in) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//�����ܺ���ֽ���--����
		value = Use3DES.decrypt(key, value);
		
		return defineClass(value, 0, value.length);//��byte����ת��Ϊһ�����Class����ʵ��
	}
	
	public static void main(String[] args) {
		BufferedInputStream in = null;
		try {
			//��ԭ  �ֽ����ļ�����src�ֽ����顣ע�⣺���ֽ����ļ����½�������Test������ģ�һ���ڹ���binĿ¼��
			in = new BufferedInputStream(new FileInputStream("E:\\GitCode\\MyClassLoader\\bin\\com\\ljheee\\loader\\Test.class"));
			byte[] src = new byte[in.available()];
			in.read(src);
			in.close();
			
			byte[] key = "01234567899876543210abcd".getBytes();//��Կ24λ
			
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("E:/GitCode/Test.class"));
			
			//���ֽ���  ���ܺ�д��"E:\\GitCode"
			out.write(Use3DES.encrypt(key, src));
			out.close();
			
			//�����Զ����������������Ŀ���ֽ���
			MyClassLoader loader = new MyClassLoader("E:/GitCode/", key);
			System.out.println(loader.loadClass("Test").getClassLoader().getClass().getName());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
