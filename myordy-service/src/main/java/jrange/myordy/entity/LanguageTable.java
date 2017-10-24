package jrange.myordy.entity;

import java.io.Serializable;

import com.fasterxml.jackson.databind.node.ObjectNode;

public final class LanguageTable implements Serializable {

    private static final long serialVersionUID = 1L;

    private Language language;

	private ObjectNode languageTable;

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(final Language language) {
		this.language = language;
	}

	public ObjectNode getLanguageTable() {
		return languageTable;
	}

	public void setLanguageTable(final ObjectNode languageTable) {
		this.languageTable = languageTable;
	}

}
