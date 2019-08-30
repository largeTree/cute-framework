package ws;

import java.lang.reflect.Field;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.qiuxs.ws.bytecode.ClassGenerator;

public class TestClassGenerator {
	
	@Test
	public void testGenClass() {
		ClassGenerator cg = ClassGenerator.newInstance();
		cg.setClassName("com.qiuxs.Test$$Proxy").addField("private final static javax.xml.namespace.QName QQONLINEWEBSERVICE_QNAME = new javax.xml.namespace.QName(\"http://WebXml.com.cn/\", \"qqOnlineWebService\");");
		Class<?> clz = cg.toClass();
		try {
			Field f = clz.getDeclaredField("QQONLINEWEBSERVICE_QNAME");
			f.setAccessible(true);
			QName qName = (QName) f.get(clz.newInstance());
			System.out.println(qName.getNamespaceURI());
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
