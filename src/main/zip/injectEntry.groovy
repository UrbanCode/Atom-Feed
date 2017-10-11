/**
 * © Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.urbancode.air.AirPluginTool
import groovy.util.XmlParser
import groovy.xml.XmlUtil
import java.text.SimpleDateFormat
import org.xml.sax.SAXParseException

def apTool = new AirPluginTool(this.args[0], this.args[1])
def props = apTool.getStepProperties()

def filePath = props['filePath']
def id = props['id']
def title = props['title']
def link = props['link']
def summary = props['summary']
def url = props['url']
def atomIndex = props['atomIndex']
if (atomIndex == null || !atomIndex.isInteger()) {
    atomIndex = 0
} else {
    atomIndex = atomIndex.toInteger()
}

Date date = new Date()
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
String updated = sdf.format(date)

// Retrieve XML from URL or File
String xmlDocStr = ""
File atom = new File(filePath)
if (url) {
    try {
        xmlDocStr = new URL(url).getText("UTF-8")
    } catch (Exception ex) {
        throw new Exception("[Error] Invalid or unreachable Atom Feed URL.")
    }
} else if (atom.isFile()){
    xmlDocStr = atom.getText("UTF-8")
} else {
    throw new RuntimeException("[Error] Must specify a valid Atom Feed File or URL.")
}

if (!xmlDocStr) {
    throw new RuntimeException("[Error] Atom Feed content is empty.")
}

// Create new Entry XML
def xml = null
String entry = ""
try {
    entry =
        "<entry>" +
            "<title>${title}</title>" +
            "<link href='${link}'/>" +
            "<id>${id}</id>" +
            "<updated>${updated}</updated>" +
            "<summary>${summary}</summary>" +
        "</entry>"
    xml = new XmlParser(false, true).parseText(entry)
} catch (SAXParseException ex) {
    println "==== New Entry ===="
    println entry
    println "==================="
    throw new SAXParseException("[Error] Specified Entry XML is invalid.")
}
println "==== New Entry ===="
println XmlUtil.serialize(xml)
println "==================="


// Parse the URL Feed URL
def xmlDoc = null
try {
    xmlDoc = new XmlParser(false, true).parseText(xmlDocStr)
} catch (SAXParseException ex) {
    throw new SAXParseException("[Error] Specified Atom Feed from URL contains invalid XML.")
}


// Insert this new node at position 0 in the children of the first feed node
xmlDoc.children().add(atomIndex, xml)

// Update the <update> element with current timestamp
if (xmlDoc.updated) {
    xmlDoc.updated[0].value = updated
}
//println XmlUtil.serialize(xmlDoc)

// Update Atom file
atom.setText(XmlUtil.serialize(xmlDoc), "UTF-8")

apTool.setOutputProperty("AtomFeed", atom.getCanonicalPath())
apTool.setOutputProperties()

println ""
println "Successfully injected new Atom Feed Entry!"
