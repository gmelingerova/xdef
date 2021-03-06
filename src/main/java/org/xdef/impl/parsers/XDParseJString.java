package org.xdef.impl.parsers;

import org.xdef.XDParseResult;

/** Parser of X-Script "jstring" type.
 * @author Vaclav Trojan
 */
public class XDParseJString extends XDParseAn {
	private static final String ROOTBASENAME = "jstring";
	public XDParseJString() {
		super();
	}
	@Override
	boolean parse(final XDParseResult p) {
		p.isSpaces();
		if (p.isChar('"')) { // quoted string
			StringBuilder sb = new StringBuilder();
			for (;;) {
				if (p.eos()) {
					return false;
				}
				if (p.isToken("\"\"")) {
					sb.append('"');
				} else if (p.isChar('"')) {
					p.setParsedValue(sb.toString());
					return true;
				} else {
					sb.append(p.peekChar());
				}
			}
		} else if (!p.eos()) {//not quoed string
			int pos = p.getIndex();
			char ch;
			while (!p.eos() && (ch = p.getCurrentChar()) != ' '
				&& ch != '\t' && ch != '\r' && ch != '\n') {
//				sb.append(ch = p.peekChar());
				ch = p.peekChar();
			}
			p.setParsedValue(p.getBufferPart(pos, p.getIndex()));
			return true;
		}
		return false;
	}
	@Override
	public String parserName() {return ROOTBASENAME;}
}