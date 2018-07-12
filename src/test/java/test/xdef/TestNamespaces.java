/*
 * File: TestNamespaces.java
 * Copyright 2009 Syntea.
 *
 * This file may be copied, modified and distributed only in accordance
 * with the terms of the limited licence contained in the accompanying
 * file LICENSE.TXT.
 *
 * Tento soubor muze byt kopirovan, modifikovan a siren pouze v souladu
 * s textem prilozeneho souboru LICENCE.TXT, ktery obsahuje specifikaci
 * prislusnych prav.
 */
package test.xdef;

import cz.syntea.xdef.sys.ArrayReporter;
import cz.syntea.xdef.sys.Report;
import cz.syntea.xdef.XDPool;

/** Test of references with NameSpace.
 * @author Vaclav Trojan
 */
public final class TestNamespaces extends Tester {

	public TestNamespaces() {
		super();
/*#if DEBUG*/
		setChkSyntax(true);
		setGenObjFile(true);
/*#end*/
	}

	@Override
	public void test() {
		String xdef;
		String xml;
		ArrayReporter reporter = new ArrayReporter();
		XDPool xp;
		Report rep;
		try {
			xdef =
"<xd:def xmlns:xd = '" + XDEFNS + "'\n"+
"  xd:name='Test' xmlns='A' xmlns:a='B' root='a|a:a'>\n"+
"\n"+
"  <a:a xd:script='occurs 2' xmlns:a='C' c='required string()' />\n"+
"  <a:a xmlns:a='B' b='required string()' />\n"+
"  <a:a xmlns:a='A' a='required string()' >\n"+
"    <a:a xmlns:a='C' xd:script='ref a:a' />\n"+
"  </a:a>\n"+
"\n"+
"</xd:def>";
			xp = compile(xdef);
			xml = "<a:a xmlns:a='A' a='a'>" +
				"<c:a xmlns:c='C' c='C'/>" +
				"<c:a xmlns:c='C' c='C'/>" +
				"</a:a>";
			assertEq(xml, parse(xp, "Test", xml, reporter));
			assertNoErrors(reporter);
			xml = "<a:a xmlns:a = 'B' b = 'b' />";
			assertEq(xml, parse(xp, "Test", xml, reporter));
			assertNoErrors(reporter);
			xml = "<a:a xmlns:a = 'B' a = 'a' />";
			parse(xp, "Test", xml, reporter);
			assertTrue(reporter.errorWarnings(), "Error not recognized");
			xml = "<a:a xmlns:a = 'A' b = 'b' />";
			parse(xp, "Test", xml, reporter);
			assertTrue(reporter.errorWarnings(), "Error not recognized");
			xml = "<a:a xmlns:a='A' a='a'><c:a xmlns:c='B' c='C'/></a:a>";
			parse(xp, "Test", xml, reporter);
			assertTrue(reporter.errorWarnings(), "Error not recognized");
		} catch (Exception ex) {fail(ex);}
		try {
			xdef =
"<xd:collection xmlns:xd = '" + XDEFNS + "'>\n"+
"\n"+
"<xd:def xmlns='A' xmlns:a='B' xd:name='Test' root='A#a|A#a:a'>\n"+
"  <a:a xd:script='occurs 2' xmlns:a='C' c='required string()' />\n"+
"</xd:def>\n"+
"<xd:def xd:name='A'>\n"+
"  <a:a xmlns:a='B' b='required string()' />\n"+
"  <b:a xmlns:b='A' a='required string()' >\n"+
"    <a:a xmlns:a='C' xd:script='ref Test#a:a' />\n"+
"  </b:a>\n"+
"</xd:def>\n"+
"\n"+
"</xd:collection>";
			xp = compile(xdef);
			xml = "<a:a xmlns:a='A' a='a'>" +
				"<c:a xmlns:c='C' c='C'/>" +
				"<c:a xmlns:c='C' c='C'/>" +
				"</a:a>";
			assertEq(xml, parse(xp, "Test", xml, reporter));
			assertNoErrors(reporter);
			xml = "<a:a xmlns:a = 'B' b = 'b' />";
			assertEq(xml, parse(xp, "Test", xml, reporter));
			assertNoErrors(reporter);
			xml = "<a:a xmlns:a = 'B' a = 'a' />";
			parse(xp, "Test", xml, reporter);
			assertTrue(reporter.errorWarnings(), "Error not recognized");
			xml = "<a:a xmlns:a = 'A' b = 'b' />";
			parse(xp, "Test", xml, reporter);
			assertTrue(reporter.errorWarnings(), "Error not recognized");
			xml = "<a:a xmlns:a='A' a='a'><c:a xmlns:c='B' c='C'/></a:a>";
			parse(xp, "Test", xml, reporter);
			assertTrue(reporter.errorWarnings(), "Error not recognized");
		} catch (Exception ex) {fail(ex);}
		try {
			xdef =
"<xd:collection xmlns:xd='" + XDEFNS + "'>\n"+
"<xd:def name='A' root='x:a|*' xmlns:x='abcdef'>\n"+
"  <x:a>\n"+
"    <x:b xd:script = 'occurs 0..;' >\n"+
"      <x:c xd:script = 'occurs 0..; ref cc' />\n"+
"    </x:b>\n"+
"  </x:a>\n"+
"  <cc a1='required string()' a2='optional string()' />\n"+
"</xd:def>\n"+
"</xd:collection>";
			xp = compile(xdef);
			xml = //1. Default namespace
"<a xmlns='abcdef'>\n"+
"<b><c a1='1'/>\n"+
"<c a1='2'/>\n"+
"<c a1='3'/>\n"+
"</b>\n"+
"<b><c a1='1'/><c a1='2'/><c a1='3'/></b>\n"+
"<b><c a1='1'/><c a1='2'/><c a1='3'/></b>\n"+
"<b><c a1='1'/><c a1='2'/><c a1='3'/></b>\n"+
"<b><c a1='1'/></b>\n"+
"<b></b>\n"+
"<b><c a1='1' a2='2'/></b>\n"+
"<b><c a1='1'/></b>\n"+
"<b><c a1='1'/></b>\n"+
"</a>";
			parse(xp, "A", xml, reporter);
			assertNoErrors(reporter);
			xml = //2. Named namespace
"<a:a xmlns:a='abcdef'>\n"+
"<a:b><a:c a1='1'/>\n"+
"<a:c a1='2'/>\n"+
"<a:c a1='3'/>\n"+
"</a:b>\n"+
"<a:b><a:c a1='1'/><a:c a1='2'/><a:c a1='3'/></a:b>\n"+
"<a:b><a:c a1='1'/><a:c a1='2'/><a:c a1='3'/></a:b>\n"+
"<a:b><a:c a1='1'/><a:c a1='2'/><a:c a1='3'/></a:b>\n"+
"<a:b><a:c a1='1'/></a:b>\n"+
"<a:b></a:b>\n"+
"<a:b><a:c a1='1' a2='2'/></a:b>\n"+
"<a:b><a:c a1='1'/></a:b>\n"+
"<a:b><a:c a1='1'/></a:b>\n"+
"</a:a>";
			parse(xp, "A", xml, reporter);
			assertNoErrors(reporter);
			xml = //3. any (with namespace)
"<a:p xmlns:a='abcdef'><q a='a'><r/></q></a:p>\n";
			parse(xp, "A", xml, reporter);
			assertNoErrors(reporter);
			xml = //5. here should be error
"<a:a xmlns:a='abcdef'><q a='a'><r/></q></a:a>\n";
			parse(xp, "A", xml, reporter);
			if ((rep = reporter.getReport()) == null) {
				fail("Error not reported");
			} else if (!"XDEF501".equals(rep.getMsgID())) {
				fail("Incorrect report: " + rep);
			}
			while((rep = reporter.getReport()) != null) {
				fail(rep.toString());
			}
		} catch (Exception ex) {fail(ex);}
	}

	/** Run test
	 * @param args the command line arguments
	 */
	public static void main(String... args) {
		if (runTest() != 0) {System.exit(1);}
	}

}
