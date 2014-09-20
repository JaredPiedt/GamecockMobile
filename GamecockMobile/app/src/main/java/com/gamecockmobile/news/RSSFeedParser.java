package com.gamecockmobile.news;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static com.gamecockmobile.util.LogUtils.LOGD;
import static com.gamecockmobile.util.LogUtils.makeLogTag;

public class RSSFeedParser extends DefaultHandler {

    private static final String TAG = makeLogTag(RSSFeedParser.class);

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    StringBuilder builder;
    FeedMessage feedMessage = null;
    List<FeedMessage> list = null;
    boolean firstElement = true;


    @Override
    public void startDocument() throws SAXException {
        // Create ArrayList to store FeedMessage objects
        list = new ArrayList<FeedMessage>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        // Create StringBuilder object to store xml node value
        builder = new StringBuilder();

        if (qName.equals(ITEM)) {
            LOGD(TAG,"Starting to parse RSS Feed");
            // Create new FeedMessage object
            firstElement = false;
            feedMessage = new FeedMessage();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Try not using firstElement boolean because it could be causing first article to not be read in
        if (qName.equals(ITEM) && !firstElement) {
            /** finished reading a job xml node, add it to the arraylist **/
            list.add(feedMessage);
        } else if (qName.equals(ITEM) && firstElement) {
            firstElement = false;
        } else if (qName.equalsIgnoreCase(TITLE) && !firstElement) {
            feedMessage.setTitle(Jsoup.parse(builder.toString()).text());
        } else if (qName.equalsIgnoreCase(DESCRIPTION) && !firstElement) {
            feedMessage.setDescription(builder.toString());
        } else if (qName.equalsIgnoreCase(GUID) && !firstElement) {
            feedMessage.setGuid(builder.toString());
        } else if (qName.equalsIgnoreCase(AUTHOR) && !firstElement) {
            feedMessage.setAuthor(builder.toString());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Read the characters and append them to the buffer
        String tempString = new String(ch, start, length);
        builder.append(tempString);
    }
}
