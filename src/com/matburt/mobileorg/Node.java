package com.matburt.mobileorg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.app.Activity;

class EditNode {
    public String editType;
    public String nodeId;
    public String title;
    public String oldVal = "";
    public String newVal = "";
    public boolean applied = false;  // True if applied to in-memory copy of node
}

class Node implements Cloneable {

    static int HEADER = 0;
    static int HEADING = 1;
    static int COMMENT = 2;
    static int DATA = 3;

    ArrayList<Node> subNodes = new ArrayList<Node>();
    ArrayList<String> tags = new ArrayList<String>();
    HashMap<String, String> properties = new HashMap<String, String>();

    String nodeName = "";
    String todo = "";
    String priority = "";
    String nodeId = "";
    int nodeType;
    String nodePayload = "";
    String nodeTitle = "";
    String tagString = "";
    String altNodeTitle = null;
    Date schedule = null;
    Date deadline = null;
    boolean encrypted = false;
    boolean parsed = false;
    Node parentNode = null;

    Node(String heading, int ntype) {
        this(heading, ntype, false);
    }

    Node(String heading, int ntype, boolean encrypted) {
        this.nodeName = heading;
        this.nodeType = ntype;
        this.encrypted = encrypted;
    }

    void setFullTitle(String title) {
        this.nodeTitle = title;
    }

    Node findChildNode(String regex) {
        Pattern findNodePattern = Pattern.compile(regex);
        for (int idx = 0; idx < this.subNodes.size(); idx++) {
            if (findNodePattern.matcher(this.subNodes.get(idx).nodeName).matches()) {
                return this.subNodes.get(idx);
            }
        }
        return null;
    }

    void setParentNode(Node pnode) {
        this.parentNode = pnode;
    }

    void addPayload(String npayload) {
        this.nodePayload += npayload + "\n";
    }

    void addChildNode(Node childNode) {
        this.subNodes.add(childNode);
    }

    void clearNodes() {
        this.subNodes.clear();
    }

    void addProperty(String key, String val) {
        this.properties.put(key, val);
    }

    String getProperty(String key) {
    	return this.properties.get(key);
    }

    boolean hasProperty(String key) {
        return this.properties.containsKey(key);
    }
    
    public void applyEdit(EditNode e) {
    	if (e.editType.equals("todo"))
    		todo = e.newVal;
    	else if (e.editType.equals("priority"))
    		priority = e.newVal;
    	else if (e.editType.equals("heading")) 
    		nodeName = e.newVal;
    	else if (e.editType.equals("body"))
    		nodePayload = e.newVal;
    }
    
    public void applyEdits(ArrayList<EditNode> edits) {
    	if (edits != null) {
    		for (EditNode e : edits) {
    			this.applyEdit(e);
    		}
    	}
    }
}