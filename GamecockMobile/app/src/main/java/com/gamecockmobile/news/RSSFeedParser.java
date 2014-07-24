package com.gamecockmobile.news;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSFeedParser extends DefaultHandler {

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

    /******* Create ArrayList To Store XmlValuesModel object ******/
    list = new ArrayList<FeedMessage>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {

    /**** When New XML Node initiating to parse this function called *****/

    // Create StringBuilder object to store xml node value
    builder = new StringBuilder();

    if (qName.equals(ITEM)) {

      // Log.i("parse","----Job start----");
      /********** Create Model Object *********/
      feedMessage = new FeedMessage();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {

    if (qName.equals(ITEM) && !firstElement) {

      /** finished reading a job xml node, add it to the arraylist **/
      list.add(feedMessage);

    } else if (qName.equals(ITEM) && firstElement) {
      firstElement = false;
    } else if (qName.equalsIgnoreCase(TITLE) && !firstElement) {
      feedMessage.setTitle(builder.toString());

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

    /****** Read the characters and append them to the buffer ******/
    String tempString = new String(ch, start, length);
    builder.append(tempString);
  }
}
