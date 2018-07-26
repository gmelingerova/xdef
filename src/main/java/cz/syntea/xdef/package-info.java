/**
 * Contains interfaces for processing of X-definitions and the class 
 * XSFactory used for compilation of the X-definitions sources. This class
 * also provides tools for creation of some important objects used in
 * X-definitions.
 * <p>For any kind of usage it is necessary first to compile X-definition to
 * the object{@link cz.syntea.xdef.XDPool}. The XDPool object is fully reentrant
 * (so that it can be declared as static and it can be used by many threads).
 * XDPool contains compiled code generated form of all X-definitions.
 * To compile X-definitions use the {@link cz.syntea.xdef.XDFactory} - see
 * the static methods of this class.
 * To execute a X-definition you must first create an object {@link
 * cz.syntea.xdef.XDDocument}. This object contains pointer to the root
 * X-definition where processing will start and it contains also all instances
 * of objects needed for processing (variables etc). This object can be used
 * for validation processing and/or for construction of XML data.
 * In the multi-thread environment you must create this object for each thread.
 * <p>There are several typical modes of usage of X-definition tools:</p>
 * <UL>
 * <li>
 * The most frequented mode is the "parsing" mode. In this mode the
 * processor	validates and processes input data represented from the input
 * XML document. The	important property of this mode is the ability
 * to process XML files of unlimited size. If it is used the option "forget" in
 * X-definitions the size of input data may be many gigabytes and it is not
 * limited by the size of internal memory of the Java virtual machine.
 * The speed of processor is	typically over 1 megabyte per second. The method
 * which provides this mode is
 * {@link cz.syntea.xdef.XDDocument#xparse(java.io.File,
 * cz.syntea.xdef.sys.ReportWriter)}.
 * </li>
 *
 * <li>
 * Similar way of processing is the mode, where the XML document
 * is available to the processor in the form of object of the 
 * <tt><i>org.w3c.dom.Document</i></tt> or 
 * <tt><i>org.w3c.dom.Element</i></tt> type. The input data are processed
 * by recursive walking in the DOM tree. The size of the input data is of
 * course in this case limited by the available memory. The method which
 * provides this mode is {@link cz.syntea.xdef.XDDocument
 * #xparse(org.w3c.dom.Node, cz.syntea.xdef.sys.ReportWriter)}.
 * </li>
 * 
 * <li>
 * The construction mode is designed to construct XML documents
 * according to X-definition. In this mode the source data is generated by the
 * X-definition itself (i.e. XDPool): the date can be taken from XML object or
 * from external database or the external methods. The output data are created
 * according to given X-definition (see {@link cz.syntea.xdef.XDDocument
 * #xcreate(String, cz.syntea.xdef.sys.ReportWriter)} or {@link
 * cz.syntea.xdef.XDDocument#xcreate(javax.xml.namespace.QName,
 * cz.syntea.xdef.sys.ReportWriter)}.
 * </li>
 * </UL>
 * 
 * In the case you want to know properties of X-definition models of
 * processed objects you can use methods:

* <UL>
 * <li>{@link cz.syntea.xdef.XDDocument#getXMDefinition()}</li>
 * <li>{@link cz.syntea.xdef.proc.XXNode#getXMElement()}</li>
 * <li>{@link cz.syntea.xdef.proc.XXData#getXMData()}</li>
 * </UL>
 * 
 * Those methods return an object with properties of
 * processed data. The interfaces of such objects see package
 * <i><b>cz.syntea.xm</b></i>.
 * 
 * <UL>
 * <li>
 * <h2><tt>XDPool</tt></h2>
 * Contains pool of X-definitions compiled from the source (see
 * {@link cz.syntea.xdef.XDFactory}).
 * <p><b>Example:</b></p>
 * <pre><tt>
 * //Prepare X-definition file
 * File xdef = new File("./src/Example.xdef");
 * 
 * // 1. Create DefPool and XDDocument.
 * XDPool xpool = XDFactory.genXDPool(null, xdef); //creation of XDPool
 * XDDocument xdoc = xp.createXDDocument(name); //create of XDDocument
 *   ...
 * </tt></pre>
 * </li>
 * <li>
 * <h2><tt>XDDocument</tt></h2>
 * Validates XML source data (see
 * {@link cz.syntea.xdef.XDDocument#xparse(java.io.File,
 * cz.syntea.xdef.sys.ReportWriter)}).
 * <p><b>Example:</b></p>
 * <pre><tt>
 * // 2. Validate and process source XML data with X-definition.
 * ArrayReporter reporter = new ArrayReporter(); // here will be written errors
 * Element el = xd.xparse(sourceXml, reporter); //validate and process data
 * //now we have root element of parsed source data errors in variable el
 * //and list of errors in reporter
 * // 3. test if an error was reported
 * if (xd.errorWarnings()) {//error or warning reported?
 *    reporter.getReportReader().printReports(System.err);
 * } else {// ok, no errors or warnings reported
 *    ...
 * }
 * </tt></pre>
 * </li>
 * <li>Generates XML data from source data (see
 * {@link cz.syntea.xdef.XDDocument#xcreate(String,
 * cz.syntea.xdef.sys.ReportWriter)} or
 * {@link cz.syntea.xdef.XDDocument#xcreate(javax.xml.namespace.QName,
 * cz.syntea.xdef.sys.ReportWriter)} and
 * {@link cz.syntea.xdef.XDDocument#getElement()}).
 * <p><b>Example:</b></p>
 * <pre><tt>
 * //Set to ChkDocument parsed source element.
 * xdoc.setSourceContext(sourcedata); //element with source data
 * 
 * //construct XML document
 * ArrayReporter reporter = new ArrayReporter();
 * Element el = xdoc.xcreate("element namespace URI", "element name", reporter);
 * Element element = chkDoc.getElement();
 * 
 * /test errors
 * if (xdoc.errorWarnings()) {
 *    reporter.printReports(System.err); // print error messages
 * } else {
 *    System.out.println(KXmlUtils.nodeToString(element, true)); //print result
 * }
 * </tt></pre>
 * </li>
 * </UL>
 * 
 * <UL>
 * <li>
 * <h2><tt>GenXDefinition</tt></h2>
 * You can create the X-definition from a XML document when you
 * call the class GenXDefinition from command line:
 * see: {@link cz.syntea.xdef.util.GenXDefinition#main(String[])}.
 * </li>
 * </UL>
 */
package cz.syntea.xdef;
