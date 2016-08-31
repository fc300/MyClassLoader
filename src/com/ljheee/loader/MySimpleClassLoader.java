package com.ljheee.loader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * CustomClassLoader
 * ��ʾ--�Զ����������
 * @author ljheee
 *
 */
public class MySimpleClassLoader extends ClassLoader{
	private String byteCode_Path;

	public MySimpleClassLoader(String byteCode_Path) {
		this.byteCode_Path = byteCode_Path;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte value[] = null;
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(byteCode_Path+name+".class"));
			value = new byte[in.available()];
			in.read(value);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{//�ͷ���Դ
			try {
				if(null != in)  in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//��byte����ת��Ϊһ�����Class����ʵ��
		return defineClass(value, 0, value.length);
	}
	//����
	public static void main(String[] args) throws ClassNotFoundException {
		
		MySimpleClassLoader loader = new MySimpleClassLoader("E:\\GitCode\\MyClassLoader\\bin\\com\\ljheee\\loader\\");
		System.out.println("��ǰ��ĸ��������"+loader.getParent().getClass().getName());
		System.out.println("����Ŀ������������"+loader.loadClass("Test").getClassLoader().getClass().getName());
	}
}
