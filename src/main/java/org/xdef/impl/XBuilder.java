package org.xdef.impl;

import org.xdef.impl.code.DefDuration;
import org.xdef.impl.code.DefBoolean;
import org.xdef.impl.code.DefDecimal;
import org.xdef.impl.code.DefString;
import org.xdef.impl.code.DefNull;
import org.xdef.impl.code.DefBNFRule;
import org.xdef.impl.code.DefBytes;
import org.xdef.impl.code.DefDate;
import org.xdef.impl.code.DefLong;
import org.xdef.impl.code.DefBNFGrammar;
import org.xdef.impl.code.DefDouble;
import org.xdef.msg.XDEF;
import org.xdef.sys.ArrayReporter;
import org.xdef.sys.BNFGrammar;
import org.xdef.sys.BNFRule;
import org.xdef.sys.Report;
import org.xdef.sys.SDatetime;
import org.xdef.sys.SDuration;
import org.xdef.sys.SRuntimeException;
import org.xdef.XDBuilder;
import org.xdef.XDDocument;
import org.xdef.XDPool;
import org.xdef.XDValue;
import org.xdef.impl.compile.CompileXDPool;
import org.xdef.impl.debug.ChkGUIDisplay;
import org.xdef.impl.debug.XEditor;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import org.xdef.sys.ReportWriter;
import java.lang.reflect.Constructor;

/** Builder of XPool.
 * @author Vaclav Trojan
 */
@SuppressWarnings("deprecation") // see setReporter method
public class XBuilder implements XDBuilder {

	private XPool _xp;

	/** Creates instance of XDBuilder with properties and external objects.
	 * @param props Properties or <tt>null</tt>.
	 * @param extObjects The array of classes where are available methods
	 * referred from definitions (may be <tt>null</tt>).
	 */
	public XBuilder(final Properties props, final Class<?>... extObjects) {
		this(null, props, extObjects);
	}

	/** Creates instance of XDBuilder with properties and external objects.
	 * @param reporter ReportWriter or <tt>null</tt>.
	 * @param props Properties or <tt>null</tt>.
	 * @param extObjects The array of classes where are available methods
	 * referred from definitions (may be <tt>null</tt>).
	 */
	public XBuilder(final ReportWriter reporter,
		final Properties props,
		final Class<?>... extObjects) {
		_xp = new XPool(props, null, extObjects);
		if (reporter != null) {
			_xp._compiler.setReportWriter(reporter);
		}
		_xp._reporter = reporter;
	}

	@Override
	/** Add source data of X-definition or collection. If the argument starts
	 * with "&lt;" character then it is interpreted as source X-definition data,
	 * otherwise it can be the pathname of the file or URL. If it is a pathname
	 * format then it may contain also wildcard characters representing a group
	 * of files.
	 * @param source The string with source X-definition.
	 * @param sourceId name of source source data corresponding to
	 * the argument source (may be null).
	 */
	public final void setSource(final String source, final String sourceId) {
		_xp.setSource(source, sourceId);
	}

	@Override
	/** Add source data of X-definitions or collections. If an item starts with
	 * "&lt;" character then it is interpreted as source data, otherwise
	 * it can be the pathname of the file or URL. If it is a pathname format,
	 * then it may contain also wildcard characters representing a group
	 * of files.
	 * @param sources The string with sources.
	 */
	public final void setSource(final String... sources) {
		_xp.setSource(sources, null);
	}

	@Override
	/** Add source data of X-definitions or collections. If an item starts with
	 * "&lt;" character then it is interpreted as source data, otherwise
	 * it can be the pathname of the file or URL. If it is a pathname format,
	 * then it may contain also wildcard characters representing a group
	 * of files.
	 * @param sources The string with sources.
	 * @param sourceIds array of names of source source data corresponding to
	 * the sources argument (any item or even this argument
	 * may be <tt>null</tt>).
	 */
	public final void setSource(final String[] sources,
		final String[] sourceIds) {
		_xp.setSource(sources, sourceIds);
	}

	@Override
	/** Add files with source data of  X-definitions or collections.
	 * @param sources array of files with sources.
	 */
	public final void setSource(final File... sources) {_xp.setSource(sources);}

	@Override
	/** Add URLs with source data of X-definitions or collections.
	 * @param sources array of URLs with sources.
	 */
	public final void setSource(final URL... sources) {_xp.setSource(sources);}

	@Override
	/** Add input stream with source data of a X-definition or collection.
	 * @param source The input stream with source.
	 * @param sourceId name of source source data corresponding to
	 * stream from the argument sources (any item or even this argument
	 * may be <tt>null</tt>).
	 */
	public final void setSource(final InputStream source, final String sourceId) {
		_xp.setSource(source, sourceId);
	}

	@Override
	/** Add input streams with sources data of X-definitions or collections.
	 * @param sources array of input streams with sources.
	 * @param sourceIds array of names of source source data corresponding to
	 * the sources argument (any item may be null).
	 */
	public final void setSource(final InputStream sources[],
		final String sourceIds[]) {
		_xp.setSource(sources, sourceIds);
	}

	@Override
	/** Set external classes with external methods.
	 * @param ext array of classes with external methods.
	 */
	public final void setExternals(final Class<?>... ext) {
		_xp._compiler.setExternals(ext);
	}

	@Override
	/** Set reporter. This method is should be used only for incremental
	 * message reporting. The reporter must be set before setting sources.
	 * @param reporter the reporter to be set to this builder.
	 * @deprecated pleas use XDFactrory.getXDBuilder(ReportWriter, Properties)
	 */
	public final void setReporter(final ReportWriter reporter) {
		if (reporter != null) {
			_xp._compiler.setReportWriter(reporter);
		}
		_xp._reporter = reporter;
	}

	/** Get compiler.
	 * @return created XDPool.
	 */
	public final CompileXDPool getCompiler() {return _xp._compiler;}

	@Override
	/** Build XDPool from prepared sources.
	 * @return created XDPool.
	 */
	public final XDPool compileXD() {
		XPool result = _xp;
		if (result == null || result._compiler == null) {
			//XDPool object was already created
			throw new SRuntimeException(XDEF.XDEF901);
		}
		_xp = null;
		CompileXDPool p = result._compiler;
		result._compiler = null;
		ReportWriter userReporter = result._reporter; // user's reporter
		result._reporter = null;
		p.compileXPool(result);
		ArrayReporter reporter = (ArrayReporter) p.getReportWriter();
		byte displayMode = result.getDisplayMode();
		boolean display = displayMode == XPool.DISPLAY_TRUE
			|| (reporter.errorWarnings()&&(displayMode==XPool.DISPLAY_ERRORS));
		if (display) {
			Class<?>[] externals = p.getExternals(); //save external classes
			ArrayReporter ar = (ArrayReporter) reporter;
			XEditor xeditor = null;
			try {
				String xdefEditor = result.getXdefEditor();
				if (xdefEditor != null) {
					Class<?> cls = Class.forName(xdefEditor);
					Constructor<?> c = cls.getDeclaredConstructor();
					c.setAccessible(true);
					xeditor = (XEditor) c.newInstance();
				}
			} catch (Exception ex) {
				xeditor = null;
				// Class with the external debug editor &{0}{"}{"}
				// is not available.
				throw new SRuntimeException(
					XDEF.XDEF850, ex, result.getXdefEditor());
			}
			if (xeditor == null) {
				// create editor with the default screen position.
				xeditor = new ChkGUIDisplay(result.getXDSourceInfo());
			}
			while(!xeditor.setXEditor(result, ar)) {
				XDSourceInfo is = result.getXDSourceInfo();
				Map<String, XDSourceItem> map = is.getMap();
				// compile again
				result = new XPool(result.getProperties(),null, externals);
				XDSourceInfo is1 = result.getXDSourceInfo();
				// update source info (something might be changed)
				is1._xpos = is._xpos;
				is1._ypos = is._ypos;
				is1._width = is._width;
				is1._height = is._height;
				for (Map.Entry<String, XDSourceItem> e: map.entrySet()) {
					String key = e.getKey();
					XDSourceItem src = e.getValue();
					if (src._source != null) {
						result.setSource(src._source, key);
						result.getXDSourceInfo().getMap().put(key, src);
					} else if (src._url != null) {
						result.setSource(src._url);
					}
				}
				// compile again
				p = result._compiler;
				p.compileXPool(result);
				ar = (ArrayReporter) p.getReportWriter();
				result._compiler = null;
			}
		}
		if (userReporter == null) {
			if (result.isChkWarnings()) {
				p.getReportWriter().checkAndThrowErrorWarnings();
			} else {
				p.getReportWriter().checkAndThrowErrors();
			}
		} else if (reporter != userReporter) {
			Report rep;
			while ((rep = reporter.getReport()) != null) {
				userReporter.putReport(rep);
			}
		}
		result.clearSourcesMap(!result.isDebugMode());
		return result;
	}

	@Override
	/** Set class loader. The class loader must be set before setting sources.
	 * @param loader class loader.
	 */
	public final void setClassLoader(final ClassLoader loader) {
		_xp._compiler.setClassLoader(loader);
	}

	/** Parse XML with X-definition declared in source input stream.
	 * @param source where to read XML.
	 * @param reporter used for error messages or <tt>null</tt>.
	 * @return created XDDocument object.
	 * @throws SRuntimeException if an error occurs.
	 */
	public final static XDDocument xparse(final InputStream source,
		final ReportWriter reporter) throws SRuntimeException {
		ChkDocument chkdoc = new ChkDocument(new Class<?>[0], null);
		chkdoc.xparse(source, null, reporter);
		return chkdoc;
	}

	/** Parse XML with X-definition declared in source.
	 * @param source URL, pathname direct to XML or direct XML.
	 * @param reporter used for error messages or <tt>null</tt>.
	 * @return created XDDocument object.
	 * @throws SRuntimeException if an error occurs.
	 */
	public final static XDDocument xparse(final String source,
		final ReportWriter reporter) throws SRuntimeException {
		ChkDocument chkdoc = new ChkDocument(new Class<?>[0], null);
		chkdoc.xparse(source, reporter);
		return chkdoc;
	}

	/** Builds XPool with properties and external objects from source.
	 * @param props Properties or <tt>null</tt>. If the argument is
	 * <tt>null</tt> then System properties object is used.
	 * @param extObjects The array of classes where are available methods
	 * referred from definitions (may be <tt>null</tt>).
	 * @param source source X-definition ()
	 * @return created XDPool object.
	 */
	static final XPool build(final Properties props,
		final Class<?>[] extObjects,
		final URL source) {
		XBuilder xb = new XBuilder(props, extObjects);
		xb.setSource(source);
		return (XPool) xb.compileXD();
	}

	/** Create XDValue object.
	 * @param obj the object from which XDValue will be created.
	 * It may be one of:
	 * <ul>
	 * <li>XDValue</li>
	 * <li>String</li>
	 * <li>Short, Integer, Long</li>
	 * <li>Float, Double</li>
	 * <li>BigDecimal</li>
	 * <li>BNFGrammar</li>
	 * <li>BNFRule</li>
	 * <li>Boolean</li>
	 * <li>Calendar, SDatetime</li>
	 * <li>SDuration</li>
	 * </ul>
	 * @return new XDValue object.
	 * @throws RuntimeException if the object from argument is not possible
	 * to convert to XDValue object.
	 */
	public final static XDValue createXDValue(final Object obj) {
		if (obj == null) {
			return new DefNull();
		} else if (obj instanceof XDValue) {
			return (XDValue) obj;
		} else if (obj instanceof String) {
			return new DefString((String) obj);
		} else if (obj instanceof Short ||
			obj instanceof Integer || obj instanceof Long) {
			return new DefLong(((Number) obj).longValue());
		} else if (obj instanceof Float || obj instanceof Double) {
			return new DefDouble(((Number) obj).doubleValue());
		} else if (obj instanceof BigDecimal) {
			return new DefDecimal((BigDecimal) obj);
		} else if (obj instanceof byte[]) {
			return new DefBytes((byte[]) obj);
		} else if (obj instanceof BNFGrammar) {
			return new DefBNFGrammar((BNFGrammar) obj);
		} else if (obj instanceof BNFRule) {
			return new DefBNFRule((BNFRule) obj);
		} else if (obj instanceof Boolean) {
			return new DefBoolean(((Boolean) obj));
		} else if (obj instanceof Calendar) {
			return new DefDate((Calendar) obj);
		} else if (obj instanceof SDatetime) {
			return new DefDate((SDatetime) obj);
		} else if (obj instanceof SDuration) {
			return new DefDuration((SDuration) obj);
		}
		//Can't convert the object of type '&{0}' to XDValue
		throw new SRuntimeException(XDEF.XDEF542, obj.getClass().getName());
	}
}