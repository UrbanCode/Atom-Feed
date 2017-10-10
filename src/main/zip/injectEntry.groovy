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
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
String updated = sdf.format(date)
// Retrieve XML from URL
String xmlDocStr = ""
try {
    xmlDocStr = new URL(url).getText()
} catch (Exception ex) {
    throw new Exception("[Error] Invalid or unreachable Atom Feed URL.")
}

if (!xmlDocStr) {
    throw new RuntimeException("[Error] Unable to find content from the specified Atom Feed URL.")
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

// Create new Atom file
File atom = new File("atom-feed.xml")
atom << XmlUtil.serialize(xmlDoc)

apTool.setOutputProperty("AtomFeed", atom.getCanonicalPath())
apTool.setOutputProperties()

println ""
println "Successfully added new Atom Feed Entry!"
