package test;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import org.junit.Test;

import parser.ParseException;
import parser.SimpleNode;
import parser.SimplePCRE;

public class ParserTests {
	@SuppressWarnings("unused")
	public boolean global_test(String regex) {
		boolean thrown = false;
		try {
			SimplePCRE parser = new SimplePCRE(new ByteArrayInputStream(regex.getBytes()));
			SimpleNode n = parser.Start();
			thrown = true;
		} catch (ParseException e) {
			thrown = false;
		}
		return thrown;
	}
	
	@Test
	//RE: "a""b"
	public void test0() throws parser.ParseException {
		String regex = "\"a\"\"b\"\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}	
	
	@Test
	//RE: "a"|"b"
	public void test1() throws parser.ParseException {
		String regex = "\"a\"|\"b\"\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}	
	
	@Test
	//RE: "a"?
	public void test2() throws parser.ParseException {
		String regex = "\"a\"?\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}	
	
	@Test
	//RE: "a"{2,5}"b"
	public void test3() throws parser.ParseException {
		String regex = "\"a\"{2,3}\"b\"\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}	

	@Test
	//RE: a"
	public void test4() throws parser.ParseException {
		String regex = "a\"\n";
		boolean thrown = global_test(regex);
		assertFalse(thrown);
	}

	@Test
	//RE: ("a"|"b")*"c"
	public void test5() throws parser.ParseException {
		String regex = "(\"a\"|\"b\")*\"c\"\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}
	
	@Test
	//RE: ("a""b""cd"|"q")
	public void test6() throws parser.ParseException {
		String regex = "(\"a\"\"b\"\"cd\"|\"q\")\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}
	
	@Test
	//RE: +("a""b""cd"|"q")
	public void test7() throws parser.ParseException {
		String regex = "+(\"a\"\"b\"\"cd\"|\"q\")\n";
		boolean thrown = global_test(regex);
		assertFalse(thrown);
	}
	
	@Test
	//RE: ("a"+"b"?"cd"|"q"){2}
	public void test8() throws parser.ParseException {
		String regex = "(\"a\"+\"b\"?\"cd\"|\"q\"){2}\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}
	
	@Test
	//RE: "a"?("bCd"{2,}"gt"?|"w"{3,5}"Rr"+"ttt"){5}"avQ"*
	public void test9() throws parser.ParseException {
		String regex = "\"a\"?(\"bCd\"{2,}\"gt\"?|\"w\"{3,5}\"Rr\"+\"ttt\"){5}\"avQ\"*\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}
	
	@Test
	//RE: ("B3N"{2})?("Aa1"?"e3"+)*"ff4"{3,}(("A"|"Css"{8,9})|"CV54"+)*
	public void test10() throws parser.ParseException {
		String regex = "(\"B3N\"{2})?(\"Aa1\"?\"e3\"+)*\"ff4\"{3,}((\"A\"|\"Css\"{8,9})|\"CV54\"+)*\n";
		boolean thrown = global_test(regex);
		assertTrue(thrown);
	}
	
	@Test
	//RE: ("a"+"b"?
	public void test11() throws parser.ParseException {
		String regex = "(\"a\"+\"b\"?\n";
		boolean thrown = global_test(regex);
		assertFalse(thrown);
	}
	
	@Test
	//RE: +*{2,3}
	public void test12() throws parser.ParseException {
		String regex = "+*{2,3}\n";
		boolean thrown = global_test(regex);
		assertFalse(thrown);
	}

	
	

}
