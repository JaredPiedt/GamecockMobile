package com.gamecockmobile.news;

public class FeedMessage {

	String title;
	String description;
	String link;
	String author;
	String guid;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String toString() {
		String s = "";
		s += "FeedMessage: [\n";
		s += "title=" + title + "\n";
		s += ", description=" + description + "\n";
		s += ", link=" + link + "\n";
	    s += ", author=" + author + "\n";
	    s += ", guid=" + guid+ "]";
	    return s;
	}
}
