// This file was generated by org.xdef.component.GenXComponent.
// XDPosition: "M#X".
// Any modifications to this file will be lost upon recompilation.
package test.xdef.component;
public class M implements org.xdef.component.XComponent{
//<editor-fold defaultstate="collapsed" desc="XComponent interface">
  @Override
  public org.w3c.dom.Element toXml()
    {return (org.w3c.dom.Element) toXml((org.w3c.dom.Document) null);}
  @Override
  public String xGetNodeName() {return XD_NodeName;}
  @Override
  public void xInit(org.xdef.component.XComponent p,
    String name, String ns, String xdPos) {
    XD_Parent=p; XD_NodeName=name; XD_NamespaceURI=ns; XD_Model=xdPos;
  }
  @Override
  public String xGetNamespaceURI() {return XD_NamespaceURI;}
  @Override
  public String xGetXPos() {return XD_XPos;}
  @Override
  public void xSetXPos(String xpos){XD_XPos = xpos;}
  @Override
  public int xGetNodeIndex() {return XD_Index;}
  @Override
  public void xSetNodeIndex(int index) {XD_Index = index;}
  @Override
  public org.xdef.component.XComponent xGetParent() {return XD_Parent;}
  @Override
  public Object xGetObject() {return XD_Object;}
  @Override
  public void xSetObject(final Object obj) {XD_Object = obj;}
  @Override
  public String toString() {return "XComponent: "+xGetModelPosition();}
  @Override
  public String xGetModelPosition() {return XD_Model;}
  @Override
  public int xGetModelIndex() {return -1;}
  @Override
  public org.w3c.dom.Node toXml(org.w3c.dom.Document doc) {
    if (doc==null) {
      return org.xdef.xml.KXmlUtils.parseXml(XD_Any)
        .getDocumentElement();
    } else {
      return (org.w3c.dom.Element)
        doc.adoptNode(org.xdef.xml.KXmlUtils.parseXml(XD_Any)
          .getDocumentElement());
    }
  }
  @Override
  public java.util.List<org.xdef.component.XComponent> xGetNodeList() {
    return new java.util.ArrayList<org.xdef.component.XComponent>();}
  public M() {}
  public M(org.xdef.component.XComponent p,
    String name, String ns, String xPos, String XDPos) {
    XD_NodeName=name; XD_NamespaceURI=ns;
    XD_XPos=xPos;
    XD_Model=XDPos;
    XD_Object = (XD_Parent=p)!=null ? p.xGetObject() : null;
  }
  public M(org.xdef.component.XComponent p, org.xdef.proc.XXNode xx){
    org.w3c.dom.Element el=xx.getElement();
    XD_NodeName=el.getNodeName(); XD_NamespaceURI=el.getNamespaceURI();
    XD_XPos=xx.getXPos();
    XD_Model=xx.getXMElement().getXDPosition();
    XD_Object = (XD_Parent=p)!=null ? p.xGetObject() : null;
    if (!"98983345EAC2589DECDB3670BED87864".equals(
      xx.getXMElement().getDigest())) { //incompatible element model
      throw new org.xdef.sys.SRuntimeException(
        org.xdef.msg.XDEF.XDEF374);
    }
  }
  private org.xdef.component.XComponent XD_Parent;
  private Object XD_Object;
  private String XD_NodeName = "$any";
  private String XD_NamespaceURI;
  private int XD_Index = -1;
  private String XD_XPos;
  private String XD_Model="M#X$any/$any";
  private String XD_Any;
  @Override
  public void xSetText(org.xdef.proc.XXNode xx,
    org.xdef.XDParseResult parseResult) {}
  @Override
  public void xSetAttr(org.xdef.proc.XXNode xx,
    org.xdef.XDParseResult parseResult) {}
  @Override
  public org.xdef.component.XComponent xCreateXChild(org.xdef.proc.XXNode xx)
    {return null;}
  @Override
  public void xAddXChild(org.xdef.component.XComponent xc) {}
  @Override
  public void xSetAny(org.w3c.dom.Element el) {
    XD_Any = org.xdef.xml.KXmlUtils.nodeToString(el);
  }
// </editor-fold>
}