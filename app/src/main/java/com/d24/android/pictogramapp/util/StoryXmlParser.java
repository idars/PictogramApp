package com.d24.android.pictogramapp.util;

import android.util.Log;
import android.util.Xml;

import com.d24.android.pictogramapp.model.Figure;
import com.d24.android.pictogramapp.model.Scene;
import com.d24.android.pictogramapp.model.Story;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StoryXmlParser {

	// No namespaces will be used in this occasion
	private static final String ns = null;

	/**
	 * Reads an XML file containing information about a previously saved story. This method collects
	 * information from the given XML file, and populates an instance of Story, as close to its
	 * original state as possible. The XML should look like this:
	 *
	 * <pre>{@code
	 * <?xml version='1.0' encoding='utf-8' ?>
	 * <story name="My very own story">
	 *   <scene background="#fffff9c4">
	 *     <figure id="29" x="100.61575", y="356.74945" size="300" rotation="0.0" mirrored="false" />
	 *     <figure id="1" x="330.33588", y="363.47583" size="300" rotation="0.0" mirrored="false" />
	 *     ...
	 *   </scene>
	 *   <scene>
	 *       ...
	 *   </scene>
	 * </story>
	 * }</pre>
	 *
	 * @param in the stream of which the XML will be read from
	 * @return the previously saved story
	 * @throws XmlPullParserException if the XML is badly formatted
	 * @throws IOException if the given story is not parsable
	 */
	public Story parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readStory(parser);
		} finally {
			in.close();
		}
	}

	private Story readStory(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "story");

		List<Scene> scenes = new ArrayList<>();
		String title = parser.getAttributeValue(ns, "title");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the scene tag
			if (name.equals("scene")) {
				scenes.add(readScene(parser));
			} else {
				skip(parser);
			}
		}

		return new Story(title, scenes);
	}

	private Scene readScene(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "scene");

		List<Figure> figures = new ArrayList<>();
		String background = parser.getAttributeValue(ns, "background");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("figure")) {
				figures.add(readFigure(parser));
			} else {
				skip(parser);
			}
		}

		return new Scene(background, figures);
	}

	private Figure readFigure(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "figure");
		Figure figure = null;

		try {
			int id = Integer.valueOf(parser.getAttributeValue(ns, "id"));
			String color = String.valueOf(parser.getAttributeValue(ns, "color"));
			float x = Float.valueOf(parser.getAttributeValue(ns, "x"));
			float y = Float.valueOf(parser.getAttributeValue(ns, "y"));
			int size = Integer.valueOf(parser.getAttributeValue(ns, "size"));
			float rotation = Float.valueOf(parser.getAttributeValue(ns, "rotation"));
			boolean mirrored = Boolean.valueOf(parser.getAttributeValue(ns, "mirrored"));
			figure = new Figure(id, color, x, y, size, rotation, mirrored);
		} catch (NumberFormatException e) {
			// Abort reading the figure
			Log.i("StoryXmlParser", "Found badly written argument, skipping figure");
		}

		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, ns, "figure");

		return figure;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}

		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
}
