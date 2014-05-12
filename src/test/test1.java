package test;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import parser.ParseException;
import parser.SimpleNode;
import parser.SimplePCRE;

public class test1 {
	public boolean global_test(String regex) {
		boolean thrown = false;
		try {
			SimplePCRE parser = new SimplePCRE(new ByteArrayInputStream(
					regex.getBytes()));
			SimpleNode n = parser.Start();
		} catch (ParseException e) {
			thrown = true;
		}
		return thrown;
	}

	@Test
	public void test1() throws parser.ParseException {
		String regex = "(\"a\"|\"b\")*\"c\"\n";
		boolean thrown = global_test(regex);
		assertFalse(thrown);
	}

	@Test
	public void test2() throws parser.ParseException {
		String regex = "(\"a\"\"b\")*\"c\"\n";
		boolean thrown = global_test(regex);
		assertFalse(thrown);
	}

}
